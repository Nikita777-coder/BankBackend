package prog.rates.services;

import io.swagger.client.model.Currency;
import io.swagger.client.model.RatesResposne;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import prog.rates.dto.ConvertCurrencyResponse;
import prog.rates.dto.RatesRequest;
import prog.rates.services.restservices.RestService;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class RatesService {
    private final RestService restService;
    @Value("${service-configs.rates-service.path.public-url-path}")
    private String ratesPath;

    @Value("${service-configs.rates-service.rounding-mode}")
    private RoundingMode halfRoundingMode;
    @Retryable(
            maxAttemptsExpression = "${web-retry.max-attempts}",
            backoff = @Backoff(
                    delayExpression = "${web-retry.backoff.delay}",
                    multiplierExpression = "${web-retry.backoff.multiplier}",
                    maxDelayExpression = "${web-retry.backoff.max-delay}"
            )
    )
    public ConvertCurrencyResponse convert(RatesRequest ratesRequest) {
        Currency responseCurrency = ratesRequest.getCurrencyTo();

        RatesResposne ratesResponse = restService.get(ratesPath, RatesResposne.class);
        checkRatesResponseBody(ratesResponse);

        String currencyFrom = ratesRequest.getCurrencyFrom().getValue();
        String currencyTo = ratesRequest.getCurrencyTo().getValue();
        BigDecimal ratesAmount = ratesRequest.getAmount();

        BigDecimal responseAmount = calculateAmount
                (
                        ratesResponse,
                        currencyFrom,
                        currencyTo,
                        ratesAmount
                );

        return new ConvertCurrencyResponse(responseCurrency, responseAmount);
    }
    private BigDecimal calculateAmount(RatesResposne resposne,
                                       String currencyFrom,
                                       String currencyTo,
                                       BigDecimal amount) {
        var rates = resposne.getRates();
        BigDecimal toCurrencyRate = rates.get(currencyTo);
        BigDecimal fromCurrencyRate = rates.get(currencyFrom);

        checkCourseInRatesServer(fromCurrencyRate, currencyFrom, resposne.getBase().getValue());
        checkCourseInRatesServer(toCurrencyRate, currencyTo, resposne.getBase().getValue());

        if (currencyFrom.equals(resposne.getBase().getValue())) {
            return amount.divide(
                    toCurrencyRate,
                    2,
                    halfRoundingMode);
        }

        if (currencyTo.equals(resposne.getBase().getValue())) {
            return fromCurrencyRate.multiply(amount).setScale(2, halfRoundingMode);
        }

        return fromCurrencyRate.multiply(amount).divide(
                toCurrencyRate,
                2,
                halfRoundingMode);
    }

    private void checkCourseInRatesServer(BigDecimal rate, String currency, String base) {
        if (rate == null && !currency.equals(base)) {
            throw new ArithmeticException(String.format("для курса %s нет данных на сервере", currency));
        }
    }
    private void checkRatesResponseBody(RatesResposne response) {
        if (response == null || response.getRates() == null) {
            throw new IllegalStateException("не удалось распарсить дату сервера");
        }
    }
}
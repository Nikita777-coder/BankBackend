package prog.rates.mappers;

import io.swagger.client.model.Currency;
import net.devh.boot.grpc.example.ConverterServiceOuterClass;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import prog.rates.dto.RatesRequest;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface RatesMapper {
    RatesRequest dataToRatesRequest(String currencyFrom,
                                    String currencyTo,
                                    BigDecimal amount);
    @BeforeMapping
    default void checkRateRequestMappingData(String currencyFrom,
                                String currencyTo,
                                BigDecimal amount) {
        checkCurrency(currencyFrom);
        checkCurrency(currencyTo);
        checkAmount(amount);
    }

    default void checkCurrency(String currency) {
        if (Currency.fromValue(currency) == null) {
            throw new IllegalArgumentException(String.format("Валюта %s недоступна", currency));
        }
    }

    default void checkAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Отрицательная сумма");
        }
    }
}

package prog.rates.dto;

import io.swagger.client.model.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RatesRequest {
    private Currency currencyFrom;
    private Currency currencyTo;
    private BigDecimal amount;
}

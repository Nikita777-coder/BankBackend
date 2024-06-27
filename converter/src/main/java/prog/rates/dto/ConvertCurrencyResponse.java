package prog.rates.dto;

import io.swagger.client.model.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ConvertCurrencyResponse {
    private Currency currency;
    private BigDecimal amount;
}

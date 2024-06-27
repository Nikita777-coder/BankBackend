package accounts.dto.converter;

import io.swagger.client.model.Currency;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ConverterJavaResponse {
    private Currency currency;
    private BigDecimal amount;
}

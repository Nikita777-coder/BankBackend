package accounts.dto.converter;

import io.swagger.client.model.Currency;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ConverterJavaRequest {
    private Currency from;
    private Currency to;
    private BigDecimal amount;
}

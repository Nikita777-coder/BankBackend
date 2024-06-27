package accounts.dto.customer;

import io.swagger.client.model.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ProgramRatesResponse {
    private Currency currency;
    private BigDecimal amount;
}

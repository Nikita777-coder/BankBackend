package accounts.dto.account;

import io.swagger.client.model.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class AccountGetResponse {
    private BigDecimal amount;
    private Currency currency;
}

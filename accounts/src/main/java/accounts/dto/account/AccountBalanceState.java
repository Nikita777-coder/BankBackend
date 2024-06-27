package accounts.dto.account;

import io.swagger.client.model.Currency;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AccountBalanceState {
    private int accountNumber;
    private Currency currency;
    private BigDecimal balance;
}

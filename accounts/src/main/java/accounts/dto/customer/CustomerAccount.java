package accounts.dto.customer;

import io.swagger.client.model.Currency;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CustomerAccount {
    private int accountId;
    private Currency currency;
    private BigDecimal balance;
}

package accounts.dto.customer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class BalanceRequest {
    private int customerId;
    private String currency;
}

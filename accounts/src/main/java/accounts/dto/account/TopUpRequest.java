package accounts.dto.account;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TopUpRequest {
    private int accountNumber;

    @NotNull(message = "сумма пополнения не может быть не указана")
    private BigDecimal amount;
}

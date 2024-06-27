package accounts.dto.transfer;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class TransferRequest {
    @NotNull(message = "счёт получателя не может быть не указан")
    private int receiverAccount;

    @NotNull(message = "счёт отправителя не может быть не указан")
    private int senderAccount;

    @NotNull(message = "сумма перевода не может быть не указан")
    private BigDecimal amountInSenderCurrency;
}

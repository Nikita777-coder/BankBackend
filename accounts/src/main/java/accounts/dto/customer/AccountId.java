package accounts.dto.customer;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountId {
    @NotNull(message = "id аккаунта для удаления не может быть пустым!")
    private int id;
}

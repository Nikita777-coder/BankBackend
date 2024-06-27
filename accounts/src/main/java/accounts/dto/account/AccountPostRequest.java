package accounts.dto.account;

import io.swagger.client.model.Currency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class AccountPostRequest {
    @NotNull(message = "id покупателя не может быть не указано")
    private int customerId;

    @NotNull(message = "валюта не может быть не указана")
    private Currency currency;
}

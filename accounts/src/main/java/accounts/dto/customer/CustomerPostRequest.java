package accounts.dto.customer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class CustomerPostRequest {
    @NotNull(message = "имя не может быть не указана")
    private String firstName;

    @NotNull(message = "фамилия не может быть не указана")
    private String lastName;

    @NotNull(message = "дата рождения не может быть не указана")
    private String birthDay;
}

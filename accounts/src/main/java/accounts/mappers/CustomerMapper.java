package accounts.mappers;

import accounts.dto.customer.BalanceRequest;
import accounts.dto.customer.CustomerAccount;
import accounts.dto.customer.CustomerPostRequest;
import accounts.dto.customer.CustomerPostResponse;
import accounts.entities.CustomerEntity;
import accounts.entities.account.AccountEntity;
import jakarta.validation.constraints.Past;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerPostRequest makeAdditionalValidation(CustomerPostRequest request);
    @Mapping(ignore = true, target = "customerId")
    CustomerEntity customerPostRequestToCustomerEntity(CustomerPostRequest request);
    BalanceRequest createBalanceRequest(int customerId, String currency);
    CustomerPostResponse customerEntityToCustomerPostResponse(CustomerEntity entity);
    List<CustomerAccount> accountEntitiesToCustomerAccounts(List<AccountEntity> accountEntities);
    @BeforeMapping
    default void checkPersonAgeFromCustomerPostRequest(CustomerPostRequest request) {
        LocalDate newCustomerBirthDate;

        try {
            newCustomerBirthDate = LocalDate.parse(request.getBirthDay());
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("дата рождения представлена в некорректном виде");
        }

        if (newCustomerBirthDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("дата рождения должна быть до текущей даты!");
        }

        int personAge = LocalDate.now().getYear() - newCustomerBirthDate.getYear();
        if (personAge < 14 || personAge > 120) {
            throw new IllegalArgumentException("некорректный возраст");
        }
    }
}

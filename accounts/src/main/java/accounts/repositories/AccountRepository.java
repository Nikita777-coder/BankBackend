package accounts.repositories;

import accounts.entities.CustomerEntity;
import accounts.entities.account.AccountEntity;
import io.swagger.client.model.Currency;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends CrudRepository<AccountEntity, Integer> {
    Optional<AccountEntity> getAccountEntityByAccountId(int id);
    Optional<AccountEntity> getAccountEntityByCurrencyAndOwner(Currency currency, CustomerEntity customerEntity);
}

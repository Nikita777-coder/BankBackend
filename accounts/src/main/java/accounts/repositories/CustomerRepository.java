package accounts.repositories;

import accounts.entities.CustomerEntity;
import accounts.entities.account.AccountEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends CrudRepository<CustomerEntity, Integer> {
    Optional<CustomerEntity> getCustomerEntityByCustomerId(int id);
}

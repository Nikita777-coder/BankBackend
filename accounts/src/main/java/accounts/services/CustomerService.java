package accounts.services;

import accounts.dto.converter.ConverterJavaResponse;
import accounts.dto.customer.AccountId;
import accounts.dto.customer.BalanceRequest;
import accounts.dto.customer.BalanceResponse;
import accounts.dto.customer.CustomerAccount;
import accounts.dto.customer.CustomerPostRequest;
import accounts.dto.customer.CustomerPostResponse;
import accounts.entities.CustomerEntity;
import accounts.entities.account.AccountEntity;
import accounts.mappers.AccountMapper;
import accounts.mappers.CustomerMapper;
import accounts.mappers.GRPCMapper;
import accounts.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.example.ConverterServiceOuterClass;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final AccountMapper accountMapper;
    private final GRPCMapper grpcMapper;
    private final GRPCService grpcService;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public CustomerPostResponse createCustomer(CustomerPostRequest request) {
        log.info("createCustomer request from controller come to customer service");
        return customerMapper.customerEntityToCustomerPostResponse(customerRepository.save(customerMapper.customerPostRequestToCustomerEntity(request)));
    }

    public BalanceResponse getBalance(BalanceRequest request) {
        CustomerEntity foundCustomer = getCustomerById(request.getCustomerId());
        List<AccountEntity> customerAccounts = foundCustomer.getAccounts();
        BigDecimal totalBalance = new BigDecimal(0);

        for (var customerAccount: customerAccounts) {
            BigDecimal customerBalance;

            ConverterServiceOuterClass.ConvertRequest convertRequest = grpcMapper.dataToConvertRequest(
                    customerAccount.getCurrency().getValue(),
                    request.getCurrency(),
                    customerAccount.getBalance()
            );

            ConverterJavaResponse response = grpcService.convert(convertRequest);

            customerBalance = Objects.requireNonNull(response.getAmount());
            totalBalance = totalBalance.add(customerBalance);
        }

        return new BalanceResponse(totalBalance, request.getCurrency());
    }
    public List<CustomerAccount> getAllCustomerAccounts(int customerId) {
        return customerMapper.accountEntitiesToCustomerAccounts(getCustomerById(customerId).getAccounts());
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void deleteAccounts(int customerId, List<AccountId> accountIds) {
        CustomerEntity customer = getCustomerById(customerId);
        Map<Integer, AccountEntity> customerAccounts = accountMapper.listAccountEntitiesToMapAccountEntities(customer.getAccounts());

        for (AccountId accountId : accountIds) {
            AccountEntity obj = customerAccounts.remove(accountId.getId());

            if (obj == null) {
                log.warn(String.format("the customer with id %s doesn't have account with %s", customerId, accountId.getId()));
            }
        }

        customer.getAccounts().clear();
        customer.getAccounts().addAll(customerAccounts.values());

        customerRepository.save(customer);
    }
    public CustomerEntity getCustomerById(int id) {
        Optional<CustomerEntity> foundCustomer = customerRepository.getCustomerEntityByCustomerId(id);
        if (foundCustomer.isEmpty()) {
            throw new IllegalArgumentException("покупатель не найден");
        }

        return foundCustomer.get();
    }
}

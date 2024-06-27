package accounts.services;

import accounts.dto.account.AccountGetResponse;
import accounts.dto.account.AccountPostRequest;
import accounts.dto.account.AccountPostResponse;
import accounts.dto.account.TopUpRequest;
import accounts.entities.CustomerEntity;
import accounts.entities.account.AccountEntity;
import accounts.mappers.AccountMapper;
import accounts.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final CustomerService customerService;
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final SimpMessagingTemplate simpMessagingTemplate;
    @Value("${services-configs.web-socket.change-balance-account-destination}")
    private String changeAccountBalanceDestination;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public AccountPostResponse createAccount(AccountPostRequest request) {
        CustomerEntity foundCustomer = customerService.getCustomerById(request.getCustomerId());
        Optional<AccountEntity> foundEntity = accountRepository.getAccountEntityByCurrencyAndOwner(request.getCurrency(), foundCustomer);

        if (foundEntity.isPresent()) {
            throw new IllegalArgumentException(String.format("счёт в валюте %s уже существует", request.getCurrency()));
        }

        AccountEntity newAccount = accountMapper.accountRequestToAccountEntity(request, foundCustomer);
        return accountMapper.accountEntityToAccountPostResponse(updateAccountWithBalance(newAccount));
    }
    public AccountGetResponse getAccountInfo(int accountNumber) {
        AccountEntity foundAccount = getAccountById(accountNumber);

        return accountMapper.accountEntityToAccountGetResponse(foundAccount);
    }
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void topUpAccount(TopUpRequest request) {
        AccountEntity foundEntity = getAccountById(request.getAccountNumber());
        foundEntity.setBalance(foundEntity.getBalance().add(request.getAmount()));
        updateAccountWithBalance(foundEntity);
    }
    public AccountEntity getAccountById(int id) {
        var foundAccountEntity = accountRepository.getAccountEntityByAccountId(id);

        if (foundAccountEntity.isEmpty()) {
            throw new IllegalArgumentException("счёт не найден");
        }

        return foundAccountEntity.get();
    }
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public AccountEntity updateAccount(AccountEntity account) {
        return accountRepository.save(account);
    }
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public AccountEntity updateAccountWithBalance(AccountEntity accountEntity) {
        accountEntity = updateAccount(accountEntity);

        simpMessagingTemplate.convertAndSend(
                changeAccountBalanceDestination,
                accountMapper.accountEntityToAccountBalanceState(accountEntity)
        );

        return accountEntity;
    }
}

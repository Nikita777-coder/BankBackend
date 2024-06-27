package accounts.mappers;

import accounts.dto.account.*;
import accounts.entities.CustomerEntity;
import accounts.entities.account.AccountEntity;
import net.devh.boot.grpc.example.AccountService;
import net.devh.boot.grpc.example.WideUsedTypes;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountEntity accountRequestToAccountEntity(AccountPostRequest request, CustomerEntity owner);
    @Mapping(source = "balance", target = "amount")
    AccountGetResponse accountEntityToAccountGetResponse(AccountEntity accountEntity);
    TopUpRequest makeAdditionalChecksToTopUpRequest(TopUpRequest request);
    @Mapping(source = "accountId", target = "accountNumber")
    AccountPostResponse accountEntityToAccountPostResponse(AccountEntity entity);
    @Mapping(source = "accountId", target = "accountNumber")
    AccountBalanceState accountEntityToAccountBalanceState(AccountEntity entity);
    @AfterMapping
    default void setupBalance(AccountEntity entity, @MappingTarget AccountBalanceState accountBalanceState) {
        accountBalanceState.setBalance(accountBalanceState.getBalance().setScale(2, RoundingMode.HALF_EVEN));
    }
    default Map<Integer, AccountEntity> listAccountEntitiesToMapAccountEntities(List<AccountEntity> accountEntities) {
        Map<Integer, AccountEntity> accounts = new HashMap<>(accountEntities.size());

        for (AccountEntity accountEntity : accountEntities) {
            accounts.put(accountEntity.getAccountId(), accountEntity);
        }

        return accounts;
    }
    @BeforeMapping
    default void checkAmount(TopUpRequest request) {
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("сумма увеличения счёта должа быть > 0");
        }
    }
    @AfterMapping
    default void setupBalanceToNewAccount(AccountPostRequest request, CustomerEntity owner, @MappingTarget AccountEntity accountEntity) {
        accountEntity.setBalance(BigDecimal.ZERO);
    }
    default WideUsedTypes.BDecimal bigDecimalToBDecimal(BigDecimal balance) {
        return WideUsedTypes.BDecimal
                .newBuilder()
                .setUnscaledValue(balance.unscaledValue().toString())
                .setScale(balance.scale())
                .build();
    }
}

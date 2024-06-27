package accounts.services;

import accounts.dto.converter.ConverterJavaResponse;
import accounts.dto.transfer.TransferRequest;
import accounts.entities.account.AccountEntity;
import accounts.mappers.GRPCMapper;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.example.ConverterServiceOuterClass;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TransferService {
    private final AccountService accountService;
    private final GRPCMapper grpcMapper;
    private final GRPCService grpcService;
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void makeTransfer(TransferRequest request) {
        AccountEntity senderAccount = accountService.getAccountById(request.getSenderAccount());
        checkSenderBalance(senderAccount, request.getAmountInSenderCurrency());
        AccountEntity receiverAccount = accountService.getAccountById(request.getReceiverAccount());

        if (receiverAccount.getCurrency() == senderAccount.getCurrency()) {
            receiverAccount.setBalance(receiverAccount.getBalance().add(request.getAmountInSenderCurrency()));
            senderAccount.setBalance(senderAccount.getBalance().subtract(request.getAmountInSenderCurrency()));

            accountService.updateAccountWithBalance(senderAccount);
            accountService.updateAccountWithBalance(receiverAccount);

            return;
        }

        ConverterServiceOuterClass.ConvertRequest convertRequest = grpcMapper.dataToConvertRequest(
                senderAccount.getCurrency().getValue(),
                receiverAccount.getCurrency().getValue(),
                request.getAmountInSenderCurrency()
        );

        ConverterJavaResponse response = grpcService.convert(convertRequest);

        senderAccount.setBalance(senderAccount.getBalance().subtract(request.getAmountInSenderCurrency()));
        receiverAccount.setBalance(receiverAccount.getBalance().add(Objects.requireNonNull(response.getAmount())));

        accountService.updateAccountWithBalance(senderAccount);
        accountService.updateAccountWithBalance(receiverAccount);
    }
    private void checkSenderBalance(AccountEntity senderAccount, BigDecimal sendAmount) {
        if (senderAccount.getBalance().compareTo(sendAmount) < 0) {
            throw new IllegalArgumentException("недостаточно средств на счёте для перевода");
        }
    }
}

package accounts.mappers;

import accounts.dto.transfer.TransferRequest;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface TransferMapper {
    TransferRequest makeAdditionalChecksToTransferRequest(TransferRequest request);
    @BeforeMapping
    default void checkAmount(TransferRequest request) {
        if (request.getAmountInSenderCurrency().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("сумма перевода должа быть > 0");
        }
    }
}

package accounts.controllers;

import accounts.dto.transfer.TransferRequest;
import accounts.mappers.TransferMapper;
import accounts.services.TransferService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/transfers")
@RequiredArgsConstructor
public class TransferController {
    private final TransferMapper transferMapper;
    private final TransferService transferService;
    @PostMapping
    @ResponseStatus(code = HttpStatus.OK)
    public void makeTransfer(@Valid @RequestBody TransferRequest request) {
        transferService.makeTransfer(transferMapper.makeAdditionalChecksToTransferRequest(request));
    }
}

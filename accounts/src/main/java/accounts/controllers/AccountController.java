package accounts.controllers;

import accounts.dto.account.AccountGetResponse;
import accounts.dto.account.AccountPostRequest;
import accounts.dto.account.AccountPostResponse;
import accounts.dto.account.TopUpRequest;
import accounts.mappers.AccountMapper;
import accounts.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final AccountMapper accountMapper;
    @PostMapping
    public ResponseEntity<AccountPostResponse> createAccount(@Valid @RequestBody AccountPostRequest request) {
        return new ResponseEntity<>(accountService.createAccount(request), HttpStatus.OK);
    }
    @PostMapping("/{accountNumber}/top-up")
    @ResponseStatus(code = HttpStatus.OK)
    public void topUpAccount(@PathVariable int accountNumber,
                                             @Valid @RequestBody TopUpRequest request) {
        request.setAccountNumber(accountNumber);
        accountService.topUpAccount(accountMapper.makeAdditionalChecksToTopUpRequest(request));
    }
    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountGetResponse> getAccountInfo(@PathVariable int accountNumber) {
        return new ResponseEntity<>(accountService.getAccountInfo(accountNumber), HttpStatus.OK);
    }
}

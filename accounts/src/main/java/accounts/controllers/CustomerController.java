package accounts.controllers;

import accounts.dto.customer.*;
import accounts.mappers.CustomerMapper;
import accounts.myelements.RateLimiter;
import accounts.services.CustomerService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebExchangeBindException;

import javax.validation.Valid;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@RestController
@RequestMapping("/customers")
@Slf4j
public class CustomerController {
    private final CustomerService customerService;
    private final CustomerMapper customerMapper;
    private final RateLimiter rateLimiter;

    @PostMapping
    public ResponseEntity<CustomerPostResponse> createCustomer(@Valid @RequestBody CustomerPostRequest request) {
        log.info("Request reached server");
        return new ResponseEntity<>(customerService.createCustomer(customerMapper.makeAdditionalValidation(request)),
                HttpStatus.OK);
    }

    @GetMapping("{customerId}/balance")
    public ResponseEntity<BalanceResponse> getBalance(@PathVariable int customerId,
                                                      @RequestParam String currency) {
        Bucket customerBucket = rateLimiter.getCustomerBucket(customerId);

        if (customerBucket.tryConsume(1)) {
            return new ResponseEntity<>(customerService.getBalance(customerMapper.createBalanceRequest(customerId, currency)), HttpStatus.OK);
        }

        rateLimiter.deleteCustomerBucket(customerId);
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }
    @GetMapping("{customerId}/accounts")
    @ResponseStatus(HttpStatus.OK)
    public List<CustomerAccount> getAllCustomerAccounts(@PathVariable int customerId) {
        return customerService.getAllCustomerAccounts(customerId);
    }

    @DeleteMapping("{customerId}/accounts")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAccounts(@PathVariable int customerId,
                               @Valid @RequestBody List<AccountId> accountIds) {
        customerService.deleteAccounts(customerId, accountIds);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(WebExchangeBindException.class)
    private Map<String, String> handleValidationExceptions(
            WebExchangeBindException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}

package accounts.advices;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ControllerAdvices {
    @ExceptionHandler({IllegalArgumentException.class, NullPointerException.class})
    private @ResponseBody ResponseEntity<String> handleBAD_REQUEST(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({IllegalStateException.class, CallNotPermittedException.class})
    private @ResponseBody ResponseEntity<String> handleINTERNAL_SERVER_ERROR(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

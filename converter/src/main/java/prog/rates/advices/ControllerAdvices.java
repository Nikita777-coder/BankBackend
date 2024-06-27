package prog.rates.advices;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import prog.rates.dto.ErrorMessage;

@ControllerAdvice
public class ControllerAdvices {
    @ExceptionHandler({IllegalArgumentException.class, NullPointerException.class})
    private @ResponseBody ResponseEntity<ErrorMessage> handleBAD_REQUEST(RuntimeException ex) {
        return new ResponseEntity<>(new ErrorMessage(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ArithmeticException.class, IllegalStateException.class})
    private @ResponseBody ResponseEntity<ErrorMessage> handleINTERNAL_SERVER_ERROR(RuntimeException ex) {
        return new ResponseEntity<>(new ErrorMessage(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

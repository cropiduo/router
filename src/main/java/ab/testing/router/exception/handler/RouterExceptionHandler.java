package ab.testing.router.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class RouterExceptionHandler {

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Empty or no parameter given")
    @ExceptionHandler(IllegalArgumentException.class)
    public void handleNotFound() {
        // do nothing
    }
}

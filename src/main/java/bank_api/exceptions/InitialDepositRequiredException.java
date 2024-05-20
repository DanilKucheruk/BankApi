package bank_api.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InitialDepositRequiredException extends RuntimeException {
    public InitialDepositRequiredException(String message) {
        super(message);
    }
}
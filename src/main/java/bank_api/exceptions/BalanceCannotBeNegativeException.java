package bank_api.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BalanceCannotBeNegativeException extends RuntimeException {
    public BalanceCannotBeNegativeException(String message) {
        super(message);
    }
}
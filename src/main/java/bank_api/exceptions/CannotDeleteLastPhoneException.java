package bank_api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CannotDeleteLastPhoneException extends RuntimeException {

    public CannotDeleteLastPhoneException(String message) {
        super(message);
    }
}
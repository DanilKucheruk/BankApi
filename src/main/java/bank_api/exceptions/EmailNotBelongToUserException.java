package bank_api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class EmailNotBelongToUserException extends RuntimeException {

    public EmailNotBelongToUserException(String message) {
        super(message);
    }
}
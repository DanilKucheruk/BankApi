package bank_api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class EmailAlredyExistsException extends RuntimeException{
    public EmailAlredyExistsException(String message) {
        super(message);
    }
}

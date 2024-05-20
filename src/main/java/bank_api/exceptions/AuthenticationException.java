package bank_api.exceptions;

import lombok.Data;

import java.util.Date;

@Data
public class AuthenticationException {
    private int status;
    private String message;
    private Date timestamp;

    public AuthenticationException(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = new Date();
    }
}

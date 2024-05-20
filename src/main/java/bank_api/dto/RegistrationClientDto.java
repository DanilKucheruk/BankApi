package bank_api.dto;

import java.time.LocalDate;
import java.util.List;

import bank_api.entity.Email;
import bank_api.entity.Phone;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class RegistrationClientDto {
    @NotEmpty(message = "Full name is required")
    private String fullName;

    @NotEmpty(message = "Login is required")
    private String login;

    private LocalDate birthDate;

    @Valid
    @Size(min = 1, message = "At least one phone number is required")
    private List<Phone> phones;

    @Valid
    @Size(min = 1, message = "At least one email is required")
    private List<Email> emails;

    @NotEmpty(message = "Password is required")
    private String password;

    @NotEmpty
    private double initialDeposit;
}
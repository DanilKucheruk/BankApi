package bank_api.dto;

import java.time.LocalDate;
import java.util.List;

import bank_api.model.Email;
import bank_api.model.Phone;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDto {
    private Long id;

    @NotEmpty(message = "Full name is required")
    private String fullName;

    @NotEmpty(message = "Login is required")
    private String login;

    @NotNull(message = "Birth date is required")
    private LocalDate birthDate;

    @Valid
    @Size(min = 1, message = "At least one phone number is required")
    private List<Phone> phones;

    @Valid
    @Size(min = 1, message = "At least one email is required")
    private List<Email> emails;

    @NotEmpty(message = "Password is required")
    private String password;

    @Valid
    private AccountDto account;
}

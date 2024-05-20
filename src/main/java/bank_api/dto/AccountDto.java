package bank_api.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {
    private Long id;

    private BigDecimal balance;

    @NotNull(message = "Insert initial deposit")
    private BigDecimal initialDeposit;
}
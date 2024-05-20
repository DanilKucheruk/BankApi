package bank_api.dto;

import lombok.Data;

@Data
public class EmailDto {
    private Long id;
    private String emailAddress;
    private Long clientId;
}
package bank_api.dto;

import lombok.Data;

@Data
public class PhoneDto {
    private Long id;
    private String phoneNumber;
    private Long clientId;
}
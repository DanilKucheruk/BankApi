package bank_api.mapper;

import org.springframework.stereotype.Component;

import bank_api.dto.EmailDto;
import bank_api.entity.Email;

@Component
public class EmailMapper implements Mapper<Email, EmailDto> {

    @Override
    public EmailDto map(Email email) {
        if (email == null) {
            return null;
        }

        EmailDto emailDto = new EmailDto();
        emailDto.setId(email.getId());
        emailDto.setEmailAddress(email.getEmailAddress());
        emailDto.setClientId(email.getClient() != null ? email.getClient().getId() : null);

        return emailDto;
    }
}

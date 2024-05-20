package bank_api.service;

import bank_api.dto.EmailDto;
import bank_api.entity.Email;
import jakarta.transaction.Transactional;

public interface EmailService {
    EmailDto getEmailById(Long id);

    @Transactional
    Email addEmail(EmailDto emailDto);

    void deleteEmail(Long clientId, Long emailId);
}

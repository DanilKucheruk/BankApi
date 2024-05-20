package bank_api.service.impl;

import org.springframework.stereotype.Service;

import bank_api.dto.EmailDto;
import bank_api.entity.Client;
import bank_api.entity.Email;
import bank_api.exceptions.CannotDeleteLastPhoneException;
import bank_api.exceptions.ClientNotFoundException;
import bank_api.exceptions.EmailAlredyExistsException;
import bank_api.exceptions.EmailNotBelongToUserException;
import bank_api.exceptions.EmailNotFoundException;
import bank_api.mapper.EmailMapper;
import bank_api.repository.ClientRepository;
import bank_api.repository.EmailRepository;
import bank_api.service.EmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);
    private final ClientRepository clientRepository;
    private final EmailRepository emailRepository;
    private final EmailMapper emailMapper;

    @Override
    public EmailDto getEmailById(Long id) {
        LOGGER.debug("Request to get email by id: {}", id);
        return emailRepository.findById(id).map(emailMapper::map).orElseThrow();
    }

    @Override
    public Email addEmail(EmailDto emailDto) {
        LOGGER.debug("Request to add email: {}", emailDto);
        boolean emailAddressAlreadyExists = emailRepository.findByEmailAddress(emailDto.getEmailAddress()).isPresent();
        Optional<Client> client = clientRepository.findById(emailDto.getClientId());
        if (emailAddressAlreadyExists) {
            LOGGER.warn("Email address already exists: {}", emailDto.getEmailAddress());
            throw new EmailAlredyExistsException("Такой email уже сущетвует");
        }
        if (client.isPresent()) {
            Email email = new Email();
            email.setEmailAddress(emailDto.getEmailAddress());
            email.setClient(client.get());
            return emailRepository.save(email);
        } else {
            LOGGER.warn("Client not found by id: {}", emailDto.getClientId());
            throw new ClientNotFoundException("Клиент не найден");
        }
    }

    @Override
    @Transactional
    public void deleteEmail(Long clientId, Long emailId) {
        LOGGER.debug("Request to delete email by client id: {} and email id: {}", clientId, emailId);
        Email email = emailRepository.findById(emailId)
                .orElseThrow(() -> new EmailNotFoundException("Email не найден"));
        if (!email.getClient().getId().equals(clientId)) {
            LOGGER.warn("Email does not belong to user: {}", clientId);
            throw new EmailNotBelongToUserException("Email не принадлежит пользователю");
        }
        if (email.getClient().getPhones().size() == 1) {
            LOGGER.warn("Cannot delete last email for user: {}", clientId);
            throw new CannotDeleteLastPhoneException("Не удается удалить последний email");
        }
        email.getClient().getEmails().remove(email);
        emailRepository.delete(email);
        emailRepository.flush();
    }
}

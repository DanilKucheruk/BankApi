package bank_api.service.impl;

import org.springframework.stereotype.Service;

import bank_api.dto.PhoneDto;
import bank_api.entity.Client;
import bank_api.entity.Phone;
import bank_api.exceptions.CannotDeleteLastPhoneException;
import bank_api.exceptions.ClientNotFoundException;
import bank_api.exceptions.PhoneAlredyExistsException;
import bank_api.exceptions.PhoneNotBelongToUserException;
import bank_api.exceptions.PhoneNotFoundException;
import bank_api.mapper.PhoneMapper;
import bank_api.repository.ClientRepository;
import bank_api.repository.PhoneRepository;
import bank_api.service.PhoneService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PhoneServiceImpl implements PhoneService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PhoneServiceImpl.class);
    private final PhoneRepository phoneRepository;
    private final ClientRepository clientRepository;
    private final PhoneMapper phoneMapper;

    public PhoneDto getPhoneById(Long id) {
        LOGGER.debug("Getting phone with id: {}", id);
        return phoneRepository.findById(id).map(phoneMapper::map)
                .orElseThrow(() -> new PhoneNotFoundException("Телефон не найден"));
    }

    @Transactional
    public Phone addPhone(PhoneDto phoneDto) {
        LOGGER.debug("Adding new phone: {}", phoneDto);
        boolean phoneNumberAlreadyExists = phoneRepository.findByPhoneNumber(phoneDto.getPhoneNumber()).isPresent();
        Optional<Client> client = clientRepository.findById(phoneDto.getClientId());
        if (phoneNumberAlreadyExists) {
            LOGGER.error("Phone with number {} already exists", phoneDto.getPhoneNumber());
            throw new PhoneAlredyExistsException("Такой телефон уже сущетвует");
        }
        if (client.isPresent()) {
            Phone phone = new Phone();
            phone.setPhoneNumber(phoneDto.getPhoneNumber());
            phone.setClient(client.get());
            LOGGER.debug("Phone saved: {}", phone);
            return phoneRepository.save(phone);
        } else {
            LOGGER.error("Client not found with id: {}", phoneDto.getClientId());
            throw new ClientNotFoundException("Клиент не найден");
        }
    }

    @Transactional
    public void deletePhone(Long clientId, Long phoneId) {
        LOGGER.debug("Deleting phone with id: {} for client: {}", phoneId, clientId);
        Phone phone = phoneRepository.findById(phoneId)
                .orElseThrow(() -> new PhoneNotFoundException("Телефон не найден"));
        if (!phone.getClient().getId().equals(clientId)) {
            LOGGER.error("Phone with id {} does not belong to client with id {}", phoneId, clientId);
            throw new PhoneNotBelongToUserException("Телефон не принадлежит пользователю");
        }
        if (phone.getClient().getPhones().size() == 1) {
            LOGGER.error("Cannot delete last phone number for client with id {}", clientId);
            throw new CannotDeleteLastPhoneException("Не удается удалить последний телефонный номер");
        }
        phone.getClient().getPhones().remove(phone);
        phoneRepository.deleteById(phoneId);
        phoneRepository.flush();
        LOGGER.debug("Phone with id: {} deleted", phoneId);
    }

}

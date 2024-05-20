package bank_api.service;

import bank_api.dto.PhoneDto;
import bank_api.entity.Phone;
import bank_api.exceptions.CannotDeleteLastPhoneException;
import bank_api.exceptions.ClientNotFoundException;
import bank_api.exceptions.PhoneAlredyExistsException;
import bank_api.exceptions.PhoneNotBelongToUserException;
import bank_api.exceptions.PhoneNotFoundException;
import jakarta.transaction.Transactional;

public interface PhoneService {
    PhoneDto getPhoneById(Long id) throws PhoneNotFoundException;

    @Transactional
    Phone addPhone(PhoneDto phoneDto) throws PhoneAlredyExistsException, ClientNotFoundException;

    void deletePhone(Long clientId, Long phoneId)
            throws PhoneNotFoundException, PhoneNotBelongToUserException, CannotDeleteLastPhoneException;
}

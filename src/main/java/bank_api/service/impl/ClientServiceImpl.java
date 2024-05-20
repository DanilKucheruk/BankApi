package bank_api.service.impl;

import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Collections;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bank_api.dto.ClientDto;
import bank_api.dto.RegistrationClientDto;
import bank_api.entity.Client;
import bank_api.entity.Email;
import bank_api.entity.Phone;
import bank_api.mapper.ClientMapper;
import bank_api.mapper.RegistrationClientMapper;
import bank_api.repository.ClientRepository;
import bank_api.service.ClientService;
import bank_api.specification.ClientSpecifications;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientServiceImpl.class);
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final RegistrationClientMapper registrationClientMapper;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        LOGGER.debug("Request to load user by username: {}", login);
        return clientRepository.findByLogin(login)
                .map(client -> new org.springframework.security.core.userdetails.User(
                        client.getLogin(),
                        client.getPassword(),
                        Collections.emptyList()))
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Failed to retrieve user: " + login));
    }

    @Override
    @Transactional
    public Client create(RegistrationClientDto clientDto) {
        LOGGER.debug("Request to create client: {}", clientDto);
        return Optional.of(clientDto)
                .map(dto -> {
                    Client client = registrationClientMapper.map(dto);
                    List<Email> emails = new ArrayList<>();
                    for (Email email : dto.getEmails()) {
                        email.setClient(client);
                        email.setEmailAddress(email.getEmailAddress());
                        emails.add(email);
                    }
                    client.setEmails(emails);
                    List<Phone> phones = new ArrayList<>();
                    for (Phone phone : dto.getPhones()) {
                        phone.setClient(client);
                        phone.setPhoneNumber(phone.getPhoneNumber());
                        phones.add(phone);
                    }
                    client.setPhones(phones);
                    return client;
                })
                .map(clientRepository::save)
                .orElseThrow();
    }

    @Override
    public List<ClientDto> findAll() {
        LOGGER.debug("Request to find all clients");
        return clientRepository.findAll()
                .stream()
                .map(clientMapper::map)
                .toList();
    }

    @Override
    public Optional<ClientDto> findById(Long id) {
        LOGGER.debug("Request to find client by id: {}", id);
        return clientRepository.findById(id).map(clientMapper::map);
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        LOGGER.debug("Request to delete client by id: {}", id);
        return clientRepository.findById(id)
                .map(entity -> {
                    if (entity != null) {
                        clientRepository.delete(entity);
                        clientRepository.flush();
                        return true;
                    } else {
                        return false;
                    }
                })
                .orElse(false);
    }

    @Override
    public Optional<Client> findByLogin(String login) {
        LOGGER.debug("Request to find client by login: {}", login);
        return clientRepository.findByLogin(login);
    }

    @Override
    public Page<ClientDto> searchClients(String firstName, String lastName , LocalDate birthDate,
            String phone, String email, int page, int size, String sort) {
        LOGGER.debug(
                "Request to search clients with parameters: firstName={}, lastName={}, patronymic={}, birthDate={}, phone={}, email={}, page={}, size={}, sort={}",
                firstName, lastName, birthDate, phone, email, page, size, sort);

        Specification<Client> spec = Specification.where(null);

        if (firstName != null) {
            String fullName = firstName + " " + lastName;
            spec = spec.and(ClientSpecifications.likeFullName(fullName));
        }
        if (birthDate != null) {
            spec = spec.and(ClientSpecifications.greaterThanBirthDate(birthDate));
        }
        if (phone != null) {
            spec = spec.and(ClientSpecifications.equalPhone(phone));
        }
        if (email != null) {
            spec = spec.and(ClientSpecifications.equalEmail(email));
        }

        PageRequest pageRequest = PageRequest.of(page, size,  Sort.by(Sort.Order.asc(sort)));

        Page<Client> clients = clientRepository.findAll(spec, pageRequest);

        return clients.map(clientMapper::map);
    }
}

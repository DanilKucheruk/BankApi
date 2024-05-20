package bank_api.service;

import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;

import bank_api.dto.ClientDto;
import bank_api.dto.RegistrationClientDto;
import bank_api.entity.Client;

import java.util.Optional;
import java.util.List;

import java.time.LocalDate;

public interface ClientService extends UserDetailsService {
    // for authentication controller
    Client create(RegistrationClientDto user);

    Optional<ClientDto> findById(Long id);

    Optional<Client> findByLogin(String login);

    List<ClientDto> findAll();

    boolean delete(Long id);

    Page<ClientDto> searchClients(String firstName, String lastName , LocalDate birthDate,
            String phone, String email, int page, int size, String sort);
}
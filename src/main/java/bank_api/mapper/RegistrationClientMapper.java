package bank_api.mapper;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import bank_api.dto.RegistrationClientDto;
import bank_api.entity.Account;
import bank_api.entity.Client;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.math.BigDecimal;


@Component
@RequiredArgsConstructor
public class RegistrationClientMapper implements Mapper<RegistrationClientDto, Client>{
    private final BCryptPasswordEncoder passwordEncoder;
    @Override
    public Client map(RegistrationClientDto object) {
        Client client = new Client();
        copy(object,client);
        return client;
    }

    public void copy(RegistrationClientDto regClientDto, Client client){
        client.setFullName(regClientDto.getFullName());
        client.setLogin(regClientDto.getLogin());
        client.setBirthDate(regClientDto.getBirthDate());
        client.setPhones(regClientDto.getPhones());
        client.setEmails(regClientDto.getEmails());
        Optional.ofNullable(regClientDto.getPassword()).filter(StringUtils::hasText)
        .map(passwordEncoder::encode).ifPresent(client::setPassword);
        Account account = new Account(BigDecimal.valueOf(regClientDto.getInitialDeposit()));
        client.setAccount(account);
    }

    
}

package bank_api.mapper;

import org.springframework.stereotype.Component;

import bank_api.dto.ClientDto;
import bank_api.dto.EmailDto;
import bank_api.dto.PhoneDto;
import bank_api.entity.Client;
import lombok.RequiredArgsConstructor;

import java.util.stream.Collectors;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ClientMapper implements Mapper<Client, ClientDto> {
    private final AccountMapper accountMapper;
    private final PhoneMapper phoneMapper;
    private final EmailMapper emailMapper;

    @Override
    public ClientDto map(Client object) {
        ClientDto clientDto = new ClientDto();
        clientDto.setId(object.getId());
        clientDto.setFullName(object.getFullName());
        clientDto.setLogin(object.getLogin());
        clientDto.setBirthDate(object.getBirthDate());
        clientDto.setPhones(getPhonesDto(object));
        clientDto.setEmails(getEmailsDto(object));
        clientDto.setPassword(object.getPassword());
        clientDto.setAccount(accountMapper.map(object.getAccount()));
        return clientDto;
    }

    private List<EmailDto> getEmailsDto(Client client) {
        return client.getEmails().stream().map(emailMapper::map).collect(Collectors.toList());
    }

    private List<PhoneDto> getPhonesDto(Client client) {
        return client.getPhones().stream().map(phoneMapper::map).collect(Collectors.toList());
    }

}

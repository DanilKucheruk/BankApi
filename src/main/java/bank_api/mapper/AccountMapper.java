package bank_api.mapper;

import org.springframework.stereotype.Component;

import bank_api.dto.AccountDto;
import bank_api.entity.Account;

@Component
public class AccountMapper implements Mapper<Account, AccountDto> {

    @Override
    public AccountDto map(Account object) {
        AccountDto accountDto = new AccountDto();
        accountDto.setId(object.getId());
        accountDto.setBalance(object.getBalance());
        accountDto.setInitialDeposit(object.getInitialDeposit());
        return accountDto;
    }
}

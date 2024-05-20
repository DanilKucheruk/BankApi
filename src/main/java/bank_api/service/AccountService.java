package bank_api.service;

import bank_api.dto.AccountDto;
import bank_api.entity.Account;
import java.math.BigDecimal;

import java.util.List;

public interface AccountService {
    public Account getAccountById(Long id);
    public AccountDto deposit(Long id, BigDecimal amount);
    public void updateBalances();
    public AccountDto transfer(Long fromId, Long toId, BigDecimal amount);
    public List<AccountDto> findAll();
}

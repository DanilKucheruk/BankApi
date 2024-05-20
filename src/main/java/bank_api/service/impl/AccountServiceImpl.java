package bank_api.service.impl;

import bank_api.dto.AccountDto;
import bank_api.entity.Account;
import bank_api.exceptions.BalanceCannotBeNegativeException;
import bank_api.exceptions.ClientNotFoundException;
import bank_api.mapper.AccountMapper;
import bank_api.repository.AccontRepository;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import bank_api.service.AccountService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);
    private final AccountMapper accountMapper;
    private final AccontRepository accountRepository;

    @Override
    public List<AccountDto> findAll() {
        LOGGER.debug("Request to get all accounts");
        List<Account> accounts = accountRepository.findAll();
        LOGGER.debug("Found {} accounts", accounts.size());
        return accounts.stream()
                .map(accountMapper::map)
                .toList();
    }

    @Override
    public Account getAccountById(Long id) {
        LOGGER.debug("Request to get account by id: ", id);
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Клиент не найден"));
        LOGGER.debug("Found account: ", account);
        return account;
    }

    @Override
    public AccountDto deposit(Long id, BigDecimal amount) {
        LOGGER.debug("Request to deposit to account: {} with amount: {}", id, amount);
        Account account = getAccountById(id);
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            LOGGER.warn("Attempt to deposit non-positive amount: {}", amount);
            throw new IllegalArgumentException("Сумма должна быть положительной");
        }
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
        LOGGER.debug("Deposited: amount: {} to account: {}", amount, account);
        return accountMapper.map(account);
    }

    @Override
    @Transactional
    public AccountDto transfer(Long fromId, Long toId, BigDecimal amount) {
        LOGGER.debug("Request to transfer from account: {} to account: {} with amount: {}", fromId, toId, amount);
        Account fromAccount = null;
        Account toAccount = null;
        synchronized (accountRepository) {
            fromAccount = getAccountById(fromId);
            toAccount = getAccountById(toId);
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Сумма должна быть положительной");
        }
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new BalanceCannotBeNegativeException("Недостаточно средств на счету");
        }
        synchronized (fromAccount) {
            synchronized (toAccount) {
                fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
                toAccount.setBalance(toAccount.getBalance().add(amount));
                accountRepository.saveAll(List.of(fromAccount, toAccount));
            }
        }
        LOGGER.debug("Transferred: amount: {} from account: {} to account: {}", amount, fromAccount, toAccount);
        return accountMapper.map(fromAccount);
    }

    @PostConstruct
    @Scheduled(fixedRate = 60000)
    public void updateBalances() {
        LOGGER.debug("Request to update account balances");
        List<Account> accounts = accountRepository.findAll();
        for (Account account : accounts) {
            BigDecimal newBalance = account.getBalance()
                    .compareTo(BigDecimal.valueOf(2.07).multiply(account.getInitialDeposit())) < 0
                            ? account.getBalance().multiply(BigDecimal.valueOf(1.05))
                            : account.getBalance();
            if (newBalance.compareTo(account.getBalance()) > 0) {
                account.setBalance(newBalance);
                accountRepository.save(account);
            }
        }
        LOGGER.debug("Updated account balances");
    }
}
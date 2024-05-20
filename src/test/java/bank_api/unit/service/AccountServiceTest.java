package bank_api.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import bank_api.dto.AccountDto;
import bank_api.entity.Account;
import bank_api.exceptions.BalanceCannotBeNegativeException;
import bank_api.mapper.AccountMapper;
import bank_api.repository.AccontRepository;
import bank_api.service.impl.AccountServiceImpl;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private AccontRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @Test
    void testTransferPositiveAmountWhenValidTransferIsMade() {
        Account account1 = new Account();
        account1.setId(1L);
        account1.setBalance(BigDecimal.valueOf(500));

        Account account2 = new Account();
        account2.setId(2L);
        account2.setBalance(BigDecimal.valueOf(300));

        AccountDto accountDto = new AccountDto();
        accountDto.setId(1L);
        accountDto.setBalance(BigDecimal.valueOf(400));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account1));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(account2));
        when(accountMapper.map(account1)).thenReturn(accountDto);

        AccountDto result = accountService.transfer(1L, 2L, BigDecimal.valueOf(100));

        assertEquals(accountDto, result);
        assertEquals(BigDecimal.valueOf(400), account1.getBalance());
        assertEquals(BigDecimal.valueOf(400), account2.getBalance());
    }

    @Test
    void testTransferZeroAmount() {
        Account account1 = new Account();
        account1.setId(1L);
        account1.setBalance(BigDecimal.valueOf(500));

        Account account2 = new Account();
        account2.setId(2L);
        account2.setBalance(BigDecimal.valueOf(300));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account1));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(account2));

        assertThrows(IllegalArgumentException.class, () -> accountService.transfer(1L, 2L, BigDecimal.ZERO));
    }

    @Test
    void testTransferNegativeAmount() {
        Account account1 = new Account();
        account1.setId(1L);
        account1.setBalance(BigDecimal.valueOf(500));

        Account account2 = new Account();
        account2.setId(2L);
        account2.setBalance(BigDecimal.valueOf(300));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account1));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(account2));

        assertThrows(IllegalArgumentException.class, () -> accountService.transfer(1L, 2L, BigDecimal.valueOf(-100)));
    }

    @Test
    void testTransferAmountEqualToBalance() {
        Account account1 = new Account();
        account1.setId(1L);
        account1.setBalance(BigDecimal.valueOf(100));

        Account account2 = new Account();
        account2.setId(2L);
        account2.setBalance(BigDecimal.valueOf(200));

        AccountDto accountDto = new AccountDto();
        accountDto.setId(1L);
        accountDto.setBalance(BigDecimal.valueOf(0));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account1));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(account2));
        when(accountMapper.map(account1)).thenReturn(accountDto);

        AccountDto result = accountService.transfer(1L, 2L, BigDecimal.valueOf(100));

        assertEquals(accountDto, result);
        assertEquals(BigDecimal.ZERO, account1.getBalance());
        assertEquals(BigDecimal.valueOf(300), account2.getBalance());
    }

    @Test
    void testTransferAmountLessThanBalance() {
        Account account1 = new Account();
        account1.setId(1L);
        account1.setBalance(BigDecimal.valueOf(500));

        Account account2 = new Account();
        account2.setId(2L);
        account2.setBalance(BigDecimal.valueOf(300));

        AccountDto accountDto = new AccountDto();
        accountDto.setId(1L);
        accountDto.setBalance(BigDecimal.valueOf(450));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account1));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(account2));
        when(accountMapper.map(account1)).thenReturn(accountDto);

        AccountDto result = accountService.transfer(1L, 2L, BigDecimal.valueOf(50));

        assertEquals(accountDto, result);
        assertEquals(BigDecimal.valueOf(450), account1.getBalance());
        assertEquals(BigDecimal.valueOf(350), account2.getBalance());
    }

    @Test
    void testTransferAmountGreaterThanBalance() {
        Account account1 = new Account();
        account1.setId(1L);
        account1.setBalance(BigDecimal.valueOf(100));

        Account account2 = new Account();
        account2.setId(2L);
        account2.setBalance(BigDecimal.valueOf(200));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account1));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(account2));

        assertThrows(BalanceCannotBeNegativeException.class,
                () -> accountService.transfer(1L, 2L, BigDecimal.valueOf(101)));
    }

    @Test
    void testTransferNegativeAmountThrowsException() {
        Account account1 = new Account();
        account1.setId(1L);
        account1.setBalance(BigDecimal.valueOf(500));

        Account account2 = new Account();
        account2.setId(2L);
        account2.setBalance(BigDecimal.valueOf(300));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account1));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(account2));

        assertThrows(IllegalArgumentException.class, () -> accountService.transfer(1L, 2L, BigDecimal.valueOf(-50)));
    }

    @Test
    void testTransferAmountLessThanZeroThrowsException() {
        Account account1 = new Account();
        account1.setId(1L);
        account1.setBalance(BigDecimal.valueOf(500));

        Account account2 = new Account();
        account2.setId(2L);
        account2.setBalance(BigDecimal.valueOf(300));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account1));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(account2));

        assertThrows(IllegalArgumentException.class, () -> accountService.transfer(1L, 2L, BigDecimal.valueOf(-100)));
    }

    @Test
    void testTransferAmountGreaterThanBalanceThrowsException() {
        Account account1 = new Account();
        account1.setId(1L);
        account1.setBalance(BigDecimal.valueOf(100));

        Account account2 = new Account();
        account2.setId(2L);
        account2.setBalance(BigDecimal.valueOf(200));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account1));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(account2));

        assertThrows(BalanceCannotBeNegativeException.class,
                () -> accountService.transfer(1L, 2L, BigDecimal.valueOf(101)));
    }

}

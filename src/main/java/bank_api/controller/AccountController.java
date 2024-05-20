package bank_api.controller;

import bank_api.dto.AccountDto;
import bank_api.entity.Account;
import bank_api.exceptions.BalanceCannotBeNegativeException;
import bank_api.exceptions.ClientNotFoundException;
import bank_api.service.AccountService;
import bank_api.service.impl.PhoneServiceImpl;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PhoneServiceImpl.class);
    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<List<AccountDto>> getAllAccounts() {
        LOGGER.info("GET /api/accounts - Retrieving all accounts");
        return ResponseEntity.ok(accountService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long id) {
        LOGGER.info("GET /api/accounts/{} - Retrieving account with id {}", id, id);
        try {
            Account account = accountService.getAccountById(id);
            return ResponseEntity.ok(account);
        } catch (ClientNotFoundException e) {
            LOGGER.error("GET /api/accounts/{} - Account not found", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<AccountDto> deposit(@PathVariable Long id, @RequestParam BigDecimal amount) {
        LOGGER.info("POST /api/accounts/{}/deposit - Depositing {} into account with id {}", id, amount, id);
        try {
            AccountDto account = accountService.deposit(id, amount);
            return ResponseEntity.ok(account);
        } catch (IllegalArgumentException e) {
            LOGGER.error("POST /api/accounts/{}/deposit - Invalid deposit amount", id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<AccountDto> transfer(@RequestParam Long fromId, @RequestParam Long toId,
            @RequestParam BigDecimal amount) {
        LOGGER.info("POST /api/accounts/transfer - Transferring {} from account with id {} to account with id {}",
                amount, fromId, toId);
        try {
            AccountDto account = accountService.transfer(fromId, toId, amount);
            return ResponseEntity.ok(account);
        } catch (IllegalArgumentException | BalanceCannotBeNegativeException e) {
            LOGGER.error("POST /api/accounts/transfer - Invalid transfer request");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (ClientNotFoundException e) {
            LOGGER.error("POST /api/accounts/transfer - Account not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
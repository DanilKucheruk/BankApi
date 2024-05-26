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
import org.springframework.web.bind.annotation.ExceptionHandler;
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
    public ResponseEntity<Account> getAccountById(@PathVariable Long id) throws ClientNotFoundException{
        LOGGER.info("GET /api/accounts/{} - Retrieving account with id {}", id, id);
        Account account = accountService.getAccountById(id);
        return ResponseEntity.ok(account);
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<AccountDto> deposit(@PathVariable Long id, @RequestParam BigDecimal amount) throws IllegalArgumentException {
        LOGGER.info("POST /api/accounts/{}/deposit - Depositing {} into account with id {}", id, amount, id);
        AccountDto account = accountService.deposit(id, amount);
        return ResponseEntity.ok(account);
    }

    @PostMapping("/transfer")
    public ResponseEntity<AccountDto> transfer(@RequestParam Long fromId, @RequestParam Long toId,
            @RequestParam BigDecimal amount) throws IllegalArgumentException, BalanceCannotBeNegativeException, ClientNotFoundException {
        LOGGER.info("POST /api/accounts/transfer - Transferring {} from account with id {} to account with id {}",
                amount, fromId, toId);
        AccountDto account = accountService.transfer(fromId, toId, amount);
        return ResponseEntity.ok(account);
    }

    @ExceptionHandler({IllegalArgumentException.class, BalanceCannotBeNegativeException.class})
    public ResponseEntity<String> handleBadRequest(Exception e) {
        LOGGER.error("Invalid request", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request");
    }

    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<String> handleNotFound(Exception e) {
        LOGGER.error("Account not found", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
    }
}
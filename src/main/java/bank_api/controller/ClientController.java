package bank_api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bank_api.dto.ClientDto;
import bank_api.dto.EmailDto;
import bank_api.dto.PhoneDto;
import bank_api.exceptions.ClientNotFoundException;
import bank_api.service.ClientService;
import bank_api.service.EmailService;
import bank_api.service.PhoneService;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;
    private final EmailService emailService;
    private final PhoneService phoneService;
    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

    @GetMapping("/search")
    public Page<ClientDto> searchClients(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) LocalDate birthDate,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort
    ) {
        return clientService.searchClients(firstName, lastName, birthDate, phone, email, page, size, sort);
    }

    @GetMapping
    public ResponseEntity<List<ClientDto>> searchClients() {
        return ResponseEntity.ok(clientService.findAll());
    }

    @PostMapping("/{clientId}/phones")
    public ResponseEntity<?> addPhone(@PathVariable Long clientId, @RequestBody PhoneDto phoneDto) {
        logger.info("POST /api/clients/{}/phones - Adding phone for client with id {}", clientId, clientId);
        phoneDto.setClientId(clientId);
        phoneService.addPhone(phoneDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{clientId}/phones/{phoneId}")
    public ResponseEntity<?> deletePhone(@PathVariable Long clientId, @PathVariable Long phoneId) {
        logger.info("DELETE /api/clients/{}/phones/{} - Deleting phone with id {} for client with id {}", clientId,
                phoneId, phoneId, clientId);
        phoneService.deletePhone(clientId, phoneId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{clientId}/emails")
    public ResponseEntity<?> addEmail(@PathVariable Long clientId, @RequestBody EmailDto emailDto) {
        logger.info("POST /api/clients/{}/emails - Adding email for client with id {}", clientId, clientId);
        emailDto.setClientId(clientId);
        emailService.addEmail(emailDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{clientId}/emails/{emailId}")
    public ResponseEntity<?> deleteEmail(@PathVariable Long clientId, @PathVariable Long emailId) {
        logger.info("DELETE /api/clients/{}/emails/{} - Deleting email with id {} for client with id {}", clientId,
                emailId, emailId, clientId);
        emailService.deleteEmail(clientId, emailId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClient(@PathVariable Long id) throws ClientNotFoundException {
        logger.info("DELETE /api/clients/{} - Deleting client with id {}", id, id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<String> handlerClientNotFound(Exception e){
        logger.error("Account not found", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
    }

}

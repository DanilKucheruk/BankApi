package bank_api.controller.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bank_api.dto.AuthenticationRequest;
import bank_api.dto.RegistrationClientDto;
import bank_api.service.AuthenticationService;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authService;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @PostMapping("/auth")
    public ResponseEntity<?> createAuthToken(@RequestBody AuthenticationRequest authRequest) {
        logger.info("POST /api/auth - Creating authentication token");
        return authService.createAuthToken(authRequest);
    }

    @PostMapping("/registration")
    public ResponseEntity<?> createNewUser(@RequestBody RegistrationClientDto clientDto) {
        logger.info("POST /api/registration - Creating new user");
        return authService.createNewUser(clientDto);
    }

}
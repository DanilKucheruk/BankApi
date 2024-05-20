package bank_api.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import bank_api.dto.AuthenticationRequest;
import bank_api.dto.AuthenticationResponse;
import bank_api.dto.RegistrationClientDto;
import bank_api.exceptions.AuthenticationException;
import bank_api.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final ClientService clientService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<?> createAuthToken(@RequestBody AuthenticationRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getLogin(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(
                    new AuthenticationException(HttpStatus.UNAUTHORIZED.value(), "Неправильный логин или пароль"),
                    HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = clientService.loadUserByUsername(authRequest.getLogin());
        String token = jwtTokenUtils.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    public ResponseEntity<?> createNewUser(@RequestBody RegistrationClientDto clientDto) {
        if (clientService.findByLogin(clientDto.getLogin()).isPresent()) {
            return new ResponseEntity<>(new AuthenticationException(HttpStatus.BAD_REQUEST.value(),
                    "Пользователь c указанным именем уже существует"), HttpStatus.BAD_REQUEST);
        }
        clientService.create(clientDto);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }
}
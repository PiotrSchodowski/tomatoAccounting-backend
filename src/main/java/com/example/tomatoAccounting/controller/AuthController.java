package com.example.tomatoAccounting.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import com.example.tomatoAccounting.MessageResponse;
import com.example.tomatoAccounting.dto.AuthRequest;
import com.example.tomatoAccounting.dto.AuthResponse;
import com.example.tomatoAccounting.entity.UserEntity;
import com.example.tomatoAccounting.entity.UserRepository;
import com.example.tomatoAccounting.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
public class AuthController {

    @Value("${secret.key}")
    private String KEY;

    private final UserService userService;
    private final AuthenticationManager authorizationManager;

    private final UserRepository userRepository;

    public AuthController(AuthenticationManager authorizationManager, UserService userService, UserRepository userRepository) {
        this.authorizationManager = authorizationManager;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> getJwt(@RequestBody AuthRequest authRequest) {
        if (!userRepository.existsByEmail(authRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Taki użytkownik nie istnieje!"));
        }
        try {
            Authentication authenticate = authorizationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );
            UserEntity userEntity = (UserEntity) authenticate.getPrincipal();

            Algorithm algorithm = Algorithm.HMAC256(KEY);
            String token = JWT.create()
                    .withSubject(userEntity.getUsername())
                    .withIssuer("PiotrSchodowski")
                    .withClaim("roles", userEntity.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                    .sign(algorithm);

            AuthResponse authResponse = new AuthResponse(userEntity.getUsername(), token);
            return ResponseEntity.ok(authResponse);
        } catch (UsernameNotFoundException exception) {
            return ResponseEntity.badRequest().body(new MessageResponse("Nie poprawne dane logowania!"));
        } catch (BadCredentialsException exception) {
            return ResponseEntity.badRequest().body(new MessageResponse("Nieprawidłowe hasło, spróbuj ponownie!"));
        }
    }


    @PostMapping("/auth/register")
    public ResponseEntity<?> createNewUser(@RequestBody AuthRequest authRequest) {

        if (userRepository.existsByEmail(authRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Taki email jest już zajęty!"));
        }
        try {
             userService.createUser(authRequest.getEmail(), authRequest.getPassword());
            return ResponseEntity.ok(new MessageResponse("Użytkownik został zarejestrowany!"));
        } catch (RuntimeException e) {
            MessageResponse errorResponse = new MessageResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

}

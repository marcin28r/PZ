package com.library.backend.config.auth;

import com.library.backend.config.JwtService;
import com.library.backend.user.Role;
import com.library.backend.user.User;
import com.library.backend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .age(request.getAge())
                .password(passwordEncoder.encode(request.getPassword()))
                .username(request.getUsername())
                .email(request.getEmail())
                .registered(LocalDateTime.now())
                .rents(null)
                .role(Role.USER)
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateJwtToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authentication(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword()
                )
        );
        var user = repository.findByUsername(request.getUsername())
                .orElseThrow();
        var jwtToken = jwtService.generateJwtToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public Optional<User> authenticatedUser(String token) {
        return repository.findByUsername(jwtService.extractUsername(token));
    }

    public Role authenticatedRole(String token) {
        User user = repository.findByUsername(jwtService.extractUsername(token)).orElseThrow();
        System.out.println(user.getRole().toString());
        return user.getRole();
    }

    public boolean authenticatedAdmin(String token) {
        User user = repository.findByUsername(
                jwtService.extractUsername(token.split(" ")[1].trim())).orElseThrow();
        return Objects.equals(user.getRole(), Role.ADMIN);
    }
}

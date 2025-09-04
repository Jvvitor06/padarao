package com.example.padaria_paotorrado.controller;

import com.example.padaria_paotorrado.dto.RegisterDTO;
import com.example.padaria_paotorrado.infrastructure.entitys.User;
import com.example.padaria_paotorrado.infrastructure.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public AuthenticationController(UserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDTO data) {
        if (repository.findByUsername(data.login()).isPresent()) {
            return ResponseEntity.badRequest().body("Usuário já existente");
        }
        String encryptedPassword = encoder.encode(data.password());
        User newUser = new User(data.login(), encryptedPassword, data.role());
        repository.save(newUser);
        return ResponseEntity.ok("Usuário registrado com sucesso");
    }
}

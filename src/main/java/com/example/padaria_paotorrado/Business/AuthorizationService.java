package com.example.padaria_paotorrado.Business;

import com.example.padaria_paotorrado.infrastructure.entitys.User;
import com.example.padaria_paotorrado.infrastructure.repository.UserRepository;
import com.example.padaria_paotorrado.infrastructure.repository.role.UserRole;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {
    private final UserRepository repository;

    public AuthorizationService(UserRepository repository) {
        this.repository = repository;
    }
    public boolean userExists(String login) {
        return repository.findByUsername(login).isPresent(); // <-- em vez de != null
    }

    public void registerUser(String login, String password, String roleStr) {
        if (userExists(login)) {
            throw new RuntimeException("Usuario ja existe");
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(password);

        // Se quiser aceitar "admin" ou "user" independente de maiúsculas:
        UserRole roleEnum = UserRole.fromValue(roleStr); // usa seu @JsonCreator

        User newUser = new User(login, encryptedPassword, roleEnum);
        repository.save(newUser);
    }

    public User findByLogin(String login) {
        return repository.findByUsername(login)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + login));
    }

}
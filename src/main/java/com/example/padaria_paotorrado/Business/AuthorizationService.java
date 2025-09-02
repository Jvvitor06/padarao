package com.example.padaria_paotorrado.Business;

import com.example.padaria_paotorrado.infrastructure.entitys.User;
import com.example.padaria_paotorrado.infrastructure.repository.UserRepository;
import com.example.padaria_paotorrado.infrastructure.repository.role.UserRole;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {
    private final UserRepository repository;

    public AuthorizationService(UserRepository repository) {
        this.repository = repository;
    }
    public boolean userExists(String login) {
        return repository.findByUsername(login) !=null;
    }
    public void registerUser(String login, String password, String roleStr) {
        if (userExists(login)) {
            throw new RuntimeException("Usuario ja existe");
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(password);
        UserRole roleEnum;

        try {
            roleEnum = UserRole.valueOf(roleStr.toUpperCase());
        }catch (IllegalArgumentException e)  {
            throw new RuntimeException("Role invalido! use adm ou user");
        }
        User newUser = new User(login, encryptedPassword, roleEnum);
        repository.save(newUser);
    }
    public User findByLogin(String login) {
        return repository.findByUsername(login);
    }

}
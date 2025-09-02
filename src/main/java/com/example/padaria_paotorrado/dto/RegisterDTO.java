package com.example.padaria_paotorrado.dto;

import com.example.padaria_paotorrado.infrastructure.repository.role.UserRole;

public record RegisterDTO(
        String login,
        String password,
        UserRole role
) {
}

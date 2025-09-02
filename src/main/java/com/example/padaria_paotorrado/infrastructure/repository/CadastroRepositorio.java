package com.example.padaria_paotorrado.infrastructure.repository;

import com.example.padaria_paotorrado.infrastructure.entitys.Cadastro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CadastroRepositorio extends JpaRepository<Cadastro,Long> {
}

package com.example.padaria_paotorrado.Business;

import com.example.padaria_paotorrado.infrastructure.entitys.Cadastro;
import com.example.padaria_paotorrado.infrastructure.repository.CadastroRepositorio;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class CadastroService {
    private final CadastroRepositorio repository;

    public CadastroService(CadastroRepositorio repository) {
        this.repository = repository;
    }
    public Cadastro save(Cadastro cadastro) {
        return repository.saveAndFlush(cadastro);
    }
    public List<Cadastro> listarTodos() {
        return repository.findAll();
    }
}

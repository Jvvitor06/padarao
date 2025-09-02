package com.example.padaria_paotorrado.controller;

import com.example.padaria_paotorrado.Business.CadastroService;
import com.example.padaria_paotorrado.infrastructure.entitys.Cadastro;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cadastros")
public class CadastroController {
    private final CadastroService cadastroService;

    public CadastroController(CadastroService cadastroService) {
        this.cadastroService = cadastroService;
    }
    @PostMapping
    public ResponseEntity<Cadastro> salvar (@RequestBody Cadastro cadastro) {
        Cadastro salvo = cadastroService.save(cadastro);
        return ResponseEntity.ok(salvo);
    }
    @GetMapping
    public ResponseEntity<List<Cadastro>> listarTodos() {
        List<Cadastro> lista = cadastroService.listarTodos();
        return ResponseEntity.ok(lista);

    }
}

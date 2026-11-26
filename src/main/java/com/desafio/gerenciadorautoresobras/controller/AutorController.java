package com.desafio.gerenciadorautoresobras.controller;

import com.desafio.gerenciadorautoresobras.business.AutorService;
import com.desafio.gerenciadorautoresobras.business.dto.request.AutorRequestDTO;
import com.desafio.gerenciadorautoresobras.business.dto.response.AutorResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/autores")
@RequiredArgsConstructor

public class AutorController {

    private final AutorService autorService;

    @PostMapping
    public ResponseEntity<AutorResponseDTO> cadastrarAutor(@RequestBody @Valid AutorRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(autorService.cadastrarAutor(dto)
        );
    }

    @GetMapping
    public ResponseEntity<List<AutorResponseDTO>> listarAutores() {
        return ResponseEntity.ok(autorService.listarAutores());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AutorResponseDTO> buscarAutorPorId(@PathVariable Long id) {
        return ResponseEntity.ok(autorService.buscarAutorPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AutorResponseDTO> atualizarAutor(@PathVariable Long id,
                                                           @RequestBody AutorRequestDTO dto) {
        return ResponseEntity.ok(autorService.atualizarAutor(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAutor(@PathVariable Long id) {
        autorService.deletarAutor(id);
        return ResponseEntity.noContent().build();
    }

}

package com.desafio.gerenciadorautoresobras.controller;

import com.desafio.gerenciadorautoresobras.business.ObraService;
import com.desafio.gerenciadorautoresobras.business.dto.request.ObraRequestDTO;
import com.desafio.gerenciadorautoresobras.business.dto.response.ObraResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/obras")
@RequiredArgsConstructor

public class ObraController {
    private final ObraService obraService;

    @PostMapping
    public ResponseEntity<ObraResponseDTO> cadastrarObra(@RequestBody @Valid ObraRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(obraService.cadastrarObra(dto));
    }

    @GetMapping
    public ResponseEntity<List<ObraResponseDTO>> listarObras() {
        return ResponseEntity.ok(obraService.listarObras());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ObraResponseDTO> buscarObraPorId(@PathVariable Long id) {
        return ResponseEntity.ok(obraService.buscarObraPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ObraResponseDTO> atualizarObra(@PathVariable Long id, @RequestBody ObraRequestDTO dto) {
        return ResponseEntity.ok(obraService.atualizarObra(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarObra(@PathVariable Long id) {
        obraService.deletarObra(id);
        return ResponseEntity.noContent().build();
    }
}

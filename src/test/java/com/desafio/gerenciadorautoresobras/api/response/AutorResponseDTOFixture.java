package com.desafio.gerenciadorautoresobras.api.response;

import com.desafio.gerenciadorautoresobras.business.dto.response.AutorResponseDTO;
import com.desafio.gerenciadorautoresobras.infrastructure.enums.SexoEnum;

import java.time.LocalDate;

public class AutorResponseDTOFixture {

    public static AutorResponseDTO build(Long id,
                                         String nome,
                                         SexoEnum sexo,
                                         String email,
                                         LocalDate dataNascimento,
                                         String paisOrigem,
                                         String cpf) {

        return new AutorResponseDTO(id, nome, sexo, email, dataNascimento, paisOrigem, cpf);
    }
}

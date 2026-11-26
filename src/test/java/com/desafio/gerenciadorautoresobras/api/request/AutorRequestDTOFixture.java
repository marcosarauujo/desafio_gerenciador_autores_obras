package com.desafio.gerenciadorautoresobras.api.request;

import com.desafio.gerenciadorautoresobras.business.dto.request.AutorRequestDTO;
import com.desafio.gerenciadorautoresobras.infrastructure.enums.SexoEnum;

import java.time.LocalDate;

public class AutorRequestDTOFixture {

    public static AutorRequestDTO build(String nome,
                                        SexoEnum sexo,
                                        String email,
                                        LocalDate dataNascimento,
                                        String paisOrigem,
                                        String cpf) {

        return new AutorRequestDTO(nome, sexo, email, dataNascimento, paisOrigem, cpf);
    }
}

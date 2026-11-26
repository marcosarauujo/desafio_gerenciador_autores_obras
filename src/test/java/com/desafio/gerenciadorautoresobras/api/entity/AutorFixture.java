package com.desafio.gerenciadorautoresobras.api.entity;

import com.desafio.gerenciadorautoresobras.infrastructure.entities.Autor;
import com.desafio.gerenciadorautoresobras.infrastructure.entities.Obra;
import com.desafio.gerenciadorautoresobras.infrastructure.enums.SexoEnum;

import java.time.LocalDate;
import java.util.List;

public class AutorFixture {

    public static Autor build(Long id,
                              String nome,
                              SexoEnum sexo,
                              String email,
                              LocalDate dataNascimento,
                              String paisOrigem,
                              String cpf,
                              List<Obra> obras) {

        return new Autor(id, nome, sexo, email, dataNascimento, paisOrigem, cpf, obras);
    }
}

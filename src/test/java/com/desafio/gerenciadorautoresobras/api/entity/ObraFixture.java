package com.desafio.gerenciadorautoresobras.api.entity;

import com.desafio.gerenciadorautoresobras.infrastructure.entities.Autor;
import com.desafio.gerenciadorautoresobras.infrastructure.entities.Obra;

import java.time.LocalDate;
import java.util.List;

public class ObraFixture {
    public static Obra build(Long id,
                             String nome,
                             String descricao,
                             LocalDate dataPublicacao,
                             LocalDate dataExposicao,
                             List<Autor> autores) {

        return new Obra(id, nome, descricao, dataPublicacao, dataExposicao, autores);
    }
}

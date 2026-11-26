package com.desafio.gerenciadorautoresobras.api.request;

import com.desafio.gerenciadorautoresobras.business.dto.request.ObraRequestDTO;

import java.time.LocalDate;
import java.util.List;

public class ObraRequestDTOFixture {

    public static ObraRequestDTO build(String nome,
                                       String descricao,
                                       LocalDate dataPublicacao,
                                       LocalDate dataExposicao,
                                       List<Long> autoresIds) {

        return new ObraRequestDTO(nome, descricao, dataPublicacao, dataExposicao, autoresIds);
    }
}

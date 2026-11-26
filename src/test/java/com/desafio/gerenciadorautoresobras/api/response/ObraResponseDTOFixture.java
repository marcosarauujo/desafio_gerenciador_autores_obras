package com.desafio.gerenciadorautoresobras.api.response;

import com.desafio.gerenciadorautoresobras.business.dto.response.AutorResponseDTO;
import com.desafio.gerenciadorautoresobras.business.dto.response.ObraResponseDTO;

import java.time.LocalDate;
import java.util.List;

public class ObraResponseDTOFixture {
    public static ObraResponseDTO build(Long id,
                                        String nome,
                                        String descricao,
                                        LocalDate dataPublicacao,
                                        LocalDate dataExposicao,
                                        List<AutorResponseDTO> autores) {

        return new ObraResponseDTO(id, nome, descricao, dataPublicacao, dataExposicao, autores);
    }

}

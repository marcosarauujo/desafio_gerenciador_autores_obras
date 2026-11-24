package com.desafio.gerenciadorautoresobras.business.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ObraResponseDTO {
    private Long id;
    private String nome;
    private String descricao;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataPublicacao;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataExposicao;
    private List<AutorResponseDTO> autores;
}

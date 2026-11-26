package com.desafio.gerenciadorautoresobras.business.dto.response;

import com.desafio.gerenciadorautoresobras.infrastructure.enums.SexoEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class AutorResponseDTO {
    private Long id;
    private String nome;
    private SexoEnum sexo;
    private String email;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataNascimento;
    private String paisOrigem;
    private String cpf;
}

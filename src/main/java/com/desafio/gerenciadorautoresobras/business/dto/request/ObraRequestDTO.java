package com.desafio.gerenciadorautoresobras.business.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ObraRequestDTO {
    @NotBlank(message = "O nome da obra é obrigatório")
    private String nome;
    @NotBlank(message = "A descrição da obra é obrigatória")
    @Size(max = 240, message = "A descrição não pode ter mais de 240 caracteres")
    private String descricao;
    @NotNull(message = "A data de publicação é obrigatória")
    @PastOrPresent(message = "A data de publicação não pode ser uma data futura")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataPublicacao;
    @PastOrPresent(message = "A data de exposição não pode ser uma data futura")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataExposicao;
    @NotEmpty(message = "A obra deve possuir pelo menos um autor associado")
    private List<Long> autoresIds;
}

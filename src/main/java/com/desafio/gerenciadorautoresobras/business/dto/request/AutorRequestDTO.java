package com.desafio.gerenciadorautoresobras.business.dto.request;

import com.desafio.gerenciadorautoresobras.infrastructure.enums.SexoEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class AutorRequestDTO {

    @NotBlank(message = "O nome é obrigatório")
    private String nome;
    private SexoEnum sexo;
    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Formato de e-mail inválido")
    private String email;
    @NotNull(message = "A data de nascimento é obrigatória")
    @Past(message = "A data de nascimento não pode ser uma data futura")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataNascimento;
    @NotBlank(message = "O país de origem é obrigatório")
    private String paisOrigem;
    private String cpf;

}

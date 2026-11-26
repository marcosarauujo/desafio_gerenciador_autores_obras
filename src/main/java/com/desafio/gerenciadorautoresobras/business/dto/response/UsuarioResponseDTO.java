package com.desafio.gerenciadorautoresobras.business.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UsuarioResponseDTO {
    private Long id;
    private String nome;
    private String email;
}

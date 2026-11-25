package com.desafio.gerenciadorautoresobras.infrastructure.entities;

import com.desafio.gerenciadorautoresobras.infrastructure.enums.SexoEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "autor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@EqualsAndHashCode(of = "id")

public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    private SexoEnum sexo;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(name = "pais_origem", nullable = false)
    private String paisOrigem;

    @Column(unique = true)
    private String cpf;

    @Builder.Default
    @ManyToMany(mappedBy = "autores")
    private List<Obra> obras = new ArrayList<>();

}

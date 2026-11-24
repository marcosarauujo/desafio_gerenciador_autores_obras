package com.desafio.gerenciadorautoresobras.infrastructure.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "obra")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Obra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false, length = 240)
    private String descricao;
    @Column(name = "data_publicacao", nullable = false)
    private LocalDate dataPublicacao;
    @Column(name = "data_exposicao")
    private LocalDate dataExposicao;
    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "autor_obra",
            joinColumns = @JoinColumn(name = "obra_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private List<Autor> autores = new ArrayList<>();
}

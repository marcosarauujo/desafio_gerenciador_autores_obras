package com.desafio.gerenciadorautoresobras.infrastructure.repository;

import com.desafio.gerenciadorautoresobras.infrastructure.entities.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {

    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);

    Optional<Autor> findByEmail(String email);
}

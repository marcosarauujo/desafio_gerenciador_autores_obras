package com.desafio.gerenciadorautoresobras.infrastructure.repository;

import com.desafio.gerenciadorautoresobras.infrastructure.entities.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {
}

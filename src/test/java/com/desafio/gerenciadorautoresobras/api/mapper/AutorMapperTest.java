package com.desafio.gerenciadorautoresobras.api.mapper;

import com.desafio.gerenciadorautoresobras.api.request.AutorRequestDTOFixture;
import com.desafio.gerenciadorautoresobras.api.response.AutorResponseDTOFixture;
import com.desafio.gerenciadorautoresobras.business.dto.request.AutorRequestDTO;
import com.desafio.gerenciadorautoresobras.business.dto.response.AutorResponseDTO;
import com.desafio.gerenciadorautoresobras.business.mapper.AutorMapper;
import com.desafio.gerenciadorautoresobras.infrastructure.entities.Autor;
import com.desafio.gerenciadorautoresobras.infrastructure.enums.SexoEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AutorMapperTest {

    AutorMapper autorMapper;
    Autor autorEntity;
    AutorRequestDTO autorRequestDTO;
    AutorResponseDTO autorResponseDTO;
    List<Autor> listaAutorEntity;
    List<AutorResponseDTO> listaAutorResponseDTO;
    LocalDate dataNascimento;

    @BeforeEach
    public void setup() {
        autorMapper = Mappers.getMapper(AutorMapper.class);
        dataNascimento = LocalDate.of(1839, 6, 21);

        autorEntity = Autor.builder()
                .nome("Machado de Assis")
                .sexo(SexoEnum.MASCULINO)
                .email("machado@email.com")
                .dataNascimento(dataNascimento)
                .paisOrigem("Brasil")
                .cpf("12345678909")
                .obras(new ArrayList<>())
                .build();

        autorRequestDTO = AutorRequestDTOFixture.build(
                "Machado de Assis",
                SexoEnum.MASCULINO,
                "machado@email.com",
                dataNascimento,
                "Brasil",
                "12345678909"
        );

        autorResponseDTO = AutorResponseDTOFixture.build(
                null,
                "Machado de Assis",
                SexoEnum.MASCULINO,
                "machado@email.com",
                dataNascimento,
                "Brasil",
                "12345678909"
        );

        listaAutorEntity = Collections.singletonList(autorEntity);
        listaAutorResponseDTO = Collections.singletonList(autorResponseDTO);
    }
    @Test
    void deveConverterParaEntityComSucesso() {
        Autor entity = autorMapper.toEntity(autorRequestDTO);
        assertEquals(autorEntity, entity);
    }

    @Test
    void deveConverterParaDTOComSucesso() {
        AutorResponseDTO dto = autorMapper.toResponseDTO(autorEntity);
        assertEquals(autorResponseDTO, dto);
    }

    @Test
    void deveConverterParaListaDTOComSucesso() {
        List<AutorResponseDTO> dtos = autorMapper.toResponseDTOList(listaAutorEntity);
        assertEquals(listaAutorResponseDTO, dtos);
    }

    @Test
    void deveAtualizarEntidadeComCamposNaoNulosDoDTO() {
        AutorRequestDTO dtoAtualizacao = AutorRequestDTOFixture.build(
                "Joaquim Maria Machado de Assis",
                null,
                null,
                null,
                null,
                null
        );
        autorMapper.updateAutorFromDTO(dtoAtualizacao, autorEntity);
        assertEquals("Joaquim Maria Machado de Assis", autorEntity.getNome());
        assertEquals("machado@email.com", autorEntity.getEmail());
    }

}

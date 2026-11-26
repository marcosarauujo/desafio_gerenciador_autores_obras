package com.desafio.gerenciadorautoresobras.api.mapper;

import com.desafio.gerenciadorautoresobras.api.entity.AutorFixture;
import com.desafio.gerenciadorautoresobras.api.entity.ObraFixture;
import com.desafio.gerenciadorautoresobras.api.request.ObraRequestDTOFixture;
import com.desafio.gerenciadorautoresobras.api.response.AutorResponseDTOFixture;
import com.desafio.gerenciadorautoresobras.api.response.ObraResponseDTOFixture;
import com.desafio.gerenciadorautoresobras.business.dto.request.ObraRequestDTO;
import com.desafio.gerenciadorautoresobras.business.dto.response.AutorResponseDTO;
import com.desafio.gerenciadorautoresobras.business.dto.response.ObraResponseDTO;
import com.desafio.gerenciadorautoresobras.business.mapper.AutorMapper;
import com.desafio.gerenciadorautoresobras.business.mapper.ObraMapper;
import com.desafio.gerenciadorautoresobras.infrastructure.entities.Autor;
import com.desafio.gerenciadorautoresobras.infrastructure.entities.Obra;
import com.desafio.gerenciadorautoresobras.infrastructure.enums.SexoEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ObraMapperTest {

    ObraMapper obraMapper;
    Obra obraEntity;
    ObraRequestDTO obraRequestDTO;
    ObraResponseDTO obraResponseDTO;

    Autor autorEntity;
    AutorResponseDTO autorResponseDTO;

    List<Obra> listaObraEntity;

    List<ObraResponseDTO> listaObraResponseDTO;
    LocalDate dataPublicacao;

    @BeforeEach
    public void setup() {
        obraMapper = Mappers.getMapper(ObraMapper.class);
        AutorMapper autorMapper = Mappers.getMapper(AutorMapper.class);
        org.springframework.test.util.ReflectionTestUtils.setField(obraMapper, "autorMapper", autorMapper);
        dataPublicacao = LocalDate.of(1899, 1, 1);

        autorEntity = AutorFixture.build(
                1L,
                "Machado de Assis",
                SexoEnum.MASCULINO,
                "machado@email.com",
                LocalDate.of(1839, 6, 21),
                "Brasil",
                "12345678909",
                null
        );
        autorResponseDTO = AutorResponseDTOFixture.build(
                1L,
                "Machado de Assis",
                SexoEnum.MASCULINO,
                "machado@email.com",
                LocalDate.of(1839, 6, 21),
                "Brasil",
                "12345678909"
        );
        obraEntity = ObraFixture.build(
                null,
                "Dom Casmurro",
                "Memórias de Bento Santiago sobre Capitu",
                dataPublicacao,
                null,
                Collections.singletonList(autorEntity)
        );
        obraRequestDTO = ObraRequestDTOFixture.build(
                "Dom Casmurro",
                "Memórias de Bento Santiago sobre Capitu",
                dataPublicacao,
                null,
                Collections.singletonList(1L)
        );
        obraResponseDTO = ObraResponseDTOFixture.build(
                null,
                "Dom Casmurro",
                "Memórias de Bento Santiago sobre Capitu",
                dataPublicacao,
                null,
                Collections.singletonList(autorResponseDTO)
        );
        listaObraEntity = Collections.singletonList(obraEntity);
        listaObraResponseDTO = Collections.singletonList(obraResponseDTO);
    }

    @Test
    void deveConverterParaEntityComSucesso() {
        Obra entity = obraMapper.toEntity(obraRequestDTO);

        assertEquals(obraRequestDTO.getNome(), entity.getNome());
        assertEquals(obraRequestDTO.getDescricao(), entity.getDescricao());
        assertEquals(obraRequestDTO.getDataPublicacao(), entity.getDataPublicacao());
        assertEquals(obraRequestDTO.getDataExposicao(), entity.getDataExposicao());
    }

    @Test
    void deveConverterParaDTOComSucesso() {
        ObraResponseDTO dto = obraMapper.toResponseDTO(obraEntity);
        assertEquals(obraResponseDTO, dto);
    }
    @Test
    void deveConverterParaListaDTOComSucesso() {
        List<ObraResponseDTO> dtos = obraMapper.toResponseDTOList(listaObraEntity);
        assertEquals(listaObraResponseDTO, dtos);
    }
    @Test
    void deveAtualizarEntidadeComCamposNaoNulosDoDTO() {
        ObraRequestDTO dtoAtualizacao = ObraRequestDTOFixture.build(
                "Dom Casmurro - Edição de Luxo",
                null,
                null,
                null,
                null
        );
        obraMapper.updateObraFromDTO(dtoAtualizacao, obraEntity);
        assertEquals("Dom Casmurro - Edição de Luxo", obraEntity.getNome());
        assertEquals("Memórias de Bento Santiago sobre Capitu", obraEntity.getDescricao());
    }
}


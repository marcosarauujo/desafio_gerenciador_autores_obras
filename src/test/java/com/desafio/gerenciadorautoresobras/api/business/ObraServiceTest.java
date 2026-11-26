package com.desafio.gerenciadorautoresobras.api.business;

import com.desafio.gerenciadorautoresobras.api.entity.AutorFixture;
import com.desafio.gerenciadorautoresobras.api.entity.ObraFixture;
import com.desafio.gerenciadorautoresobras.api.request.ObraRequestDTOFixture;
import com.desafio.gerenciadorautoresobras.api.response.AutorResponseDTOFixture;
import com.desafio.gerenciadorautoresobras.api.response.ObraResponseDTOFixture;
import com.desafio.gerenciadorautoresobras.business.ObraService;
import com.desafio.gerenciadorautoresobras.business.dto.request.ObraRequestDTO;
import com.desafio.gerenciadorautoresobras.business.dto.response.AutorResponseDTO;
import com.desafio.gerenciadorautoresobras.business.dto.response.ObraResponseDTO;
import com.desafio.gerenciadorautoresobras.business.mapper.ObraMapper;
import com.desafio.gerenciadorautoresobras.infrastructure.entities.Autor;
import com.desafio.gerenciadorautoresobras.infrastructure.entities.Obra;
import com.desafio.gerenciadorautoresobras.infrastructure.enums.SexoEnum;
import com.desafio.gerenciadorautoresobras.infrastructure.exceptions.NegocioException;
import com.desafio.gerenciadorautoresobras.infrastructure.exceptions.RecursoNaoEncontradoException;
import com.desafio.gerenciadorautoresobras.infrastructure.repository.AutorRepository;
import com.desafio.gerenciadorautoresobras.infrastructure.repository.ObraRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ObraServiceTest {

    @InjectMocks
    ObraService obraService;

    @Mock
    ObraRepository obraRepository;
    @Mock
    AutorRepository autorRepository;
    @Mock
    ObraMapper obraMapper;

    Obra obraEntity;
    ObraRequestDTO obraRequestDTO;
    ObraResponseDTO obraResponseDTO;
    Autor autorEntity;
    AutorResponseDTO autorResponseDTO;
    List<Obra> listaObraEntity;
    List<ObraResponseDTO> listaObraResponseDTO;
    List<Autor> listaAutores;
    List<Long> autoresIds;

    ObraRequestDTO obraSemAutorRequestDTO;

    LocalDate dataPublicacao;


    @BeforeEach
    public void setup() {
        dataPublicacao = LocalDate.of(1899, 1, 1);
        autoresIds = Collections.singletonList(1L);

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

        listaAutores = Collections.singletonList(autorEntity);

        obraEntity = ObraFixture.build(
                1L,
                "Dom Casmurro",
                "Memórias de Bento Santiago sobre Capitu",
                dataPublicacao,
                null,
                listaAutores
        );

        obraRequestDTO = ObraRequestDTOFixture.build(
                "Dom Casmurro",
                "Memórias de Bento Santiago sobre Capitu",
                dataPublicacao,
                null,
                autoresIds
        );

        obraResponseDTO = ObraResponseDTOFixture.build(
                1L,
                "Dom Casmurro",
                "Memórias de Bento Santiago sobre Capitu",
                dataPublicacao,
                null,
                Collections.singletonList(autorResponseDTO)
        );

        obraSemAutorRequestDTO = ObraRequestDTOFixture.build(
                "Dom Casmurro",
                "Memórias de Bento Santiago sobre Capitu",
                dataPublicacao,
                null,
                Collections.emptyList() // Lista vazia de autores!
        );

        listaObraEntity = Collections.singletonList(obraEntity);
        listaObraResponseDTO = Collections.singletonList(obraResponseDTO);
    }

    // TESTES DE SUCESSO: cadastrarObra

    @Test
    void deveCadastrarObraComSucesso() {

        when(autorRepository.findAllById(autoresIds)).thenReturn(listaAutores);
        when(obraMapper.toEntity(obraRequestDTO)).thenReturn(obraEntity);
        when(obraRepository.save(obraEntity)).thenReturn(obraEntity);
        when(obraMapper.toResponseDTO(obraEntity)).thenReturn(obraResponseDTO);

        ObraResponseDTO resultado = obraService.cadastrarObra(obraRequestDTO);

        assertEquals(obraResponseDTO, resultado);

        verify(autorRepository).findAllById(autoresIds);
        verify(obraMapper).toEntity(obraRequestDTO);
        verify(obraRepository).save(obraEntity);
        verify(obraMapper).toResponseDTO(obraEntity);

        verifyNoMoreInteractions(obraRepository);
        verifyNoMoreInteractions(autorRepository);
        verifyNoMoreInteractions(obraMapper);
    }

    // TESTES DE FALHA: cadastrarObra

    @Test
    void deveLancarExcecaoQuandoObraNaoTiverAutores() {

        NegocioException exception = assertThrows(NegocioException.class, () -> {
            obraService.cadastrarObra(obraSemAutorRequestDTO);
        });
        assertEquals("A obra deve possuir pelo menos um autor associado.", exception.getMessage());

        verifyNoInteractions(obraRepository);
        verifyNoInteractions(autorRepository);
        verifyNoInteractions(obraMapper);
    }

    @Test
    void deveLancarExcecaoQuandoAutorInformadoNaoExistirNoBanco() {

        when(autorRepository.findAllById(autoresIds)).thenReturn(Collections.emptyList());

        NegocioException exception = assertThrows(NegocioException.class, () -> {
            obraService.cadastrarObra(obraRequestDTO);
        });
        assertEquals("Um ou mais autores informados não foram encontrados no sistema.", exception.getMessage());

        verify(autorRepository).findAllById(autoresIds);

        verifyNoMoreInteractions(autorRepository);
        verifyNoInteractions(obraRepository);
        verifyNoInteractions(obraMapper);
    }

    // TESTES DE SUCESSO: buscarObraPorId

    @Test
    void deveBuscarObraPorIdComSucesso() {

        when(obraRepository.findById(1L)).thenReturn(Optional.of(obraEntity));
        when(obraMapper.toResponseDTO(obraEntity)).thenReturn(obraResponseDTO);

        ObraResponseDTO resultado = obraService.buscarObraPorId(1L);

        assertEquals(obraResponseDTO, resultado);

        verify(obraRepository).findById(1L);
        verify(obraMapper).toResponseDTO(obraEntity);

        verifyNoMoreInteractions(obraRepository);
        verifyNoMoreInteractions(obraMapper);
        verifyNoInteractions(autorRepository);
    }

    // TESTES DE FALHA: buscarObraPorId

    @Test
    void deveLancarExcecaoQuandoBuscarObraPorIdInexistente() {

        when(obraRepository.findById(1L)).thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(RecursoNaoEncontradoException.class, () -> {
            obraService.buscarObraPorId(1L);
        });
        assertEquals("Obra não encontrada com id: 1", exception.getMessage());

        verify(obraRepository).findById(1L);

        verifyNoMoreInteractions(obraRepository);
        verifyNoInteractions(obraMapper);
        verifyNoInteractions(autorRepository);
    }

    // TESTES DE SUCESSO: listarObras

    @Test
    void deveListarTodasAsObrasComSucesso() {

        when(obraRepository.findAll()).thenReturn(listaObraEntity);
        when(obraMapper.toResponseDTOList(listaObraEntity)).thenReturn(listaObraResponseDTO);

        List<ObraResponseDTO> resultado = obraService.listarObras();

        assertEquals(listaObraResponseDTO, resultado);

        verify(obraRepository).findAll();
        verify(obraMapper).toResponseDTOList(listaObraEntity);

        verifyNoMoreInteractions(obraRepository);
        verifyNoMoreInteractions(obraMapper);
        verifyNoInteractions(autorRepository);
    }

    // TESTES DE SUCESSO: atualizarObra

    @Test
    void deveAtualizarObraComSucesso() {

        when(obraRepository.findById(1L)).thenReturn(Optional.of(obraEntity));
        when(autorRepository.findAllById(autoresIds)).thenReturn(listaAutores);
        when(obraRepository.save(obraEntity)).thenReturn(obraEntity);
        when(obraMapper.toResponseDTO(obraEntity)).thenReturn(obraResponseDTO);

        ObraResponseDTO resultado = obraService.atualizarObra(1L, obraRequestDTO);

        assertEquals(obraResponseDTO, resultado);

        verify(obraRepository).findById(1L);
        verify(obraMapper).updateObraFromDTO(obraRequestDTO, obraEntity);
        verify(autorRepository).findAllById(autoresIds);
        verify(obraRepository).save(obraEntity);
        verify(obraMapper).toResponseDTO(obraEntity);

        verifyNoMoreInteractions(obraRepository);
        verifyNoMoreInteractions(autorRepository);
        verifyNoMoreInteractions(obraMapper);
    }

    // TESTES DE FALHA: atualizarObra

    @Test
    void deveLancarExcecaoAoAtualizarObraInexistente() {

        when(obraRepository.findById(1L)).thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(RecursoNaoEncontradoException.class, () -> {
            obraService.atualizarObra(1L, obraRequestDTO);
        });
        assertEquals("Obra não encontrada com id: 1", exception.getMessage());

        verify(obraRepository).findById(1L);

        verifyNoMoreInteractions(obraRepository);
        verifyNoInteractions(obraMapper);
        verifyNoInteractions(autorRepository);
    }

    // TESTES DE SUCESSO: deletarObra

    @Test
    void deveDeletarObraComSucesso() {

        when(obraRepository.findById(1L)).thenReturn(Optional.of(obraEntity));
        obraService.deletarObra(1L);

        verify(obraRepository).findById(1L);
        verify(obraRepository).delete(obraEntity);

        verifyNoMoreInteractions(obraRepository);
        verifyNoInteractions(obraMapper);
        verifyNoInteractions(autorRepository);
    }

    // TESTES DE FALHA: deletarObra

    @Test
    void deveLancarExcecaoAoDeletarObraInexistente() {

        when(obraRepository.findById(1L)).thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(RecursoNaoEncontradoException.class, () -> {
            obraService.deletarObra(1L);
        });
        assertEquals("Obra não encontrada com id: 1", exception.getMessage());

        verify(obraRepository).findById(1L);

        verifyNoMoreInteractions(obraRepository);
        verifyNoInteractions(obraMapper);
        verifyNoInteractions(autorRepository);
    }
}
package com.desafio.gerenciadorautoresobras.api.business;

import com.desafio.gerenciadorautoresobras.api.entity.AutorFixture;
import com.desafio.gerenciadorautoresobras.api.request.AutorRequestDTOFixture;
import com.desafio.gerenciadorautoresobras.api.response.AutorResponseDTOFixture;
import com.desafio.gerenciadorautoresobras.business.AutorService;
import com.desafio.gerenciadorautoresobras.business.dto.request.AutorRequestDTO;
import com.desafio.gerenciadorautoresobras.business.dto.response.AutorResponseDTO;
import com.desafio.gerenciadorautoresobras.business.mapper.AutorMapper;
import com.desafio.gerenciadorautoresobras.infrastructure.entities.Autor;
import com.desafio.gerenciadorautoresobras.infrastructure.enums.SexoEnum;
import com.desafio.gerenciadorautoresobras.infrastructure.exceptions.NegocioException;
import com.desafio.gerenciadorautoresobras.infrastructure.exceptions.RecursoNaoEncontradoException;
import com.desafio.gerenciadorautoresobras.infrastructure.repository.AutorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AutorServiceTest {

    @InjectMocks
    AutorService autorService;

    @Mock
    AutorRepository autorRepository;
    @Mock
    AutorMapper autorMapper;

    Autor autorEntity;
    AutorRequestDTO autorRequestDTO;
    AutorResponseDTO autorResponseDTO;

    Autor autorEstrangeiroEntity;
    AutorRequestDTO autorEstrangeiroRequestDTO;
    AutorResponseDTO autorEstrangeiroResponseDTO;

    AutorRequestDTO autorBrasileiroSemCpfRequestDTO;
    AutorRequestDTO autorEstrangeiroComCpfRequestDTO;

    List<Autor> listaAutorEntity;
    List<AutorResponseDTO> listaAutorResponseDTO;

    Autor autorComObraEntity;

    LocalDate dataNascimento;

    @BeforeEach
    public void setup() {
        dataNascimento = LocalDate.of(1839, 6, 21);

        autorEntity = AutorFixture.build(
                1L,
                "Machado de Assis",
                SexoEnum.MASCULINO,
                "machado@email.com",
                dataNascimento,
                "Brasil",
                "12345678909",
                new ArrayList<>()
        );
        autorRequestDTO = AutorRequestDTOFixture.build(
                "Machado de Assis",
                SexoEnum.MASCULINO,
                "machado@email.com",
                dataNascimento,
                "Brasil",
                "12345678909"
        );
        autorResponseDTO = AutorResponseDTOFixture.build(
                1L,
                "Machado de Assis",
                SexoEnum.MASCULINO,
                "machado@email.com",
                dataNascimento,
                "Brasil",
                "12345678909"
        );

        autorEstrangeiroEntity = AutorFixture.build(
                2L,
                "Gabriel García Márquez",
                SexoEnum.MASCULINO,
                "gabo@email.com",
                LocalDate.of(1927, 3, 6),
                "Colômbia",
                null,
                new ArrayList<>()
        );
        autorEstrangeiroRequestDTO = AutorRequestDTOFixture.build(
                "Gabriel García Márquez",
                SexoEnum.MASCULINO,
                "gabo@email.com",
                LocalDate.of(1927, 3, 6),
                "Colômbia",
                null
        );
        autorEstrangeiroResponseDTO = AutorResponseDTOFixture.build(
                2L,
                "Gabriel García Márquez",
                SexoEnum.MASCULINO,
                "gabo@email.com",
                LocalDate.of(1927, 3, 6),
                "Colômbia",
                null
        );

        autorBrasileiroSemCpfRequestDTO = AutorRequestDTOFixture.build(
                "Clarice Lispector",
                SexoEnum.FEMININO,
                "clarice@email.com",
                LocalDate.of(1920, 12, 10),
                "Brasil",
                null
        );

        autorEstrangeiroComCpfRequestDTO = AutorRequestDTOFixture.build(
                "Gabriel García Márquez",
                SexoEnum.MASCULINO,
                "gabo@email.com",
                LocalDate.of(1927, 3, 6),
                "Colômbia",
                "12345678909" // País não é Brasil, mas veio CPF!
        );

        listaAutorEntity = Collections.singletonList(autorEntity);
        listaAutorResponseDTO = Collections.singletonList(autorResponseDTO);

        autorComObraEntity = AutorFixture.build(
                1L,
                "Machado de Assis",
                SexoEnum.MASCULINO,
                "machado@email.com",
                dataNascimento,
                "Brasil",
                "12345678909",
                new ArrayList<>()
        );
        autorComObraEntity.getObras().add(new com.desafio.gerenciadorautoresobras.infrastructure.entities.Obra());

    }

    // TESTES DE SUCESSO: cadastrarAutor

    @Test
    void deveCadastrarAutorBrasileiroComSucesso() {

        when(autorRepository.existsByEmail(autorRequestDTO.getEmail())).thenReturn(false);
        when(autorRepository.existsByCpf(autorRequestDTO.getCpf())).thenReturn(false);
        when(autorMapper.toEntity(autorRequestDTO)).thenReturn(autorEntity);
        when(autorRepository.save(autorEntity)).thenReturn(autorEntity);
        when(autorMapper.toResponseDTO(autorEntity)).thenReturn(autorResponseDTO);

        AutorResponseDTO resultado = autorService.cadastrarAutor(autorRequestDTO);

        assertEquals(autorResponseDTO, resultado);

        verify(autorRepository).existsByEmail(autorRequestDTO.getEmail());
        verify(autorRepository).existsByCpf(autorRequestDTO.getCpf());
        verify(autorMapper).toEntity(autorRequestDTO);
        verify(autorRepository).save(autorEntity);
        verify(autorMapper).toResponseDTO(autorEntity);

        verifyNoMoreInteractions(autorRepository);
        verifyNoMoreInteractions(autorMapper);

    }

    @Test
    void deveCadastrarAutorEstrangeiroComSucesso() {

        when(autorRepository.existsByEmail(autorEstrangeiroRequestDTO.getEmail())).thenReturn(false);
        when(autorMapper.toEntity(autorEstrangeiroRequestDTO)).thenReturn(autorEstrangeiroEntity);
        when(autorRepository.save(autorEstrangeiroEntity)).thenReturn(autorEstrangeiroEntity);
        when(autorMapper.toResponseDTO(autorEstrangeiroEntity)).thenReturn(autorEstrangeiroResponseDTO);

        AutorResponseDTO resultado = autorService.cadastrarAutor(autorEstrangeiroRequestDTO);

        assertEquals(autorEstrangeiroResponseDTO, resultado);

        verify(autorRepository).existsByEmail(autorEstrangeiroRequestDTO.getEmail());
        verify(autorMapper).toEntity(autorEstrangeiroRequestDTO);
        verify(autorRepository).save(autorEstrangeiroEntity);
        verify(autorMapper).toResponseDTO(autorEstrangeiroEntity);

        verifyNoMoreInteractions(autorRepository);
        verifyNoMoreInteractions(autorMapper);
    }

    // TESTES DE FALHA / EXCEÇÕES: cadastrarAutor

    @Test
    void deveLancarExcecaoQuandoEmailJaEstiverCadastrado() {

        when(autorRepository.existsByEmail(autorRequestDTO.getEmail())).thenReturn(true);
        NegocioException exception = assertThrows(NegocioException.class, () -> {
            autorService.cadastrarAutor(autorRequestDTO);
        });

        assertEquals("Já existe um autor cadastrado com este e-mail: "
                + autorRequestDTO.getEmail(), exception.getMessage());

        verify(autorRepository).existsByEmail(autorRequestDTO.getEmail());

        verifyNoMoreInteractions(autorRepository);
        verifyNoInteractions(autorMapper);

    }

    @Test
    void deveLancarExcecaoQuandoAutorBrasileiroNaoTiverCpf() {

        when(autorRepository.existsByEmail(autorBrasileiroSemCpfRequestDTO.getEmail())).thenReturn(false);

        NegocioException exception = assertThrows(NegocioException.class, () -> {
            autorService.cadastrarAutor(autorBrasileiroSemCpfRequestDTO);
        });
        assertEquals("Para autores do Brasil, o CPF é obrigatório.", exception.getMessage());

        verify(autorRepository).existsByEmail(autorBrasileiroSemCpfRequestDTO.getEmail());

        verifyNoMoreInteractions(autorRepository);
        verifyNoInteractions(autorMapper);
    }

    @Test
    void deveLancarExcecaoQuandoAutorBrasileiroTiverCpfDuplicado() {
        when(autorRepository.existsByEmail(autorRequestDTO.getEmail())).thenReturn(false);
        when(autorRepository.existsByCpf(autorRequestDTO.getCpf())).thenReturn(true);

        NegocioException exception = assertThrows(NegocioException.class, () -> {
            autorService.cadastrarAutor(autorRequestDTO);
        });
        assertEquals("Já existe um autor cadastrado com este CPF.", exception.getMessage());

        verify(autorRepository).existsByEmail(autorRequestDTO.getEmail());
        verify(autorRepository).existsByCpf(autorRequestDTO.getCpf());

        verifyNoMoreInteractions(autorRepository);
        verifyNoInteractions(autorMapper);
    }

    @Test
    void deveLancarExcecaoQuandoAutorEstrangeiroTiverCpfPreenchido() {
        when(autorRepository.existsByEmail(autorEstrangeiroComCpfRequestDTO.getEmail())).thenReturn(false);

        NegocioException exception = assertThrows(NegocioException.class, () -> {
            autorService.cadastrarAutor(autorEstrangeiroComCpfRequestDTO);
        });
        assertEquals("Autores estrangeiros não devem possuir CPF.", exception.getMessage());

        verify(autorRepository).existsByEmail(autorEstrangeiroComCpfRequestDTO.getEmail());

        verifyNoMoreInteractions(autorRepository);
        verifyNoInteractions(autorMapper);
    }

    // TESTES DE SUCESSO: buscarAutorPorId

    @Test
    void deveBuscarAutorPorIdComSucesso() {
        when(autorRepository.findById(1L)).thenReturn(Optional.of(autorEntity));
        when(autorMapper.toResponseDTO(autorEntity)).thenReturn(autorResponseDTO);

        AutorResponseDTO resultado = autorService.buscarAutorPorId(1L);

        assertEquals(autorResponseDTO, resultado);

        verify(autorRepository).findById(1L);
        verify(autorMapper).toResponseDTO(autorEntity);

        verifyNoMoreInteractions(autorRepository);
        verifyNoMoreInteractions(autorMapper);
    }

    // TESTES DE FALHA: buscarAutorPorId

    @Test
    void deveLancarExcecaoQuandoBuscarAutorPorIdInexistente() {

        when(autorRepository.findById(1L)).thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(RecursoNaoEncontradoException.class, () -> {
            autorService.buscarAutorPorId(1L);
        });
        assertEquals("Autor não encontrado com id: 1", exception.getMessage());

        verify(autorRepository).findById(1L);

        verifyNoMoreInteractions(autorRepository);
        verifyNoInteractions(autorMapper);

    }

    // TESTES DE SUCESSO: listarAutores

    @Test
    void deveListarTodosOsAutoresComSucesso() {

        when(autorRepository.findAll()).thenReturn(listaAutorEntity);
        when(autorMapper.toResponseDTOList(listaAutorEntity)).thenReturn(listaAutorResponseDTO);

        List<AutorResponseDTO> resultado = autorService.listarAutores();

        assertEquals(listaAutorResponseDTO, resultado);

        verify(autorRepository).findAll();
        verify(autorMapper).toResponseDTOList(listaAutorEntity);

        verifyNoMoreInteractions(autorRepository);
        verifyNoMoreInteractions(autorMapper);
    }

    // TESTES DE SUCESSO: atualizarAutor

    @Test
    void deveAtualizarAutorComSucesso() {

        when(autorRepository.findById(1L)).thenReturn(Optional.of(autorEntity));
        when(autorRepository.existsByEmailAndIdNot(autorEntity.getEmail(), 1L)).thenReturn(false);
        when(autorRepository.existsByCpfAndIdNot(autorEntity.getCpf(), 1L)).thenReturn(false);
        when(autorRepository.save(autorEntity)).thenReturn(autorEntity);
        when(autorMapper.toResponseDTO(autorEntity)).thenReturn(autorResponseDTO);

        AutorResponseDTO resultado = autorService.atualizarAutor(1L, autorRequestDTO);

        assertEquals(autorResponseDTO, resultado);

        verify(autorRepository).findById(1L);
        verify(autorMapper).updateAutorFromDTO(autorRequestDTO, autorEntity);
        verify(autorRepository).existsByEmailAndIdNot(autorEntity.getEmail(), 1L);
        verify(autorRepository).existsByCpfAndIdNot(autorEntity.getCpf(), 1L);
        verify(autorRepository).save(autorEntity);
        verify(autorMapper).toResponseDTO(autorEntity);

        verifyNoMoreInteractions(autorRepository);
        verifyNoMoreInteractions(autorMapper);
    }

    // TESTES DE FALHA: atualizarAutor

    @Test
    void deveLancarExcecaoAoAtualizarAutorInexistente() {

        when(autorRepository.findById(1L)).thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(RecursoNaoEncontradoException.class, () -> {
            autorService.atualizarAutor(1L, autorRequestDTO);
        });
        assertEquals("Autor não encontrado com id: 1", exception.getMessage());

        verify(autorRepository).findById(1L);

        verifyNoMoreInteractions(autorRepository);
        verifyNoInteractions(autorMapper);
    }

    @Test
    void deveLancarExcecaoAoAtualizarQuandoEmailJaPertencerAOutroAutor() {

        when(autorRepository.findById(1L)).thenReturn(Optional.of(autorEntity));
        when(autorRepository.existsByEmailAndIdNot(autorEntity.getEmail(), 1L)).thenReturn(true);

        NegocioException exception = assertThrows(NegocioException.class, () -> {
            autorService.atualizarAutor(1L, autorRequestDTO);
        });
        assertEquals("Este e-mail já está em uso por outro autor.", exception.getMessage());

        verify(autorRepository).findById(1L);
        verify(autorMapper).updateAutorFromDTO(autorRequestDTO, autorEntity);
        verify(autorRepository).existsByEmailAndIdNot(autorEntity.getEmail(), 1L);

        verifyNoMoreInteractions(autorRepository);
        verifyNoMoreInteractions(autorMapper);
    }

    // TESTES DE SUCESSO: deletarAutor

    @Test
    void deveDeletarAutorComSucessoQuandoNaoPossuirObras() {

        when(autorRepository.findById(1L)).thenReturn(Optional.of(autorEntity));
        autorService.deletarAutor(1L);

        verify(autorRepository).findById(1L);
        verify(autorRepository).delete(autorEntity);

        verifyNoMoreInteractions(autorRepository);
        verifyNoInteractions(autorMapper);
    }

    // TESTES DE FALHA: deletarAutor

    @Test
    void deveLancarExcecaoAoDeletarAutorInexistente() {

        when(autorRepository.findById(1L)).thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(RecursoNaoEncontradoException.class, () -> {
            autorService.deletarAutor(1L);
        });
        assertEquals("Autor não encontrado com id: 1", exception.getMessage());

        verify(autorRepository).findById(1L);

        verifyNoMoreInteractions(autorRepository);
        verifyNoInteractions(autorMapper);
    }

    @Test
    void deveLancarExcecaoAoDeletarAutorComObrasVinculadas() {

        when(autorRepository.findById(1L)).thenReturn(Optional.of(autorComObraEntity));

        NegocioException exception = assertThrows(NegocioException.class, () -> {
            autorService.deletarAutor(1L);
        });
        assertEquals("Não é permitido excluir um autor que possui obras vinculadas.", exception.getMessage());

        verify(autorRepository).findById(1L);

        verifyNoMoreInteractions(autorRepository);
        verifyNoInteractions(autorMapper);
    }
}

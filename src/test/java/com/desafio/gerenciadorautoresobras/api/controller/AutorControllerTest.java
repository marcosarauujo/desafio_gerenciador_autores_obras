package com.desafio.gerenciadorautoresobras.api.controller;

import com.desafio.gerenciadorautoresobras.api.request.AutorRequestDTOFixture;
import com.desafio.gerenciadorautoresobras.api.response.AutorResponseDTOFixture;
import com.desafio.gerenciadorautoresobras.business.AutorService;
import com.desafio.gerenciadorautoresobras.business.dto.request.AutorRequestDTO;
import com.desafio.gerenciadorautoresobras.business.dto.response.AutorResponseDTO;
import com.desafio.gerenciadorautoresobras.controller.AutorController;
import com.desafio.gerenciadorautoresobras.infrastructure.enums.SexoEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class AutorControllerTest {

    @InjectMocks
    AutorController autorController;

    @Mock
    AutorService autorService;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private String url;
    private String jsonRequest;

    AutorRequestDTO autorRequestDTO;
    AutorResponseDTO autorResponseDTO;

    List<AutorResponseDTO> listaAutorResponseDTO;

    LocalDate dataNascimento;

    @BeforeEach
    public void setup() throws Exception {

        objectMapper.registerModule(new JavaTimeModule());

        mockMvc = MockMvcBuilders.standaloneSetup(autorController)
                .alwaysDo(print())
                .build();
        url = "/autores";
        dataNascimento = LocalDate.of(1839, 6, 21);

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

        listaAutorResponseDTO = Collections.singletonList(autorResponseDTO);

        jsonRequest = objectMapper.writeValueAsString(autorRequestDTO);
    }

    // TESTES DO MÉTODO: POST /autores (Cadastrar)

    @Test
    void deveCadastrarAutorComSucesso() throws Exception {

        when(autorService.cadastrarAutor(autorRequestDTO)).thenReturn(autorResponseDTO);

        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
        ).andExpect(status().isCreated());

        verify(autorService).cadastrarAutor(autorRequestDTO);
        verifyNoMoreInteractions(autorService);
    }

    @Test
    void naoDeveCadastrarAutorQuandoBodyEstiverVazio() throws Exception {

        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("")
        ).andExpect(status().isBadRequest());

        verifyNoInteractions(autorService);
    }

    // TESTES DO MÉTODO: GET /autores (Listar)

    @Test
    void deveListarTodosOsAutoresComSucesso() throws Exception {

        when(autorService.listarAutores()).thenReturn(listaAutorResponseDTO);
        mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        verify(autorService).listarAutores();
        verifyNoMoreInteractions(autorService);
    }

    // TESTES DO MÉTODO: GET /autores/{id} (Buscar por ID)

    @Test
    void deveBuscarAutorPorIdComSucesso() throws Exception {

        when(autorService.buscarAutorPorId(1L)).thenReturn(autorResponseDTO);

        mockMvc.perform(get(url + "/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        verify(autorService).buscarAutorPorId(1L);
        verifyNoMoreInteractions(autorService);
    }

    // TESTES DO MÉTODO: PUT /autores/{id} (Atualizar)

    @Test
    void deveAtualizarAutorComSucesso() throws Exception {

        when(autorService.atualizarAutor(1L, autorRequestDTO)).thenReturn(autorResponseDTO);

        mockMvc.perform(put(url + "/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
        ).andExpect(status().isOk());

        verify(autorService).atualizarAutor(1L, autorRequestDTO);
        verifyNoMoreInteractions(autorService);
    }

    // TESTES DO MÉTODO: DELETE /autores/{id} (Deletar)

    @Test
    void deveDeletarAutorComSucesso() throws Exception {

        doNothing().when(autorService).deletarAutor(1L);

        mockMvc.perform(delete(url + "/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent());

        verify(autorService).deletarAutor(1L);
        verifyNoMoreInteractions(autorService);
    }
}



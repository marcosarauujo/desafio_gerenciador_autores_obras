package com.desafio.gerenciadorautoresobras.api.controller;

import com.desafio.gerenciadorautoresobras.api.request.ObraRequestDTOFixture;
import com.desafio.gerenciadorautoresobras.api.response.AutorResponseDTOFixture;
import com.desafio.gerenciadorautoresobras.api.response.ObraResponseDTOFixture;
import com.desafio.gerenciadorautoresobras.business.ObraService;
import com.desafio.gerenciadorautoresobras.business.dto.request.ObraRequestDTO;
import com.desafio.gerenciadorautoresobras.business.dto.response.AutorResponseDTO;
import com.desafio.gerenciadorautoresobras.business.dto.response.ObraResponseDTO;
import com.desafio.gerenciadorautoresobras.controller.ObraController;
import com.desafio.gerenciadorautoresobras.infrastructure.enums.SexoEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@ExtendWith(MockitoExtension.class)
public class ObraControllerTest {

    @InjectMocks
    ObraController
            obraController;
    @Mock
    ObraService
            obraService;
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private String url;
    private String jsonRequest;

    ObraRequestDTO obraRequestDTO;
    ObraResponseDTO obraResponseDTO;
    AutorResponseDTO autorResponseDTO;
    List<ObraResponseDTO> listaObraResponseDTO;
    LocalDate dataPublicacao;

    @BeforeEach
    public void setup() throws Exception {
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(obraController)
                .alwaysDo(print())
                .build();
        url = "/obras";
        dataPublicacao = LocalDate.of(1899, 1, 1);
        autorResponseDTO = AutorResponseDTOFixture.build(
                1L,
                "Machado de Assis",
                SexoEnum.MASCULINO,
                "machado@email.com",
                LocalDate.of(1839, 6, 21),
                "Brasil",
                "12345678909"
        );
        obraRequestDTO = ObraRequestDTOFixture.build(
                "Dom Casmurro",
                "Memórias de Bento Santiago sobre Capitu",
                dataPublicacao,
                null,
                Collections.singletonList(1L)
        );
        obraResponseDTO = ObraResponseDTOFixture.build(
                1L,
                "Dom Casmurro",
                "Memórias de Bento Santiago sobre Capitu",
                dataPublicacao,
                null,
                Collections.singletonList(autorResponseDTO)
        );
        listaObraResponseDTO = Collections.singletonList(obraResponseDTO);
        jsonRequest = objectMapper.writeValueAsString(obraRequestDTO);
    }

    // TESTES DO MÉTODO: POST /obras (Cadastrar)

    @Test
    void deveCadastrarObraComSucesso() throws Exception {

        when(obraService.cadastrarObra(obraRequestDTO)).thenReturn(obraResponseDTO);
        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
        ).andExpect(status().isCreated());

        verify(obraService).cadastrarObra(obraRequestDTO);
        verifyNoMoreInteractions(obraService);
    }

    @Test
    void naoDeveCadastrarObraQuandoBodyEstiverVazio() throws Exception {

        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("")
        ).andExpect(status().isBadRequest());

        verifyNoInteractions(obraService);
    }

    // TESTES DO MÉTODO: GET /obras (Listar)

    @Test
    void deveListarTodasAsObrasComSucesso() throws Exception {

        when(obraService.listarObras()).thenReturn(listaObraResponseDTO);

        mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
        verify(obraService).listarObras();
        verifyNoMoreInteractions(obraService);
    }

    // TESTES DO MÉTODO: GET /obras/{id} (Buscar por ID)

    @Test
    void deveBuscarObraPorIdComSucesso() throws Exception {

        when(obraService.buscarObraPorId(1L)).thenReturn(obraResponseDTO);

        mockMvc.perform(get(url + "/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        verify(obraService).buscarObraPorId(1L);
        verifyNoMoreInteractions(obraService);
    }

    // TESTES DO MÉTODO: PUT /obras/{id} (Atualizar)

    @Test
    void deveAtualizarObraComSucesso() throws Exception {

        when(obraService.atualizarObra(1L, obraRequestDTO)).thenReturn(obraResponseDTO);

        mockMvc.perform(put(url + "/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
        ).andExpect(status().isOk());

        verify(obraService).atualizarObra(1L, obraRequestDTO);
        verifyNoMoreInteractions(obraService);
    }

    // TESTES DO MÉTODO: DELETE /obras/{id} (Deletar)

    @Test
    void deveDeletarObraComSucesso() throws Exception {

        doNothing().when(obraService).deletarObra(1L);

        mockMvc.perform(delete(url + "/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent());

        verify(obraService).deletarObra(1L);
        verifyNoMoreInteractions(obraService);
    }
}

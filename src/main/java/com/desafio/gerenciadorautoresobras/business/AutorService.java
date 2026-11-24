package com.desafio.gerenciadorautoresobras.business;

import com.desafio.gerenciadorautoresobras.business.dto.request.AutorRequestDTO;
import com.desafio.gerenciadorautoresobras.business.dto.response.AutorResponseDTO;
import com.desafio.gerenciadorautoresobras.business.mapper.AutorMapper;
import com.desafio.gerenciadorautoresobras.infrastructure.entities.Autor;
import com.desafio.gerenciadorautoresobras.infrastructure.exceptions.NegocioException;
import com.desafio.gerenciadorautoresobras.infrastructure.exceptions.RecursoNaoEncontradoException;
import com.desafio.gerenciadorautoresobras.infrastructure.repository.AutorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AutorService {

    private final AutorRepository autorRepository;
    private final AutorMapper autorMapper;

    @Transactional
    public AutorResponseDTO cadastrarAutor(AutorRequestDTO dto) {
        validarDadosAutor(dto);
        Autor autor = autorMapper.toEntity(dto);
        return autorMapper.toResponseDTO(autorRepository.save(autor)
        );
    }

    private void validarDadosAutor(AutorRequestDTO dto) {
        if (autorRepository.existsByEmail(dto.getEmail())) {
            throw new NegocioException("Já existe um autor cadastrado com este e-mail: " + dto.getEmail());
        }
        boolean brasileiro = "Brasil".equalsIgnoreCase(dto.getPaisOrigem());
        if (brasileiro) {
            if (dto.getCpf() == null || dto.getCpf().isBlank()) {
                throw new NegocioException("Para autores do Brasil, o CPF é obrigatório.");
            }
            if (!isCpfValido(dto.getCpf())) {
                throw new NegocioException("O CPF informado é inválido.");
            }
            if (autorRepository.existsByCpf(dto.getCpf())) {
                throw new NegocioException("Já existe um autor cadastrado com este CPF.");
            }
        } else {
            // Se NÃO for Brasil, não pode ter CPF informado
            if (dto.getCpf() != null && !dto.getCpf().isBlank()) {
                throw new NegocioException("Autores estrangeiros não devem possuir CPF.");
            }
        }
    }


    private boolean isCpfValido(String cpf) {
        if (cpf == null) return false;

        String limpo = cpf.replaceAll("\\D", "");
        if (limpo.length() != 11) return false;
        if (limpo.matches("(\\d)\\1{10}")) return false;

        try {
            int soma = 0;
            for (int i = 0; i < 9; i++) {
                soma += (limpo.charAt(i) - '0') * (10 - i);
            }
            int digito1 = 11 - (soma % 11);
            if (digito1 >= 10) digito1 = 0;
            if (digito1 != (limpo.charAt(9) - '0')) return false;

            soma = 0;
            for (int i = 0; i < 10; i++) {
                soma += (limpo.charAt(i) - '0') * (11 - i);
            }
            int digito2 = 11 - (soma % 11);
            if (digito2 >= 10) digito2 = 0;
            return digito2 == (limpo.charAt(10) - '0');
        } catch (Exception e) {
            return false;
        }
    }

    public AutorResponseDTO buscarAutorPorId(Long id) {
        return autorRepository.findById(id)
                .map(autorMapper::toResponseDTO)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Autor não encontrado com id: " + id));
    }

    public List<AutorResponseDTO> listarAutores() {
        return autorMapper.toResponseDTOList(autorRepository.findAll());
    }

    @Transactional
    public AutorResponseDTO atualizarAutor(Long id, AutorRequestDTO dto) {

        Autor autorExistente = autorRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Autor não encontrado com id: " + id));

        autorMapper.updateAutorFromDTO(dto, autorExistente);
        validarDadosAutorAtualizado(id, autorExistente);
        return autorMapper.toResponseDTO(autorRepository.save(autorExistente));
    }

    private void validarDadosAutorAtualizado(Long id, Autor autor) {
        autorRepository.findByEmail(autor.getEmail()).ifPresent(outro -> {
            if (!outro.getId().equals(id)) {
                throw new NegocioException("Este e-mail já está em uso por outro autor.");
            }
        });
        if ("Brasil".equalsIgnoreCase(autor.getPaisOrigem())) {
            if (autor.getCpf() == null || autor.getCpf().isBlank()) {
                throw new NegocioException("Para autores do Brasil, o CPF é obrigatório.");
            }
            if (!isCpfValido(autor.getCpf())) {
                throw new NegocioException("O CPF informado é inválido.");
            }
        } else {
            autor.setCpf(null);
        }
    }

    @Transactional
    public void deletarAutor(Long id) {
        Autor autor = autorRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Autor não encontrado com id: " + id));

        if (autor.getObras() != null && !autor.getObras().isEmpty()) {
            throw new NegocioException("Não é permitido excluir um autor que possui obras vinculadas.");
        }
        autorRepository.delete(autor);
    }

}




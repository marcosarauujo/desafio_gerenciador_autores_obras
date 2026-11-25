package com.desafio.gerenciadorautoresobras.business;

import br.com.caelum.stella.validation.CPFValidator;
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
        if ("Brasil".equalsIgnoreCase(dto.getPaisOrigem())) {
            if (dto.getCpf() == null || dto.getCpf().isBlank()) {
                throw new NegocioException("Para autores do Brasil, o CPF é obrigatório.");
            }
            isCpfValido(dto.getCpf());

            if (autorRepository.existsByCpf(dto.getCpf())) {
                throw new NegocioException("Já existe um autor cadastrado com este CPF.");
            }
        } else if (dto.getCpf() != null && !dto.getCpf().isBlank()) {
            throw new NegocioException("Autores estrangeiros não devem possuir CPF.");
        }
    }

    private boolean isCpfValido(String cpf) {
        if (cpf == null) {
            throw new NegocioException("O CPF não pode ser nulo.");
        }
        try {
            new CPFValidator().assertValid(cpf);
            return true;
        } catch (Exception e) {
            throw new NegocioException("O CPF informado é inválido.");
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
        if (autorRepository.existsByEmailAndIdNot(autor.getEmail(), id)) {
            throw new NegocioException("Este e-mail já está em uso por outro autor.");
        }
        if ("Brasil".equalsIgnoreCase(autor.getPaisOrigem())) {
            if (autor.getCpf() == null || autor.getCpf().isBlank()) {
                throw new NegocioException("Para autores do Brasil, o CPF é obrigatório.");
            }
            isCpfValido(autor.getCpf());

            if (autorRepository.existsByCpfAndIdNot(autor.getCpf(), id)) {
                throw new NegocioException("Este CPF já está em uso por outro autor.");
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




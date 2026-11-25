package com.desafio.gerenciadorautoresobras.business;

import com.desafio.gerenciadorautoresobras.business.dto.request.ObraRequestDTO;
import com.desafio.gerenciadorautoresobras.business.dto.response.ObraResponseDTO;
import com.desafio.gerenciadorautoresobras.business.mapper.AutorMapper;
import com.desafio.gerenciadorautoresobras.business.mapper.ObraMapper;
import com.desafio.gerenciadorautoresobras.infrastructure.entities.Autor;
import com.desafio.gerenciadorautoresobras.infrastructure.entities.Obra;
import com.desafio.gerenciadorautoresobras.infrastructure.exceptions.NegocioException;
import com.desafio.gerenciadorautoresobras.infrastructure.exceptions.RecursoNaoEncontradoException;
import com.desafio.gerenciadorautoresobras.infrastructure.repository.AutorRepository;
import com.desafio.gerenciadorautoresobras.infrastructure.repository.ObraRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ObraService {

    private final AutorRepository autorRepository;
    private final AutorMapper autorMapper;
    private final ObraMapper obraMapper;
    private final ObraRepository obraRepository;

    @Transactional
    public ObraResponseDTO cadastrarObra(ObraRequestDTO dto) {
        List<Autor> autores = buscarEValidarAutores(dto.getAutoresIds());

        Obra obra = obraMapper.toEntity(dto);
        obra.setAutores(autores);

        return obraMapper.toResponseDTO(obraRepository.save(obra)
        );
    }

    private List<Autor> buscarEValidarAutores(List<Long> autoresIds) {
        if (autoresIds == null || autoresIds.isEmpty()) {
            throw new NegocioException("A obra deve possuir pelo menos um autor associado.");
        }
        List<Autor> autoresEncontrados = autorRepository.findAllById(autoresIds);
        if (autoresEncontrados.size() != autoresIds.size()) {
            throw new NegocioException("Um ou mais autores informados não foram encontrados no sistema.");
        }
        return autoresEncontrados;
    }

    public ObraResponseDTO buscarObraPorId(Long id) {
        return obraRepository.findById(id)
                .map(obraMapper::toResponseDTO)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Obra não encontrada com id: " + id));
    }

    public List<ObraResponseDTO> listarObras() {
        return obraMapper.toResponseDTOList(obraRepository.findAll());
    }

    @Transactional
    public ObraResponseDTO atualizarObra(Long id, ObraRequestDTO dto) {
        Obra obraExistente = obraRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Obra não encontrada com id: " + id));
        obraMapper.updateObraFromDTO(dto, obraExistente);

        if (dto.getAutoresIds() != null && !dto.getAutoresIds().isEmpty()) {
            List<Autor> novosAutores = buscarEValidarAutores(dto.getAutoresIds());
            obraExistente.setAutores(novosAutores);
        }
        return obraMapper.toResponseDTO(obraRepository.save(obraExistente)
        );
    }

    @Transactional
    public void deletarObra(Long id) {
        Obra obra = obraRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Obra não encontrada com id: " + id));
        obraRepository.delete(obra);
    }
}

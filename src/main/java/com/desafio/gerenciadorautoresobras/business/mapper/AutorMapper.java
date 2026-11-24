package com.desafio.gerenciadorautoresobras.business.mapper;

import com.desafio.gerenciadorautoresobras.business.dto.request.AutorRequestDTO;
import com.desafio.gerenciadorautoresobras.business.dto.response.AutorResponseDTO;
import com.desafio.gerenciadorautoresobras.infrastructure.entities.Autor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AutorMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "obras", ignore = true)
    Autor toEntity(AutorRequestDTO dto);

    AutorResponseDTO toResponseDTO(Autor entity);

    List<AutorResponseDTO> toResponseDTOList(List<Autor> entities);
}

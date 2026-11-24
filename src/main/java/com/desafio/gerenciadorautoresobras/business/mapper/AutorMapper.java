package com.desafio.gerenciadorautoresobras.business.mapper;

import com.desafio.gerenciadorautoresobras.business.dto.request.AutorRequestDTO;
import com.desafio.gerenciadorautoresobras.business.dto.response.AutorResponseDTO;
import com.desafio.gerenciadorautoresobras.infrastructure.entities.Autor;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AutorMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "obras", ignore = true)
    Autor toEntity(AutorRequestDTO dto);

    AutorResponseDTO toResponseDTO(Autor entity);

    List<AutorResponseDTO> toResponseDTOList(List<Autor> entities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "obras", ignore = true)
    void updateAutorFromDTO(AutorRequestDTO dto, @MappingTarget Autor entity);

}

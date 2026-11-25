package com.desafio.gerenciadorautoresobras.business.mapper;

import com.desafio.gerenciadorautoresobras.business.dto.request.ObraRequestDTO;
import com.desafio.gerenciadorautoresobras.business.dto.response.ObraResponseDTO;
import com.desafio.gerenciadorautoresobras.infrastructure.entities.Obra;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AutorMapper.class})
public interface ObraMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "autores", ignore = true)
    Obra toEntity(ObraRequestDTO dto);

    ObraResponseDTO toResponseDTO(Obra entity);

    List<ObraResponseDTO> toResponseDTOList(List<Obra> entities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "autores", ignore = true)
    void updateObraFromDTO(ObraRequestDTO dto, @MappingTarget Obra entity);
}

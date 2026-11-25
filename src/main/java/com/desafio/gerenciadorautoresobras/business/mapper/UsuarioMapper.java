package com.desafio.gerenciadorautoresobras.business.mapper;

import com.desafio.gerenciadorautoresobras.business.dto.request.UsuarioRequestDTO;
import com.desafio.gerenciadorautoresobras.business.dto.response.UsuarioResponseDTO;
import com.desafio.gerenciadorautoresobras.infrastructure.entities.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(target = "id", ignore = true)
    Usuario toEntity(UsuarioRequestDTO dto);

    UsuarioResponseDTO toResponseDTO(Usuario entity);
}
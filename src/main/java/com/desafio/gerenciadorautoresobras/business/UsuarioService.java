package com.desafio.gerenciadorautoresobras.business;

import com.desafio.gerenciadorautoresobras.business.dto.request.UsuarioRequestDTO;
import com.desafio.gerenciadorautoresobras.business.dto.response.UsuarioResponseDTO;
import com.desafio.gerenciadorautoresobras.business.mapper.UsuarioMapper;
import com.desafio.gerenciadorautoresobras.infrastructure.entities.Usuario;
import com.desafio.gerenciadorautoresobras.infrastructure.exceptions.NegocioException;
import com.desafio.gerenciadorautoresobras.infrastructure.exceptions.UnauthorizedException;
import com.desafio.gerenciadorautoresobras.infrastructure.repository.UsuarioRepository;
import com.desafio.gerenciadorautoresobras.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public UsuarioResponseDTO salvaUsuario(UsuarioRequestDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new NegocioException("E-mail já cadastrado: " + dto.getEmail());
        }
        dto.setSenha(passwordEncoder.encode(dto.getSenha()));
        return usuarioMapper.toResponseDTO(
                usuarioRepository.save(usuarioMapper.toEntity(dto)
                )
        );
    }
    public String autenticadorLogin(UsuarioRequestDTO dto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getSenha())
            );
            return "Bearer " + jwtUtil.generateToken(authentication.getName());
        } catch (BadCredentialsException | UsernameNotFoundException e) {
            throw new UnauthorizedException("Usuário ou senha inválidos.");
        }
    }
}

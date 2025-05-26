package com.skar.usuario.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.skar.usuario.dto.ApiRespuestaDto;
import com.skar.usuario.dto.ApiRespuestaEstados;
import com.skar.usuario.dto.RegistracionUsuarioDto;
import com.skar.usuario.exception.ErrorLogicaServicioUsuarioException;
import com.skar.usuario.exception.UsuarioYaExisteException;
import com.skar.usuario.model.Usuario;
import com.skar.usuario.repository.RepositorioUsuario;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UsuarioServiceImp implements UsuarioService {

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    public ResponseEntity<ApiRespuestaDto> registrarUsuario(RegistracionUsuarioDto nuevoUsuarioDto)
            throws UsuarioYaExisteException, ErrorLogicaServicioUsuarioException {

        try {
            if (repositorioUsuario.findByEmail(nuevoUsuarioDto.getEmail()) != null) {
                throw new UsuarioYaExisteException("El usuario ya existe con el email: " + nuevoUsuarioDto.getEmail());
            }

            Usuario nuevoUsuario = nuevoUsuarioDto.convertirDtoAUsuario(nuevoUsuarioDto);
            repositorioUsuario.save(nuevoUsuario);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiRespuestaDto(ApiRespuestaEstados.EXITO, "Usuario registrado exitosamente"));

        } catch (UsuarioYaExisteException e) {
            throw new UsuarioYaExisteException(e.getMessage());
        } catch (Exception e) {
            log.error("Error al registrar el usuario: {}", e.getMessage());
            throw new ErrorLogicaServicioUsuarioException(e.getMessage());
        }

    }

}

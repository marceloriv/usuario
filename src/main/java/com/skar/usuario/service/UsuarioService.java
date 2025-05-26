package com.skar.usuario.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.skar.usuario.dto.ApiRespuestaDto;
import com.skar.usuario.dto.RegistracionUsuarioDto;
import com.skar.usuario.exception.ErrorLogicaServicioUsuarioException;
import com.skar.usuario.exception.UsuarioYaExisteException;

@Service
public interface UsuarioService {

    ResponseEntity<ApiRespuestaDto> registrarUsuario(RegistracionUsuarioDto nuevoUsuarioDto)
            throws UsuarioYaExisteException, ErrorLogicaServicioUsuarioException;

}

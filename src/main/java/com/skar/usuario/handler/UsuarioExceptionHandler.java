package com.skar.usuario.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.skar.usuario.dto.ApiRespuestaDto;
import com.skar.usuario.dto.ApiRespuestaEstados;
import com.skar.usuario.exception.UsuarioNoEncontradoException;
import com.skar.usuario.exception.UsuarioYaExisteException;

@RestControllerAdvice
public class UsuarioExceptionHandler {

    @ExceptionHandler(value = UsuarioNoEncontradoException.class)
    public ResponseEntity<ApiRespuestaDto> usuarioNoEncontradoExceptionHandler(UsuarioNoEncontradoException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiRespuestaDto(ApiRespuestaEstados.ERROR, exception.getMessage()));
    }

    @ExceptionHandler(value = UsuarioYaExisteException.class)
    public ResponseEntity<ApiRespuestaDto> usuarioYaExisteExceptionHandler(UsuarioYaExisteException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiRespuestaDto(ApiRespuestaEstados.ERROR, exception.getMessage()));
    }

}

package com.skar.usuario.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skar.usuario.service.UsuarioService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.skar.usuario.dto.ApiRespuestaDto;
import com.skar.usuario.dto.RegistracionUsuarioDto;
import com.skar.usuario.exception.ErrorLogicaServicioUsuarioException;
import com.skar.usuario.exception.UsuarioYaExisteException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<ApiRespuestaDto> registrarUsuario(@Valid @RequestBody RegistracionUsuarioDto nuevoUsuarioDto)
            throws UsuarioYaExisteException, ErrorLogicaServicioUsuarioException {
        return usuarioService.registrarUsuario(nuevoUsuarioDto);

    }
}

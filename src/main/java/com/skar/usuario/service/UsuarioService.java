package com.skar.usuario.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.skar.usuario.dto.ApiRespuestaDto;
import com.skar.usuario.dto.RegistracionUsuarioDto;
import com.skar.usuario.exception.ErrorLogicaServicioUsuarioException;
import com.skar.usuario.exception.UsuarioYaExisteException;
import com.skar.usuario.model.Usuario;

@Service
public interface UsuarioService {

    ResponseEntity<ApiRespuestaDto> registrarUsuario(RegistracionUsuarioDto nuevoUsuarioDto)
            throws UsuarioYaExisteException, ErrorLogicaServicioUsuarioException;

    ResponseEntity<Object> obtenerUsuarioPorEmail(String email)
            throws ErrorLogicaServicioUsuarioException;

    ResponseEntity<Object> obtenerUsuarioPorTelefono(String telefono)
            throws ErrorLogicaServicioUsuarioException;

    ResponseEntity<Object> obtenerUsuarioPorId(Long id)
            throws ErrorLogicaServicioUsuarioException;

    ResponseEntity<Object> obtenerUsuarioPorNombre(String nombre)
            throws ErrorLogicaServicioUsuarioException;

    ResponseEntity<Object> obtenerPorEstado(Boolean estado)
            throws ErrorLogicaServicioUsuarioException;

    ResponseEntity<Object> obtenerTodosLosUsuarios()
            throws ErrorLogicaServicioUsuarioException;

    ResponseEntity<Object> actualizarUsuario(Long id, Usuario usuarioActualizado)
            throws ErrorLogicaServicioUsuarioException;

    ResponseEntity<Object> eliminarUsuario(Long id)
            throws ErrorLogicaServicioUsuarioException;

    ResponseEntity<Object> cambiarEstadoUsuario(Long id, Boolean estado)
            throws ErrorLogicaServicioUsuarioException;

    ResponseEntity<Object> actualizarUsuarioPorId(Long id, RegistracionUsuarioDto usuarioDto)
            throws ErrorLogicaServicioUsuarioException, UsuarioYaExisteException;

}

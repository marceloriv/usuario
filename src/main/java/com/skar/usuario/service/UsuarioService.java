package com.skar.usuario.service;

import java.util.List;


import com.skar.usuario.dto.RegistracionUsuarioDto;
import com.skar.usuario.exception.ErrorLogicaServicioUsuarioException;
import com.skar.usuario.exception.UsuarioYaExisteException;
import com.skar.usuario.exception.UsuarioNoEncontradoException;
import com.skar.usuario.model.Usuario;

public interface UsuarioService {

    Usuario registrarUsuario(RegistracionUsuarioDto nuevoUsuarioDto)
            throws UsuarioYaExisteException, ErrorLogicaServicioUsuarioException;

    Usuario obtenerUsuarioPorEmail(String email)
            throws UsuarioNoEncontradoException, ErrorLogicaServicioUsuarioException;

    Usuario obtenerUsuarioPorTelefono(String telefono)
            throws UsuarioNoEncontradoException, ErrorLogicaServicioUsuarioException;

    Usuario obtenerUsuarioPorId(Long id)
            throws UsuarioNoEncontradoException, ErrorLogicaServicioUsuarioException;

    List<Usuario> obtenerUsuarioPorNombre(String nombre)
            throws ErrorLogicaServicioUsuarioException; // Lista vacía posible

    List<Usuario> obtenerPorEstado(Boolean estado)
            throws ErrorLogicaServicioUsuarioException; // Lista vacía posible

    List<Usuario> obtenerTodosLosUsuarios() throws ErrorLogicaServicioUsuarioException; // Lista vacía posible

    Usuario actualizarUsuario(Long id, Usuario usuarioActualizado)
            throws UsuarioNoEncontradoException, ErrorLogicaServicioUsuarioException;

    void eliminarUsuario(Long id) throws UsuarioNoEncontradoException, ErrorLogicaServicioUsuarioException;

    Usuario cambiarEstadoUsuario(Long id, Boolean estado)
            throws UsuarioNoEncontradoException, ErrorLogicaServicioUsuarioException;

    Usuario actualizarUsuarioPorId(Long id, RegistracionUsuarioDto usuarioDto)
            throws UsuarioNoEncontradoException, UsuarioYaExisteException, ErrorLogicaServicioUsuarioException;

    Usuario login(String email, String contrasena)
            throws UsuarioNoEncontradoException, ErrorLogicaServicioUsuarioException;

}

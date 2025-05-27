package com.skar.usuario.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skar.usuario.dto.ApiRespuestaDto;
import com.skar.usuario.dto.RegistracionUsuarioDto;
import com.skar.usuario.exception.ErrorLogicaServicioUsuarioException;
import com.skar.usuario.exception.UsuarioYaExisteException;
import com.skar.usuario.model.Usuario;
import com.skar.usuario.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Usuarios", description = "Operaciones relacionadas con la gestión de usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    @Operation(summary = "Registrar un nuevo usuario", description = "Permite registrar un nuevo usuario en el sistema")
    public ResponseEntity<ApiRespuestaDto> registrarUsuario(@Valid @RequestBody RegistracionUsuarioDto nuevoUsuarioDto)
            throws UsuarioYaExisteException, ErrorLogicaServicioUsuarioException {
        return usuarioService.registrarUsuario(nuevoUsuarioDto);
    }

    @GetMapping("/{mail}")
    @Operation(summary = "Obtener usuario por email", description = "Permite obtener un usuario a partir de su email")
    public ResponseEntity<Object> obtenerUsuarioPorEmail(@PathVariable String mail) throws ErrorLogicaServicioUsuarioException {
        return usuarioService.obtenerUsuarioPorEmail(mail);
    }

    @GetMapping("/telefono/{telefono}")
    @Operation(summary = "Obtener usuario por teléfono", description = "Permite obtener un usuario a partir de su número de teléfono")
    public ResponseEntity<Object> obtenerUsuarioPorTelefono(@PathVariable String telefono) throws ErrorLogicaServicioUsuarioException {
        return usuarioService.obtenerUsuarioPorTelefono(telefono);
    }

    @GetMapping("/id/{id}")
    @Operation(summary = "Obtener usuario por ID", description = "Permite obtener un usuario a partir de su ID")
    public ResponseEntity<Object> obtenerUsuarioPorId(@PathVariable Long id) throws ErrorLogicaServicioUsuarioException {
        return usuarioService.obtenerUsuarioPorId(id);
    }

    @GetMapping("/nombre/{nombre}")
    @Operation(summary = "Obtener usuario por nombre", description = "Permite obtener usuarios a partir de su nombre")
    public ResponseEntity<Object> obtenerUsuarioPorNombre(@PathVariable String nombre) throws ErrorLogicaServicioUsuarioException {
        return usuarioService.obtenerUsuarioPorNombre(nombre);
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Obtener usuarios por estado", description = "Permite obtener usuarios activos o inactivos")
    public ResponseEntity<Object> obtenerPorEstado(@PathVariable Boolean estado) throws ErrorLogicaServicioUsuarioException {
        return usuarioService.obtenerPorEstado(estado);
    }

    @GetMapping
    @Operation(summary = "Obtener todos los usuarios", description = "Permite obtener todos los usuarios registrados")
    public ResponseEntity<Object> obtenerTodosLosUsuarios() throws ErrorLogicaServicioUsuarioException {
        return usuarioService.obtenerTodosLosUsuarios();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario", description = "Permite actualizar los datos de un usuario existente")
    public ResponseEntity<Object> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuarioActualizado)
            throws ErrorLogicaServicioUsuarioException {
        return usuarioService.actualizarUsuario(id, usuarioActualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario", description = "Permite eliminar un usuario a partir de su ID")
    public ResponseEntity<Object> eliminarUsuario(@PathVariable Long id) throws ErrorLogicaServicioUsuarioException {
        return usuarioService.eliminarUsuario(id);
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Cambiar estado del usuario", description = "Permite cambiar el estado (activo/inactivo) de un usuario")
    public ResponseEntity<Object> cambiarEstadoUsuario(@PathVariable Long id, @RequestBody Boolean estado)
            throws ErrorLogicaServicioUsuarioException {
        return usuarioService.cambiarEstadoUsuario(id, estado);
    }

    @PutMapping("/{id}/actualizar")
    @Operation(summary = "Actualizar usuario por ID", description = "Permite actualizar los datos de un usuario a partir de su ID")
    public ResponseEntity<Object> actualizarUsuarioPorId(@PathVariable Long id, @RequestBody RegistracionUsuarioDto usuarioDto)
            throws ErrorLogicaServicioUsuarioException, UsuarioYaExisteException {
        return usuarioService.actualizarUsuarioPorId(id, usuarioDto);
    }
}

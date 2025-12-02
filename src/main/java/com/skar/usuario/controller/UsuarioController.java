package com.skar.usuario.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
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
import com.skar.usuario.dto.ApiRespuestaEstados;
import com.skar.usuario.dto.UsuarioRespuestaDto;
import com.skar.usuario.exception.UsuarioNoEncontradoException;
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
    public ResponseEntity<UsuarioRespuestaDto> registrarUsuario(
            @Valid @RequestBody RegistracionUsuarioDto nuevoUsuarioDto)
            throws UsuarioYaExisteException, ErrorLogicaServicioUsuarioException {
        Usuario usuario = usuarioService.registrarUsuario(nuevoUsuarioDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioRespuestaDto.from(usuario));
    }

    @GetMapping("/{mail}")
    @Operation(summary = "Obtener usuario por email", description = "Permite obtener un usuario a partir de su email")
    public ResponseEntity<UsuarioRespuestaDto> obtenerUsuarioPorEmail(@PathVariable String mail)
            throws ErrorLogicaServicioUsuarioException, UsuarioNoEncontradoException {
        Usuario usuario = usuarioService.obtenerUsuarioPorEmail(mail);
        return ResponseEntity.ok(UsuarioRespuestaDto.from(usuario));
    }

    @GetMapping("/telefono/{telefono}")
    @Operation(summary = "Obtener usuario por teléfono", description = "Permite obtener un usuario a partir de su número de teléfono")
    public ResponseEntity<UsuarioRespuestaDto> obtenerUsuarioPorTelefono(@PathVariable String telefono)
            throws ErrorLogicaServicioUsuarioException, UsuarioNoEncontradoException {
        Usuario usuario = usuarioService.obtenerUsuarioPorTelefono(telefono);
        return ResponseEntity.ok(UsuarioRespuestaDto.from(usuario));
    }

    @GetMapping("/id/{id}")
    @Operation(summary = "Obtener usuario por ID", description = "Permite obtener un usuario a partir de su ID")
    public ResponseEntity<UsuarioRespuestaDto> obtenerUsuarioPorId(@PathVariable Long id)
            throws ErrorLogicaServicioUsuarioException, UsuarioNoEncontradoException {
        Usuario usuario = usuarioService.obtenerUsuarioPorId(id);
        return ResponseEntity.ok(UsuarioRespuestaDto.from(usuario));
    }

    @GetMapping("/nombre/{nombre}")
    @Operation(summary = "Obtener usuario por nombre", description = "Permite obtener usuarios a partir de su nombre")
    public ResponseEntity<List<UsuarioRespuestaDto>> obtenerUsuarioPorNombre(@PathVariable String nombre)
            throws ErrorLogicaServicioUsuarioException {
        var usuarios = usuarioService.obtenerUsuarioPorNombre(nombre).stream().map(UsuarioRespuestaDto::from).toList();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Obtener usuarios por estado", description = "Permite obtener usuarios activos o inactivos")
    public ResponseEntity<List<UsuarioRespuestaDto>> obtenerPorEstado(@PathVariable Boolean estado)
            throws ErrorLogicaServicioUsuarioException {
        var usuarios = usuarioService.obtenerPorEstado(estado).stream().map(UsuarioRespuestaDto::from).toList();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping
    @Operation(summary = "Obtener todos los usuarios", description = "Permite obtener todos los usuarios registrados")
    public ResponseEntity<List<UsuarioRespuestaDto>> obtenerTodosLosUsuarios() throws ErrorLogicaServicioUsuarioException {
        var usuarios = usuarioService.obtenerTodosLosUsuarios().stream().map(UsuarioRespuestaDto::from).toList();
        return ResponseEntity.ok(usuarios);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario", description = "Permite actualizar los datos de un usuario existente")
    public ResponseEntity<UsuarioRespuestaDto> actualizarUsuario(@PathVariable Long id,
            @RequestBody Usuario usuarioActualizado)
            throws ErrorLogicaServicioUsuarioException, UsuarioNoEncontradoException {
        Usuario actualizado = usuarioService.actualizarUsuario(id, usuarioActualizado);
        return ResponseEntity.ok(UsuarioRespuestaDto.from(actualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario", description = "Permite eliminar un usuario a partir de su ID")
    public ResponseEntity<ApiRespuestaDto> eliminarUsuario(@PathVariable Long id)
            throws ErrorLogicaServicioUsuarioException, UsuarioNoEncontradoException {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.ok(new ApiRespuestaDto(ApiRespuestaEstados.EXITO, "Usuario eliminado exitosamente"));
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Cambiar estado del usuario", description = "Permite cambiar el estado (activo/inactivo) de un usuario")
    public ResponseEntity<UsuarioRespuestaDto> cambiarEstadoUsuario(@PathVariable Long id, @RequestBody Boolean estado)
            throws ErrorLogicaServicioUsuarioException, UsuarioNoEncontradoException {
        Usuario usuario = usuarioService.cambiarEstadoUsuario(id, estado);
        return ResponseEntity.ok(UsuarioRespuestaDto.from(usuario));
    }

    @PutMapping("/{id}/actualizar")
    @Operation(summary = "Actualizar usuario por ID", description = "Permite actualizar los datos de un usuario a partir de su ID")
    public ResponseEntity<UsuarioRespuestaDto> actualizarUsuarioPorId(@PathVariable Long id,
            @RequestBody RegistracionUsuarioDto usuarioDto)
            throws ErrorLogicaServicioUsuarioException, UsuarioYaExisteException, UsuarioNoEncontradoException {
        Usuario usuario = usuarioService.actualizarUsuarioPorId(id, usuarioDto);
        return ResponseEntity.ok(UsuarioRespuestaDto.from(usuario));
    }
}

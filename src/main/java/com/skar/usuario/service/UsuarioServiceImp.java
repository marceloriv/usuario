package com.skar.usuario.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.skar.usuario.dto.ApiRespuestaDto;
import com.skar.usuario.dto.ApiRespuestaEstados;
import com.skar.usuario.dto.RegistracionUsuarioDto;
import com.skar.usuario.exception.ErrorLogicaServicioUsuarioException;
import com.skar.usuario.exception.UsuarioYaExisteException;
import com.skar.usuario.model.Usuario;
import com.skar.usuario.repository.RepositorioUsuario;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UsuarioServiceImp implements UsuarioService {

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    @Override
    public ResponseEntity<ApiRespuestaDto> registrarUsuario(RegistracionUsuarioDto nuevoUsuarioDto)
            throws UsuarioYaExisteException, ErrorLogicaServicioUsuarioException {

        try {
            if (repositorioUsuario.findByEmail(nuevoUsuarioDto.getEmail()) != null) {
                throw new UsuarioYaExisteException("El usuario ya existe con el email: " + nuevoUsuarioDto.getEmail());
            }

            Usuario nuevoUsuario = nuevoUsuarioDto.convertirDtoAUsuario();
            repositorioUsuario.save(nuevoUsuario);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiRespuestaDto(ApiRespuestaEstados.EXITO, "Usuario registrado exitosamente"));

        } catch (UsuarioYaExisteException e) {
            // Re-lanzar la excepción para que sea manejada por el GlobalExceptionHandler
            throw e;
        } catch (Exception e) {
            log.error("Error al registrar el usuario: {}", e.getMessage());
            throw new ErrorLogicaServicioUsuarioException(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> obtenerUsuarioPorEmail(String email) throws ErrorLogicaServicioUsuarioException {
        try {
            Usuario usuario = repositorioUsuario.findByEmail(email);
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiRespuestaDto(ApiRespuestaEstados.ERROR,
                                "Usuario no encontrado con el email: " + email));
            }
            return ResponseEntity.ok(usuario); // Devuelve el usuario encontrado
        } catch (Exception e) {
            throw new ErrorLogicaServicioUsuarioException(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> obtenerUsuarioPorTelefono(String telefono)
            throws ErrorLogicaServicioUsuarioException {
        try {
            Optional<Usuario> usuarioOpt = repositorioUsuario.findByTelefono(telefono);
            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiRespuestaDto(ApiRespuestaEstados.ERROR,
                                "Usuario no encontrado con el teléfono: " + telefono));
            }
            return ResponseEntity.ok(usuarioOpt.get());
        } catch (Exception e) {
            throw new ErrorLogicaServicioUsuarioException(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> obtenerUsuarioPorId(Long id) throws ErrorLogicaServicioUsuarioException {
        try {
            Optional<Usuario> usuarioOpt = repositorioUsuario.findById(id);
            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiRespuestaDto(ApiRespuestaEstados.ERROR, "Usuario no encontrado con el ID: " + id));
            }
            return ResponseEntity.ok(usuarioOpt.get());
        } catch (Exception e) {
            throw new ErrorLogicaServicioUsuarioException(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> obtenerUsuarioPorNombre(String nombre) throws ErrorLogicaServicioUsuarioException {
        try {
            List<Usuario> usuarios = repositorioUsuario.findByNombre(nombre);
            if (usuarios.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiRespuestaDto(ApiRespuestaEstados.ERROR,
                                "No se encontraron usuarios con el nombre: " + nombre));
            }
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            throw new ErrorLogicaServicioUsuarioException(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> obtenerPorEstado(Boolean estado) throws ErrorLogicaServicioUsuarioException {
        try {
            List<Usuario> usuarios = repositorioUsuario.findByEstado(estado);
            if (usuarios.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiRespuestaDto(ApiRespuestaEstados.ERROR,
                                "No se encontraron usuarios con el estado: " + estado));
            }
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            throw new ErrorLogicaServicioUsuarioException(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> obtenerTodosLosUsuarios() throws ErrorLogicaServicioUsuarioException {
        try {
            List<Usuario> usuarios = repositorioUsuario.findAll();
            if (usuarios.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiRespuestaDto(ApiRespuestaEstados.ERROR, "No se encontraron usuarios"));
            }
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            throw new ErrorLogicaServicioUsuarioException(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> actualizarUsuario(Long id, Usuario usuarioActualizado)
            throws ErrorLogicaServicioUsuarioException {
        try {
            Optional<Usuario> usuarioOpt = repositorioUsuario.findById(id);
            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiRespuestaDto(ApiRespuestaEstados.ERROR, "Usuario no encontrado con el ID: " + id));
            }
            usuarioActualizado.setId(id);
            repositorioUsuario.save(usuarioActualizado);
            return ResponseEntity
                    .ok(new ApiRespuestaDto(ApiRespuestaEstados.EXITO, "Usuario actualizado exitosamente"));
        } catch (Exception e) {
            throw new ErrorLogicaServicioUsuarioException(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> eliminarUsuario(Long id) throws ErrorLogicaServicioUsuarioException {
        try {
            Optional<Usuario> usuarioOpt = repositorioUsuario.findById(id);
            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiRespuestaDto(ApiRespuestaEstados.ERROR, "Usuario no encontrado con el ID: " + id));
            }
            repositorioUsuario.delete(usuarioOpt.get());
            return ResponseEntity.ok(new ApiRespuestaDto(ApiRespuestaEstados.EXITO, "Usuario eliminado exitosamente"));
        } catch (Exception e) {
            throw new ErrorLogicaServicioUsuarioException(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> cambiarEstadoUsuario(Long id, Boolean estado)
            throws ErrorLogicaServicioUsuarioException {
        try {
            Optional<Usuario> usuarioOpt = repositorioUsuario.findById(id);
            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiRespuestaDto(ApiRespuestaEstados.ERROR, "Usuario no encontrado con el ID: " + id));
            }
            Usuario usuario = usuarioOpt.get();
            usuario.setEstado(estado);
            repositorioUsuario.save(usuario);
            return ResponseEntity
                    .ok(new ApiRespuestaDto(ApiRespuestaEstados.EXITO, "Estado del usuario actualizado exitosamente"));
        } catch (Exception e) {
            throw new ErrorLogicaServicioUsuarioException(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> actualizarUsuarioPorId(Long id, RegistracionUsuarioDto usuarioDto)
            throws ErrorLogicaServicioUsuarioException, UsuarioYaExisteException {
        try {
            Optional<Usuario> usuarioOpt = repositorioUsuario.findById(id);
            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiRespuestaDto(ApiRespuestaEstados.ERROR, "Usuario no encontrado con el ID: " + id));
            }

            Usuario usuario = usuarioOpt.get();
            if (!usuario.getEmail().equals(usuarioDto.getEmail())
                    && repositorioUsuario.findByEmail(usuarioDto.getEmail()) != null) {
                throw new UsuarioYaExisteException("El email ya está en uso: " + usuarioDto.getEmail());
            }

            Usuario usuarioActualizado = usuarioDto.convertirDtoAUsuario();
            usuarioActualizado.setId(id);
            repositorioUsuario.save(usuarioActualizado);
            return ResponseEntity
                    .ok(new ApiRespuestaDto(ApiRespuestaEstados.EXITO, "Usuario actualizado exitosamente"));
        } catch (UsuarioYaExisteException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiRespuestaDto(ApiRespuestaEstados.ERROR, e.getMessage()));
        } catch (Exception e) {
            throw new ErrorLogicaServicioUsuarioException(e.getMessage());
        }
    }

}

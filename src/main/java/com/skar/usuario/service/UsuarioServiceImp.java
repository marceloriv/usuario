package com.skar.usuario.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.skar.usuario.dto.RegistracionUsuarioDto;
import com.skar.usuario.exception.ErrorLogicaServicioUsuarioException;
import com.skar.usuario.exception.UsuarioNoEncontradoException;
import com.skar.usuario.exception.UsuarioYaExisteException;
import com.skar.usuario.model.Usuario;
import com.skar.usuario.repository.RepositorioUsuario;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UsuarioServiceImp implements UsuarioService {

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Usuario registrarUsuario(RegistracionUsuarioDto nuevoUsuarioDto)
            throws UsuarioYaExisteException, ErrorLogicaServicioUsuarioException {
        try {
            if (repositorioUsuario.findByEmail(nuevoUsuarioDto.getEmail()) != null) {
                throw new UsuarioYaExisteException("El usuario ya existe con el email: " + nuevoUsuarioDto.getEmail());
            }
            Usuario nuevoUsuario = nuevoUsuarioDto.convertirDtoAUsuario();
            // Hash de contraseña
            if (nuevoUsuario.getContrasena() != null) {
                nuevoUsuario.setContrasena(passwordEncoder.encode(nuevoUsuario.getContrasena()));
            }
            return repositorioUsuario.save(nuevoUsuario);
        } catch (UsuarioYaExisteException e) {
            throw e;
        } catch (DataIntegrityViolationException e) {
            throw new ErrorLogicaServicioUsuarioException("Violación de integridad de datos: " + e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            log.error("Error al registrar el usuario: {}", e.getMessage());
            throw new ErrorLogicaServicioUsuarioException(e.getMessage());
        }
    }

    @Override
    public Usuario obtenerUsuarioPorEmail(String email) throws UsuarioNoEncontradoException, ErrorLogicaServicioUsuarioException {
        try {
            Usuario usuario = repositorioUsuario.findByEmail(email);
            if (usuario == null) {
                throw new UsuarioNoEncontradoException("Usuario no encontrado con el email: " + email);
            }
            return usuario;
        } catch (UsuarioNoEncontradoException e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorLogicaServicioUsuarioException(e.getMessage());
        }
    }

    @Override
    public Usuario obtenerUsuarioPorTelefono(String telefono)
            throws UsuarioNoEncontradoException, ErrorLogicaServicioUsuarioException {
        try {
            Optional<Usuario> usuarioOpt = repositorioUsuario.findByTelefono(telefono);
            if (usuarioOpt.isEmpty()) {
                throw new UsuarioNoEncontradoException("Usuario no encontrado con el teléfono: " + telefono);
            }
            return usuarioOpt.get();
        } catch (UsuarioNoEncontradoException e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorLogicaServicioUsuarioException(e.getMessage());
        }
    }

    @Override
    public Usuario obtenerUsuarioPorId(Long id) throws UsuarioNoEncontradoException, ErrorLogicaServicioUsuarioException {
        try {
            Optional<Usuario> usuarioOpt = repositorioUsuario.findById(id);
            if (usuarioOpt.isEmpty()) {
                throw new UsuarioNoEncontradoException("Usuario no encontrado con el ID: " + id);
            }
            return usuarioOpt.get();
        } catch (UsuarioNoEncontradoException e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorLogicaServicioUsuarioException(e.getMessage());
        }
    }

    @Override
    public List<Usuario> obtenerUsuarioPorNombre(String nombre) throws ErrorLogicaServicioUsuarioException {
        try {
            return repositorioUsuario.findByNombre(nombre);
        } catch (Exception e) {
            throw new ErrorLogicaServicioUsuarioException(e.getMessage());
        }
    }

    @Override
    public List<Usuario> obtenerPorEstado(Boolean estado) throws ErrorLogicaServicioUsuarioException {
        try {
            return repositorioUsuario.findByEstado(estado);
        } catch (Exception e) {
            throw new ErrorLogicaServicioUsuarioException(e.getMessage());
        }
    }

    @Override
    public List<Usuario> obtenerTodosLosUsuarios() throws ErrorLogicaServicioUsuarioException {
        try {
            return repositorioUsuario.findAll();
        } catch (Exception e) {
            throw new ErrorLogicaServicioUsuarioException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public Usuario actualizarUsuario(Long id, Usuario usuarioActualizado)
            throws UsuarioNoEncontradoException, ErrorLogicaServicioUsuarioException {
        try {
            Optional<Usuario> usuarioOpt = repositorioUsuario.findById(id);
            if (usuarioOpt.isEmpty()) {
                throw new UsuarioNoEncontradoException("Usuario no encontrado con el ID: " + id);
            }
            Usuario existente = usuarioOpt.get();
            // Solo actualizar campos permitidos (simplificado)
            existente.setNombre(usuarioActualizado.getNombre());
            existente.setApellidos(usuarioActualizado.getApellidos());
            if (usuarioActualizado.getTelefono() != null && !usuarioActualizado.getTelefono().equals(existente.getTelefono())) {
                // Validar duplicado de telefono
                if (usuarioActualizado.getTelefono() != null && repositorioUsuario.findByTelefono(usuarioActualizado.getTelefono()).isPresent()) {
                    throw new ErrorLogicaServicioUsuarioException("Teléfono ya está en uso: " + usuarioActualizado.getTelefono());
                }
                existente.setTelefono(usuarioActualizado.getTelefono());
            }
            existente.setDireccion(usuarioActualizado.getDireccion());
            existente.setRol(usuarioActualizado.getRol());
            if (usuarioActualizado.getContrasena() != null && !usuarioActualizado.getContrasena().isBlank()) {
                existente.setContrasena(passwordEncoder.encode(usuarioActualizado.getContrasena()));
            }
            return repositorioUsuario.save(existente);
        } catch (UsuarioNoEncontradoException e) {
            throw e;
        } catch (DataIntegrityViolationException e) {
            throw new ErrorLogicaServicioUsuarioException("Violación de integridad: " + e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            throw new ErrorLogicaServicioUsuarioException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public void eliminarUsuario(Long id) throws UsuarioNoEncontradoException, ErrorLogicaServicioUsuarioException {
        try {
            Optional<Usuario> usuarioOpt = repositorioUsuario.findById(id);
            if (usuarioOpt.isEmpty()) {
                throw new UsuarioNoEncontradoException("Usuario no encontrado con el ID: " + id);
            }
            repositorioUsuario.delete(usuarioOpt.get());
        } catch (UsuarioNoEncontradoException e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorLogicaServicioUsuarioException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public Usuario cambiarEstadoUsuario(Long id, Boolean estado)
            throws UsuarioNoEncontradoException, ErrorLogicaServicioUsuarioException {
        try {
            Optional<Usuario> usuarioOpt = repositorioUsuario.findById(id);
            if (usuarioOpt.isEmpty()) {
                throw new UsuarioNoEncontradoException("Usuario no encontrado con el ID: " + id);
            }
            Usuario usuario = usuarioOpt.get();
            usuario.setEstado(estado);
            return repositorioUsuario.save(usuario);
        } catch (UsuarioNoEncontradoException e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorLogicaServicioUsuarioException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public Usuario actualizarUsuarioPorId(Long id, RegistracionUsuarioDto usuarioDto)
            throws UsuarioNoEncontradoException, UsuarioYaExisteException, ErrorLogicaServicioUsuarioException {
        try {
            Optional<Usuario> usuarioOpt = repositorioUsuario.findById(id);
            if (usuarioOpt.isEmpty()) {
                throw new UsuarioNoEncontradoException("Usuario no encontrado con el ID: " + id);
            }
            Usuario usuario = usuarioOpt.get();
            if (!usuario.getEmail().equals(usuarioDto.getEmail())
                    && repositorioUsuario.findByEmail(usuarioDto.getEmail()) != null) {
                throw new UsuarioYaExisteException("El email ya está en uso: " + usuarioDto.getEmail());
            }
            Usuario usuarioActualizado = usuarioDto.convertirDtoAUsuario();
            usuarioActualizado.setId(id);
            // Preservar contraseña existente si DTO no trae nueva
            if (usuarioDto.getContrasena() == null || usuarioDto.getContrasena().isBlank()) {
                usuarioActualizado.setContrasena(usuario.getContrasena());
            } else {
                usuarioActualizado.setContrasena(passwordEncoder.encode(usuarioDto.getContrasena()));
            }
            return repositorioUsuario.save(usuarioActualizado);
        } catch (UsuarioNoEncontradoException | UsuarioYaExisteException e) {
            throw e;
        } catch (DataIntegrityViolationException e) {
            throw new ErrorLogicaServicioUsuarioException("Violación de integridad: " + e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            throw new ErrorLogicaServicioUsuarioException(e.getMessage());
        }
    }

    @Override
    public Usuario login(String email, String contrasena)
            throws UsuarioNoEncontradoException, ErrorLogicaServicioUsuarioException {
        try {
            Usuario usuario = repositorioUsuario.findByEmail(email);
            if (usuario == null) {
                throw new UsuarioNoEncontradoException("Credenciales inválidas");
            }
            if (!passwordEncoder.matches(contrasena, usuario.getContrasena())) {
                throw new UsuarioNoEncontradoException("Credenciales inválidas");
            }
            return usuario;
        } catch (UsuarioNoEncontradoException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al iniciar sesión: {}", e.getMessage());
            throw new ErrorLogicaServicioUsuarioException(e.getMessage());
        }
    }

}

package com.skar.usuario.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.skar.usuario.dto.RegistracionUsuarioDto;
import com.skar.usuario.exception.UsuarioNoEncontradoException;
import com.skar.usuario.exception.UsuarioYaExisteException;
import com.skar.usuario.model.Rol;
import com.skar.usuario.model.Usuario;
import com.skar.usuario.repository.RepositorioUsuario;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @InjectMocks
    private UsuarioServiceImp usuarioService;

    @Mock
    private RepositorioUsuario repositorioUsuario;

    @Spy
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private Usuario usuario;
    private RegistracionUsuarioDto registracionUsuarioDto;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Juan");
        usuario.setApellidos("Lopez Garcia");
        usuario.setEmail("juanito@gmail.com");
        usuario.setContrasena("Juan062505");
        usuario.setTelefono("12345678901");
        usuario.setDireccion("Alameda Ospicio 56");
        usuario.setRol(Rol.EMPLEADO);
        usuario.setEstado(true);

        registracionUsuarioDto = new RegistracionUsuarioDto();
        registracionUsuarioDto.setNombre("Juan");
        registracionUsuarioDto.setApellidos("Lopez Garcia");
        registracionUsuarioDto.setEmail("juanito@gmail.com");
        registracionUsuarioDto.setContrasena("Juan062505");
        registracionUsuarioDto.setTelefono("12345678901");
        registracionUsuarioDto.setDireccion("Alameda Ospicio 56");
        registracionUsuarioDto.setRol(Rol.EMPLEADO);
        registracionUsuarioDto.setEstado(true);
    }

    @Test
    void testRegistrarUsuario_Exitoso() throws Exception {
        when(repositorioUsuario.findByEmail(registracionUsuarioDto.getEmail())).thenReturn(null);
        // Devolver exactamente la instancia que se guarda (ya con contraseña hasheada)
        when(repositorioUsuario.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario resultado = usuarioService.registrarUsuario(registracionUsuarioDto);

        assertNotNull(resultado);
        assertEquals(usuario.getEmail(), resultado.getEmail());
        assertNotEquals(registracionUsuarioDto.getContrasena(), resultado.getContrasena());
        assertTrue(new BCryptPasswordEncoder().matches(registracionUsuarioDto.getContrasena(), resultado.getContrasena())); // encoder bean usado en servicio

        verify(repositorioUsuario, times(1)).findByEmail(registracionUsuarioDto.getEmail());
        verify(repositorioUsuario, times(1)).save(any(Usuario.class));
    }

    @Test
    void testRegistrarUsuario_UsuarioYaExiste() {
        when(repositorioUsuario.findByEmail(registracionUsuarioDto.getEmail())).thenReturn(usuario);
        assertThrows(UsuarioYaExisteException.class, () -> usuarioService.registrarUsuario(registracionUsuarioDto));
        verify(repositorioUsuario, times(1)).findByEmail(registracionUsuarioDto.getEmail());
    }

    @Test
    void testObtenerUsuarioPorEmail_UsuarioEncontrado() throws Exception {
        String email = "juanito@gmail.com";
        when(repositorioUsuario.findByEmail(email)).thenReturn(usuario);
        Usuario res = usuarioService.obtenerUsuarioPorEmail(email);
        assertNotNull(res);
        assertEquals(email, res.getEmail());
        verify(repositorioUsuario, times(1)).findByEmail(email);
    }

    @Test
    void testObtenerUsuarioPorEmail_UsuarioNoEncontrado() {
        String email = "noexiste@gmail.com";
        when(repositorioUsuario.findByEmail(email)).thenReturn(null);
        assertThrows(UsuarioNoEncontradoException.class, () -> usuarioService.obtenerUsuarioPorEmail(email));
        verify(repositorioUsuario, times(1)).findByEmail(email);
    }

    @Test
    void testObtenerUsuarioPorTelefono_UsuarioEncontrado() throws Exception {
        String telefono = "12345678901";
        when(repositorioUsuario.findByTelefono(telefono)).thenReturn(Optional.of(usuario));
        Usuario res = usuarioService.obtenerUsuarioPorTelefono(telefono);
        assertEquals(telefono, res.getTelefono());
        verify(repositorioUsuario, times(1)).findByTelefono(telefono);
    }

    @Test
    void testObtenerUsuarioPorId_UsuarioEncontrado() throws Exception {
        Long id = 1L;
        when(repositorioUsuario.findById(id)).thenReturn(Optional.of(usuario));
        Usuario res = usuarioService.obtenerUsuarioPorId(id);
        assertEquals(id, res.getId());
        verify(repositorioUsuario, times(1)).findById(id);
    }

    @Test
    void testObtenerUsuarioPorNombre_UsuariosEncontrados() throws Exception {
        String nombre = "Juan";
        List<Usuario> usuarios = Arrays.asList(usuario);
        when(repositorioUsuario.findByNombre(nombre)).thenReturn(usuarios);
        List<Usuario> res = usuarioService.obtenerUsuarioPorNombre(nombre);
        assertFalse(res.isEmpty());
        verify(repositorioUsuario, times(1)).findByNombre(nombre);
    }

    @Test
    void testObtenerPorEstado_UsuariosEncontrados() throws Exception {
        Boolean estado = true;
        List<Usuario> usuarios = Arrays.asList(usuario);
        when(repositorioUsuario.findByEstado(estado)).thenReturn(usuarios);
        List<Usuario> res = usuarioService.obtenerPorEstado(estado);
        assertFalse(res.isEmpty());
        verify(repositorioUsuario, times(1)).findByEstado(estado);
    }

    @Test
    void testObtenerTodosLosUsuarios_UsuariosEncontrados() throws Exception {
        List<Usuario> usuarios = Arrays.asList(usuario);
        when(repositorioUsuario.findAll()).thenReturn(usuarios);
        List<Usuario> res = usuarioService.obtenerTodosLosUsuarios();
        assertFalse(res.isEmpty());
        verify(repositorioUsuario, times(1)).findAll();
    }

    @Test
    void testActualizarUsuario_Exitoso() throws Exception {
        Long id = 1L;
        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setNombre("Juan Carlos");
        when(repositorioUsuario.findById(id)).thenReturn(Optional.of(usuario));
        when(repositorioUsuario.save(any(Usuario.class))).thenReturn(usuario);
        Usuario res = usuarioService.actualizarUsuario(id, usuarioActualizado);
        assertNotNull(res);
        verify(repositorioUsuario, times(1)).findById(id);
        verify(repositorioUsuario, times(1)).save(any(Usuario.class));
    }

    @Test
    void testEliminarUsuario_Exitoso() throws Exception {
        Long id = 1L;
        when(repositorioUsuario.findById(id)).thenReturn(Optional.of(usuario));
        assertDoesNotThrow(() -> usuarioService.eliminarUsuario(id));
        verify(repositorioUsuario, times(1)).findById(id);
        verify(repositorioUsuario, times(1)).delete(usuario);
    }

    @Test
    void testCambiarEstadoUsuario_Exitoso() throws Exception {
        Long id = 1L;
        Boolean nuevoEstado = false;
        when(repositorioUsuario.findById(id)).thenReturn(Optional.of(usuario));
        when(repositorioUsuario.save(any(Usuario.class))).thenReturn(usuario);
        Usuario res = usuarioService.cambiarEstadoUsuario(id, nuevoEstado);
        assertNotNull(res);
        verify(repositorioUsuario, times(1)).findById(id);
        verify(repositorioUsuario, times(1)).save(any(Usuario.class));
    }

    @Test
    void testActualizarUsuarioPorId_Exitoso() throws Exception {
        Long id = 1L;
        RegistracionUsuarioDto usuarioDto = new RegistracionUsuarioDto();
        usuarioDto.setNombre("Juan Carlos");
        usuarioDto.setApellidos("Lopez Garcia");
        usuarioDto.setEmail("juanito@gmail.com");
        usuarioDto.setContrasena("Juan062505");
        usuarioDto.setTelefono("12345678901");
        usuarioDto.setDireccion("Alameda Ospicio 56");
        usuarioDto.setRol(Rol.EMPLEADO);
        usuarioDto.setEstado(true);
        when(repositorioUsuario.findById(id)).thenReturn(Optional.of(usuario));
        when(repositorioUsuario.save(any(Usuario.class))).thenReturn(usuario);
        Usuario res = usuarioService.actualizarUsuarioPorId(id, usuarioDto);
        assertNotNull(res);
        verify(repositorioUsuario, times(1)).findById(id);
        verify(repositorioUsuario, times(1)).save(any(Usuario.class));
    }
}

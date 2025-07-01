package com.skar.usuario.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.skar.usuario.dto.ApiRespuestaDto;
import com.skar.usuario.dto.ApiRespuestaEstados;
import com.skar.usuario.dto.RegistracionUsuarioDto;
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
        usuario.setRol("EMPLEADO");
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

    // =================================================================================
    // Tests mínimos para registrarUsuario
    // =================================================================================
    @Test
    void testRegistrarUsuario_Exitoso() throws Exception {
        when(repositorioUsuario.findByEmail(registracionUsuarioDto.getEmail())).thenReturn(null);
        when(repositorioUsuario.save(any(Usuario.class))).thenReturn(usuario);

        ResponseEntity<ApiRespuestaDto> response = usuarioService.registrarUsuario(registracionUsuarioDto);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        if (response.getBody() != null && response.getBody().getEstado() != null) {
            ApiRespuestaDto responseBody = response.getBody();
            assertEquals(ApiRespuestaEstados.EXITO, responseBody.getEstado());
        }

        verify(repositorioUsuario, times(1)).findByEmail(registracionUsuarioDto.getEmail());
        verify(repositorioUsuario, times(1)).save(any(Usuario.class));
    }

    @Test
    void testRegistrarUsuario_UsuarioYaExiste() {
        when(repositorioUsuario.findByEmail(registracionUsuarioDto.getEmail())).thenReturn(usuario);

        UsuarioYaExisteException exception = assertThrows(UsuarioYaExisteException.class, () -> {
            usuarioService.registrarUsuario(registracionUsuarioDto);
        });

        assertNotNull(exception);
        verify(repositorioUsuario, times(1)).findByEmail(registracionUsuarioDto.getEmail());
    }

    // =================================================================================
    // Tests mínimos para obtenerUsuarioPorEmail
    // =================================================================================
    @Test
    void testObtenerUsuarioPorEmail_UsuarioEncontrado() throws Exception {
        String email = "juanito@gmail.com";
        when(repositorioUsuario.findByEmail(email)).thenReturn(usuario);

        ResponseEntity<Object> response = usuarioService.obtenerUsuarioPorEmail(email);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Usuario);

        verify(repositorioUsuario, times(1)).findByEmail(email);
    }

    @Test
    void testObtenerUsuarioPorEmail_UsuarioNoEncontrado() throws Exception {
        String email = "noexiste@gmail.com";
        when(repositorioUsuario.findByEmail(email)).thenReturn(null);

        ResponseEntity<Object> response = usuarioService.obtenerUsuarioPorEmail(email);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() instanceof ApiRespuestaDto);

        verify(repositorioUsuario, times(1)).findByEmail(email);
    }

    // =================================================================================
    // Tests mínimos para obtenerUsuarioPorTelefono
    // =================================================================================
    @Test
    void testObtenerUsuarioPorTelefono_UsuarioEncontrado() throws Exception {
        String telefono = "12345678901";
        when(repositorioUsuario.findByTelefono(telefono)).thenReturn(Optional.of(usuario));

        ResponseEntity<Object> response = usuarioService.obtenerUsuarioPorTelefono(telefono);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Usuario);

        verify(repositorioUsuario, times(1)).findByTelefono(telefono);
    }

    // =================================================================================
    // Tests mínimos para obtenerUsuarioPorId
    // =================================================================================
    @Test
    void testObtenerUsuarioPorId_UsuarioEncontrado() throws Exception {
        Long id = 1L;
        when(repositorioUsuario.findById(id)).thenReturn(Optional.of(usuario));

        ResponseEntity<Object> response = usuarioService.obtenerUsuarioPorId(id);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Usuario);

        verify(repositorioUsuario, times(1)).findById(id);
    }

    // =================================================================================
    // Tests mínimos para obtenerUsuarioPorNombre
    // =================================================================================
    @Test
    void testObtenerUsuarioPorNombre_UsuariosEncontrados() throws Exception {
        String nombre = "Juan";
        List<Usuario> usuarios = Arrays.asList(usuario);
        when(repositorioUsuario.findByNombre(nombre)).thenReturn(usuarios);

        ResponseEntity<Object> response = usuarioService.obtenerUsuarioPorNombre(nombre);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof List);

        verify(repositorioUsuario, times(1)).findByNombre(nombre);
    }

    // =================================================================================
    // Tests mínimos para obtenerPorEstado
    // =================================================================================
    @Test
    void testObtenerPorEstado_UsuariosEncontrados() throws Exception {
        Boolean estado = true;
        List<Usuario> usuarios = Arrays.asList(usuario);
        when(repositorioUsuario.findByEstado(estado)).thenReturn(usuarios);

        ResponseEntity<Object> response = usuarioService.obtenerPorEstado(estado);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof List);

        verify(repositorioUsuario, times(1)).findByEstado(estado);
    }

    // =================================================================================
    // Tests mínimos para obtenerTodosLosUsuarios
    // =================================================================================
    @Test
    void testObtenerTodosLosUsuarios_UsuariosEncontrados() throws Exception {
        List<Usuario> usuarios = Arrays.asList(usuario);
        when(repositorioUsuario.findAll()).thenReturn(usuarios);

        ResponseEntity<Object> response = usuarioService.obtenerTodosLosUsuarios();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof List);

        verify(repositorioUsuario, times(1)).findAll();
    }

    // =================================================================================
    // Tests mínimos para actualizarUsuario
    // =================================================================================
    @Test
    void testActualizarUsuario_Exitoso() throws Exception {
        Long id = 1L;
        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setNombre("Juan Carlos");

        when(repositorioUsuario.findById(id)).thenReturn(Optional.of(usuario));
        when(repositorioUsuario.save(any(Usuario.class))).thenReturn(usuarioActualizado);

        ResponseEntity<Object> response = usuarioService.actualizarUsuario(id, usuarioActualizado);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof ApiRespuestaDto);

        verify(repositorioUsuario, times(1)).findById(id);
        verify(repositorioUsuario, times(1)).save(any(Usuario.class));
    }

    // =================================================================================
    // Tests mínimos para eliminarUsuario
    // =================================================================================
    @Test
    void testEliminarUsuario_Exitoso() throws Exception {
        Long id = 1L;
        when(repositorioUsuario.findById(id)).thenReturn(Optional.of(usuario));

        ResponseEntity<Object> response = usuarioService.eliminarUsuario(id);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof ApiRespuestaDto);

        verify(repositorioUsuario, times(1)).findById(id);
        verify(repositorioUsuario, times(1)).delete(usuario);
    }

    // =================================================================================
    // Tests mínimos para cambiarEstadoUsuario
    // =================================================================================
    @Test
    void testCambiarEstadoUsuario_Exitoso() throws Exception {
        Long id = 1L;
        Boolean nuevoEstado = false;
        when(repositorioUsuario.findById(id)).thenReturn(Optional.of(usuario));
        when(repositorioUsuario.save(any(Usuario.class))).thenReturn(usuario);

        ResponseEntity<Object> response = usuarioService.cambiarEstadoUsuario(id, nuevoEstado);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof ApiRespuestaDto);

        verify(repositorioUsuario, times(1)).findById(id);
        verify(repositorioUsuario, times(1)).save(any(Usuario.class));
    }

    // =================================================================================
    // Tests mínimos para actualizarUsuarioPorId
    // =================================================================================
    @Test
    void testActualizarUsuarioPorId_Exitoso() throws Exception {
        Long id = 1L;
        RegistracionUsuarioDto usuarioDto = new RegistracionUsuarioDto();
        usuarioDto.setNombre("Juan Carlos");
        usuarioDto.setApellidos("Lopez Garcia");
        usuarioDto.setEmail("juanito@gmail.com"); // Mismo email
        usuarioDto.setContrasena("Juan062505");
        usuarioDto.setTelefono("12345678901");
        usuarioDto.setDireccion("Alameda Ospicio 56");
        usuarioDto.setRol(Rol.EMPLEADO); // ← Agregar esta línea
        usuarioDto.setEstado(true); // ← Agregar esta línea

        when(repositorioUsuario.findById(id)).thenReturn(Optional.of(usuario));
        when(repositorioUsuario.save(any(Usuario.class))).thenReturn(usuario);

        ResponseEntity<Object> response = usuarioService.actualizarUsuarioPorId(id, usuarioDto);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof ApiRespuestaDto);

        verify(repositorioUsuario, times(1)).findById(id);
        verify(repositorioUsuario, times(1)).save(any(Usuario.class));
    }
}

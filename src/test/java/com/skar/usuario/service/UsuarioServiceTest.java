package com.skar.usuario.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.skar.usuario.dto.ApiRespuestaDto;
import com.skar.usuario.dto.ApiRespuestaEstados;
import com.skar.usuario.dto.RegistracionUsuarioDto;
import com.skar.usuario.exception.UsuarioYaExisteException;
import com.skar.usuario.model.Rol;
import com.skar.usuario.model.Usuario;
import com.skar.usuario.repository.RepositorioUsuario;

@SpringBootTest
public class UsuarioServiceTest {

    @Autowired
    private UsuarioService usuarioService;

    @MockitoBean
    private RepositorioUsuario repositorioUsuario;

    private Usuario usuario;

    private RegistracionUsuarioDto registracionUsuarioDto;

    @BeforeEach
    void setUp() {
        // Inicializar Usuario de prueba
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

        // Inicializar DTO de registración
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
        // Arrange: Configurar mocks para simular que el usuario no existe
        when(repositorioUsuario.findByEmail(registracionUsuarioDto.getEmail())).thenReturn(null);
        when(repositorioUsuario.save(any(Usuario.class))).thenReturn(usuario);

        // Act: Ejecutar el método a probar
        ResponseEntity<ApiRespuestaDto> response = usuarioService.registrarUsuario(registracionUsuarioDto);

        // Assert: Verificar que el resultado es el esperado
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        ApiRespuestaDto responseBody = response.getBody();
        assertEquals(ApiRespuestaEstados.EXITO, responseBody.getEstado());
        assertEquals("Usuario registrado exitosamente", responseBody.getMensaje());

        // Verificar que se llamaron los métodos del repositorio
        verify(repositorioUsuario, times(1)).findByEmail(registracionUsuarioDto.getEmail());
        verify(repositorioUsuario, times(1)).save(any(Usuario.class));
    }

    @Test
    void testRegistrarUsuario_UsuarioYaExiste() {
        // Arrange: Configurar mock para simular que el usuario ya existe
        when(repositorioUsuario.findByEmail(registracionUsuarioDto.getEmail())).thenReturn(usuario);

        // Act & Assert: Verificar que se lanza la excepción esperada
        UsuarioYaExisteException exception = assertThrows(UsuarioYaExisteException.class, () -> {
            usuarioService.registrarUsuario(registracionUsuarioDto);
        });

        // Verificar el mensaje de la excepción
        assertEquals("El usuario ya existe con el email: " + registracionUsuarioDto.getEmail(),
                exception.getMessage());

        // Verificar que se llamó al método de búsqueda pero no al de guardar
        verify(repositorioUsuario, times(1)).findByEmail(registracionUsuarioDto.getEmail());
        verify(repositorioUsuario, never()).save(any(Usuario.class));
    }

    @Test
    void testObtenerUsuarioPorEmail_UsuarioEncontrado() throws Exception {
        // Arrange: Configurar mock para simular que se encuentra el usuario
        String email = "juanito@gmail.com";
        when(repositorioUsuario.findByEmail(email)).thenReturn(usuario);

        // Act: Ejecutar el método a probar
        ResponseEntity<Object> response = usuarioService.obtenerUsuarioPorEmail(email);

        // Assert: Verificar que el resultado es el esperado
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Usuario);
        Usuario usuarioRetornado = (Usuario) response.getBody();
        assertNotNull(usuarioRetornado);
        assertEquals(email, usuarioRetornado.getEmail());
        assertEquals("Juan", usuarioRetornado.getNombre());

        // Verificar que se llamó al método del repositorio
        verify(repositorioUsuario, times(1)).findByEmail(email);
    }

    @Test
    void testObtenerUsuarioPorEmail_UsuarioNoEncontrado() throws Exception {
        // Arrange: Configurar mock para simular que no se encuentra el usuario
        String email = "noexiste@gmail.com";
        when(repositorioUsuario.findByEmail(email)).thenReturn(null);

        // Act: Ejecutar el método a probar
        ResponseEntity<Object> response = usuarioService.obtenerUsuarioPorEmail(email);

        // Assert: Verificar que el resultado es el esperado
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ApiRespuestaDto);
        ApiRespuestaDto apiResponse = (ApiRespuestaDto) response.getBody();
        assertNotNull(apiResponse);
        assertEquals(ApiRespuestaEstados.ERROR, apiResponse.getEstado());
        assertEquals("Usuario no encontrado con el email: " + email, apiResponse.getMensaje());

        // Verificar que se llamó al método del repositorio
        verify(repositorioUsuario, times(1)).findByEmail(email);
    }
}

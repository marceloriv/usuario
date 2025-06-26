package com.skar.usuario.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skar.usuario.dto.ApiRespuestaDto;
import com.skar.usuario.dto.ApiRespuestaEstados;
import com.skar.usuario.dto.RegistracionUsuarioDto;
import com.skar.usuario.model.Rol;
import com.skar.usuario.model.Usuario;
import com.skar.usuario.service.UsuarioServiceImp;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioServiceImp usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

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
    void testRegistrarUsuario() throws Exception {
        // Crear respuesta mock del servicio
        ApiRespuestaDto respuestaEsperada = new ApiRespuestaDto(
                ApiRespuestaEstados.EXITO,
                "Usuario registrado exitosamente"
        );
        ResponseEntity<ApiRespuestaDto> responseEntity = ResponseEntity.status(HttpStatus.CREATED)
                .body(respuestaEsperada);

        // Configurar el mock del servicio
        when(usuarioService.registrarUsuario(any(RegistracionUsuarioDto.class)))
                .thenReturn(responseEntity);

        // Ejecutar la petición y verificar el resultado
        mockMvc.perform(post("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registracionUsuarioDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.estado").value("EXITO"))
                .andExpect(jsonPath("$.mensaje").value("Usuario registrado exitosamente"));
    }

    @Test
    void testRegistrarUsuario_UsuarioYaExiste() throws Exception {
        // Crear respuesta de error mock
        ApiRespuestaDto respuestaError = new ApiRespuestaDto(
                ApiRespuestaEstados.ERROR,
                "El usuario ya existe con el email: juanito@gmail.com"
        );
        ResponseEntity<ApiRespuestaDto> responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(respuestaError);

        // Configurar el mock para simular usuario existente
        when(usuarioService.registrarUsuario(any(RegistracionUsuarioDto.class)))
                .thenReturn(responseEntity);

        // Ejecutar la petición y verificar el resultado de error
        mockMvc.perform(post("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registracionUsuarioDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.estado").value("ERROR"))
                .andExpect(jsonPath("$.mensaje").value("El usuario ya existe con el email: juanito@gmail.com"));
    }

    @Test
    void testRegistrarUsuario_DatosInvalidos() throws Exception {
        // Crear DTO con datos inválidos
        RegistracionUsuarioDto dtoInvalido = new RegistracionUsuarioDto();
        dtoInvalido.setNombre(""); // Nombre vacío debería fallar la validación
        dtoInvalido.setEmail("email-invalido"); // Email inválido

        // Ejecutar la petición con datos inválidos
        mockMvc.perform(post("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoInvalido)))
                .andExpect(status().isBadRequest());
    }

    // ==================== PRUEBAS PARA ENDPOINTS GET ====================
    
    @Test
    void testObtenerUsuarioPorEmail() throws Exception {
        // Configurar respuesta mock
        ResponseEntity<Object> responseEntity = ResponseEntity.ok(usuario);
        when(usuarioService.obtenerUsuarioPorEmail("juanito@gmail.com"))
                .thenReturn(responseEntity);

        // Ejecutar la petición
        mockMvc.perform(get("/api/v1/usuarios/juanito@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan"))
                .andExpect(jsonPath("$.email").value("juanito@gmail.com"));
    }

    @Test
    void testObtenerUsuarioPorEmail_NoEncontrado() throws Exception {
        // Configurar respuesta de error
        ApiRespuestaDto respuestaError = new ApiRespuestaDto(
                ApiRespuestaEstados.ERROR,
                "Usuario no encontrado"
        );
        ResponseEntity<Object> responseEntity = ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(respuestaError);
        
        when(usuarioService.obtenerUsuarioPorEmail("noexiste@gmail.com"))
                .thenReturn(responseEntity);

        mockMvc.perform(get("/api/v1/usuarios/noexiste@gmail.com"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.estado").value("ERROR"));
    }

    @Test
    void testObtenerUsuarioPorTelefono() throws Exception {
        ResponseEntity<Object> responseEntity = ResponseEntity.ok(usuario);
        when(usuarioService.obtenerUsuarioPorTelefono("12345678901"))
                .thenReturn(responseEntity);

        mockMvc.perform(get("/api/v1/usuarios/telefono/12345678901"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.telefono").value("12345678901"));
    }

    @Test
    void testObtenerUsuarioPorId() throws Exception {
        ResponseEntity<Object> responseEntity = ResponseEntity.ok(usuario);
        when(usuarioService.obtenerUsuarioPorId(1L))
                .thenReturn(responseEntity);

        mockMvc.perform(get("/api/v1/usuarios/id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testObtenerUsuarioPorNombre() throws Exception {
        List<Usuario> usuarios = Arrays.asList(usuario);
        ResponseEntity<Object> responseEntity = ResponseEntity.ok(usuarios);
        when(usuarioService.obtenerUsuarioPorNombre("Juan"))
                .thenReturn(responseEntity);

        mockMvc.perform(get("/api/v1/usuarios/nombre/Juan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Juan"));
    }

    @Test
    void testObtenerPorEstado() throws Exception {
        List<Usuario> usuarios = Arrays.asList(usuario);
        ResponseEntity<Object> responseEntity = ResponseEntity.ok(usuarios);
        when(usuarioService.obtenerPorEstado(true))
                .thenReturn(responseEntity);

        mockMvc.perform(get("/api/v1/usuarios/estado/true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estado").value(true));
    }

    @Test
    void testObtenerTodosLosUsuarios() throws Exception {
        List<Usuario> usuarios = Arrays.asList(usuario);
        ResponseEntity<Object> responseEntity = ResponseEntity.ok(usuarios);
        when(usuarioService.obtenerTodosLosUsuarios())
                .thenReturn(responseEntity);

        mockMvc.perform(get("/api/v1/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].nombre").value("Juan"));
    }

    // ==================== PRUEBAS PARA ENDPOINTS PUT/PATCH ====================
    
    @Test
    void testActualizarUsuario() throws Exception {
        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setNombre("Juan Carlos");
        usuarioActualizado.setEmail("juanito@gmail.com");

        ApiRespuestaDto respuesta = new ApiRespuestaDto(
                ApiRespuestaEstados.EXITO,
                "Usuario actualizado exitosamente"
        );
        ResponseEntity<Object> responseEntity = ResponseEntity.ok(respuesta);
        
        when(usuarioService.actualizarUsuario(eq(1L), any(Usuario.class)))
                .thenReturn(responseEntity);

        mockMvc.perform(put("/api/v1/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("EXITO"));
    }

    @Test
    void testActualizarUsuarioPorId() throws Exception {
        ApiRespuestaDto respuesta = new ApiRespuestaDto(
                ApiRespuestaEstados.EXITO,
                "Usuario actualizado exitosamente"
        );
        ResponseEntity<Object> responseEntity = ResponseEntity.ok(respuesta);
        
        when(usuarioService.actualizarUsuarioPorId(eq(1L), any(RegistracionUsuarioDto.class)))
                .thenReturn(responseEntity);

        mockMvc.perform(put("/api/v1/usuarios/1/actualizar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registracionUsuarioDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("EXITO"));
    }

    @Test
    void testCambiarEstadoUsuario() throws Exception {
        ApiRespuestaDto respuesta = new ApiRespuestaDto(
                ApiRespuestaEstados.EXITO,
                "Estado del usuario cambiado exitosamente"
        );
        ResponseEntity<Object> responseEntity = ResponseEntity.ok(respuesta);
        
        when(usuarioService.cambiarEstadoUsuario(1L, false))
                .thenReturn(responseEntity);

        mockMvc.perform(patch("/api/v1/usuarios/1/estado")
                .contentType(MediaType.APPLICATION_JSON)
                .content("false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("EXITO"));
    }

    // ==================== PRUEBAS PARA ENDPOINTS DELETE ====================
    
    @Test
    void testEliminarUsuario() throws Exception {
        ApiRespuestaDto respuesta = new ApiRespuestaDto(
                ApiRespuestaEstados.EXITO,
                "Usuario eliminado exitosamente"
        );
        ResponseEntity<Object> responseEntity = ResponseEntity.ok(respuesta);
        
        when(usuarioService.eliminarUsuario(1L))
                .thenReturn(responseEntity);

        mockMvc.perform(delete("/api/v1/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("EXITO"));
    }

    @Test
    void testEliminarUsuario_NoEncontrado() throws Exception {
        ApiRespuestaDto respuestaError = new ApiRespuestaDto(
                ApiRespuestaEstados.ERROR,
                "Usuario no encontrado con ID: 999"
        );
        ResponseEntity<Object> responseEntity = ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(respuestaError);
        
        when(usuarioService.eliminarUsuario(999L))
                .thenReturn(responseEntity);

        mockMvc.perform(delete("/api/v1/usuarios/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.estado").value("ERROR"));
    }

    // ==================== PRUEBAS DE VALIDACIÓN ADICIONALES ====================
    
    @Test
    void testRegistrarUsuario_EmailInvalido() throws Exception {
        RegistracionUsuarioDto dtoEmailInvalido = new RegistracionUsuarioDto();
        dtoEmailInvalido.setNombre("Juan");
        dtoEmailInvalido.setEmail("email-sin-formato-valido");
        dtoEmailInvalido.setContrasena("password123");

        mockMvc.perform(post("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoEmailInvalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegistrarUsuario_ContrasenaVacia() throws Exception {
        RegistracionUsuarioDto dtoContrasenaVacia = new RegistracionUsuarioDto();
        dtoContrasenaVacia.setNombre("Juan");
        dtoContrasenaVacia.setEmail("juan@gmail.com");
        dtoContrasenaVacia.setContrasena(""); // Contraseña vacía

        mockMvc.perform(post("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoContrasenaVacia)))
                .andExpect(status().isBadRequest());
    }

    // ==================== PRUEBAS DE CASOS EDGE ====================
    
    @Test
    void testObtenerUsuarioPorId_IdInvalido() throws Exception {
        mockMvc.perform(get("/api/v1/usuarios/id/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testActualizarUsuario_IdNoExiste() throws Exception {
        ApiRespuestaDto respuestaError = new ApiRespuestaDto(
                ApiRespuestaEstados.ERROR,
                "Usuario no encontrado con ID: 999"
        );
        ResponseEntity<Object> responseEntity = ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(respuestaError);
        
        when(usuarioService.actualizarUsuario(eq(999L), any(Usuario.class)))
                .thenReturn(responseEntity);

        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setNombre("Nuevo Nombre");

        mockMvc.perform(put("/api/v1/usuarios/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioActualizado)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.estado").value("ERROR"));
    }
}

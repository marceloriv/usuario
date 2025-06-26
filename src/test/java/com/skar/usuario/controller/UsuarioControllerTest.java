package com.skar.usuario.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
}

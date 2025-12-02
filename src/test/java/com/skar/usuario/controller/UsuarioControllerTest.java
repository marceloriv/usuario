package com.skar.usuario.controller;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skar.usuario.dto.RegistracionUsuarioDto;
import com.skar.usuario.exception.UsuarioNoEncontradoException;
import com.skar.usuario.exception.UsuarioYaExisteException;
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
    void testRegistrarUsuario() throws Exception {
        when(usuarioService.registrarUsuario(any(RegistracionUsuarioDto.class))).thenReturn(usuario);
        mockMvc.perform(post("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registracionUsuarioDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Juan"))
                .andExpect(jsonPath("$.email").value("juanito@gmail.com"))
                .andExpect(jsonPath("$.contrasena").doesNotExist())
                .andExpect(jsonPath("$.rol").value("EMPLEADO"));
    }

    @Test
    void testRegistrarUsuario_UsuarioYaExiste() throws Exception {
        when(usuarioService.registrarUsuario(any(RegistracionUsuarioDto.class)))
                .thenThrow(new UsuarioYaExisteException("El usuario ya existe con el email: " + registracionUsuarioDto.getEmail()));
        mockMvc.perform(post("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registracionUsuarioDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.estado").value("ERROR"));
    }

    @Test
    void testRegistrarUsuario_DatosInvalidos() throws Exception {
        RegistracionUsuarioDto dtoInvalido = new RegistracionUsuarioDto();
        dtoInvalido.setNombre("");
        dtoInvalido.setEmail("email-invalido");
        mockMvc.perform(post("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoInvalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testObtenerUsuarioPorEmail() throws Exception {
        when(usuarioService.obtenerUsuarioPorEmail("juanito@gmail.com")).thenReturn(usuario);
        mockMvc.perform(get("/api/v1/usuarios/juanito@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan"))
                .andExpect(jsonPath("$.email").value("juanito@gmail.com"))
                .andExpect(jsonPath("$.rol").value("EMPLEADO"))
                .andExpect(jsonPath("$.contrasena").doesNotExist());
    }

    @Test
    void testObtenerUsuarioPorEmail_NoEncontrado() throws Exception {
        when(usuarioService.obtenerUsuarioPorEmail("noexiste@gmail.com"))
                .thenThrow(new UsuarioNoEncontradoException("Usuario no encontrado"));
        mockMvc.perform(get("/api/v1/usuarios/noexiste@gmail.com"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.estado").value("ERROR"));
    }

    @Test
    void testObtenerUsuarioPorTelefono() throws Exception {
        when(usuarioService.obtenerUsuarioPorTelefono("12345678901")).thenReturn(usuario);
        mockMvc.perform(get("/api/v1/usuarios/telefono/12345678901"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.telefono").value("12345678901"));
    }

    @Test
    void testObtenerUsuarioPorId() throws Exception {
        when(usuarioService.obtenerUsuarioPorId(1L)).thenReturn(usuario);
        mockMvc.perform(get("/api/v1/usuarios/id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testObtenerUsuarioPorNombre() throws Exception {
        List<Usuario> usuarios = Arrays.asList(usuario);
        when(usuarioService.obtenerUsuarioPorNombre("Juan")).thenReturn(usuarios);
        mockMvc.perform(get("/api/v1/usuarios/nombre/Juan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Juan"));
    }

    @Test
    void testObtenerPorEstado() throws Exception {
        List<Usuario> usuarios = Arrays.asList(usuario);
        when(usuarioService.obtenerPorEstado(true)).thenReturn(usuarios);
        mockMvc.perform(get("/api/v1/usuarios/estado/true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estado").value(true));
    }

    @Test
    void testObtenerTodosLosUsuarios() throws Exception {
        List<Usuario> usuarios = Arrays.asList(usuario);
        when(usuarioService.obtenerTodosLosUsuarios()).thenReturn(usuarios);
        mockMvc.perform(get("/api/v1/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].nombre").value("Juan"));
    }

    @Test
    void testActualizarUsuario() throws Exception {
        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setNombre("Juan Carlos");
        when(usuarioService.actualizarUsuario(eq(1L), any(Usuario.class))).thenReturn(usuarioActualizado);
        mockMvc.perform(put("/api/v1/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan Carlos"));
    }

    @Test
    void testActualizarUsuarioPorId() throws Exception {
        when(usuarioService.actualizarUsuarioPorId(eq(1L), any(RegistracionUsuarioDto.class))).thenReturn(usuario);
        mockMvc.perform(put("/api/v1/usuarios/1/actualizar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registracionUsuarioDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("juanito@gmail.com"));
    }

    @Test
    void testCambiarEstadoUsuario() throws Exception {
        Usuario cambiado = new Usuario();
        cambiado.setId(1L);
        cambiado.setNombre("Juan");
        cambiado.setEmail("juanito@gmail.com");
        cambiado.setEstado(false);
        when(usuarioService.cambiarEstadoUsuario(1L, false)).thenReturn(cambiado);
        mockMvc.perform(patch("/api/v1/usuarios/1/estado")
                .contentType(MediaType.APPLICATION_JSON)
                .content("false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value(false));
    }

    @Test
    void testEliminarUsuario() throws Exception {
        doNothing().when(usuarioService).eliminarUsuario(1L);
        mockMvc.perform(delete("/api/v1/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("EXITO"));
    }

    @Test
        void testEliminarUsuario_NoEncontrado() throws Exception {
                org.mockito.Mockito.doThrow(new UsuarioNoEncontradoException("Usuario no encontrado")).when(usuarioService).eliminarUsuario(999L);
        mockMvc.perform(delete("/api/v1/usuarios/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.estado").value("ERROR"));
    }

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
        dtoContrasenaVacia.setContrasena("");
        mockMvc.perform(post("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoContrasenaVacia)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testObtenerUsuarioPorId_IdInvalido() throws Exception {
        mockMvc.perform(get("/api/v1/usuarios/id/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testActualizarUsuario_IdNoExiste() throws Exception {
        when(usuarioService.actualizarUsuario(eq(999L), any(Usuario.class)))
                .thenThrow(new UsuarioNoEncontradoException("Usuario no encontrado"));
        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setNombre("Nuevo Nombre");
        mockMvc.perform(put("/api/v1/usuarios/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioActualizado)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.estado").value("ERROR"));
    }
}

package com.skar.usuario.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.skar.usuario.dto.RegistracionUsuarioDto;
import com.skar.usuario.model.Usuario;
import com.skar.usuario.service.UsuarioServiceImp;

@WebMvcTest(UsuarioController.class) // Indica que se está probando el controlador de Usuario
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioServiceImp usuarioService;

    private Usuario usuario;

    private RegistracionUsuarioDto registracionUsuarioDto;

    @BeforeEach
    void setUo() {
        Usuario UsuarioTest = new Usuario();
        UsuarioTest.setNombre("Juan");
        UsuarioTest.setApellidos("Lopez Garcia");
        UsuarioTest.setEmail("juanito@gmail.com");
        UsuarioTest.setContrasena("Juan062505");
        UsuarioTest.setTelefono("12345678901");
        UsuarioTest.setDireccion("Alameda Ospicio 56");
        UsuarioTest.setRol("EMPLEADO");
        UsuarioTest.setEstado(true);

        // registracionUsuarioDto.convertirDtoAUsuario(UsuarioTest);
    }

    @Test
    public void testRegistrarUsuario() throws Exception {

        when(UsuarioServiceImp.save(any(Usuario.class))).thenReturn(usuario);

        mockMvc.perform(post("/api/v1/usuarios").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Juan"))
                .andExpect(jsonPath("$.apellidos").value("Lopez Garcia"))
                .andExpect(jsonPath("$.email").value("juanito@gmail.com"))
                .andExpect(jsonPath("$.telefono").value("12345678901"))
                .andExpect(jsonPath("$.direccion").value("Alameda Ospicio 56"))
                .andExpect(jsonPath("$.rol").value("EMPLEADO"))
                .andExpect(jsonPath("$.estado").value(true));
    }

}

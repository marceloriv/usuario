package com.skar.usuario.dto;

import com.skar.usuario.model.Rol;
import com.skar.usuario.model.Usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistracionUsuarioDto {

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    private String apellidos;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email no es válido")
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String contrasena;

    @Pattern(regexp = "\\d+", message = "El teléfono debe contener solo números")
    @Size(min = 11, max = 11, message = "El teléfono debe tener exactamente 11 dígitos")
    private String telefono;
    private String direccion;

    @NotNull(message = "El rol no puede estar vacío")
    private Rol rol;

    @NotNull(message = "El estado no puede estar vacío")
    private Boolean estado;

    /**
     * Convierte este DTO a un objeto Usuario
     *
     * @return Usuario creado a partir de este DTO
     */
    public Usuario convertirDtoAUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNombre(this.nombre);
        usuario.setApellidos(this.apellidos);
        usuario.setEmail(this.email);
        usuario.setContrasena(this.contrasena);
        usuario.setTelefono(this.telefono);
        usuario.setDireccion(this.direccion);
        usuario.setRol(this.rol.name());
        usuario.setEstado(this.estado);
        return usuario;
    }
}

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

    @Pattern(regexp = "\\d*", message = "El teléfono debe contener solo números")
    private String telefono; // Opcional
    private String direccion;

    private Rol rol; // Opcional, default: USUARIO

    private Boolean estado; // Opcional, default: true

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
        // Aplicar valores por defecto si no se proporcionan
        usuario.setRol(this.rol != null ? this.rol.name() : "USUARIO");
        usuario.setEstado(this.estado != null ? this.estado : true);
        return usuario;
    }
}

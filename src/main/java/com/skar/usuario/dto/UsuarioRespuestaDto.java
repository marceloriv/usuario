package com.skar.usuario.dto;

import com.skar.usuario.model.Usuario;
import com.skar.usuario.model.Rol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRespuestaDto {
    private Long id;
    private String nombre;
    private String apellidos;
    private String email;
    private String telefono;
    private String direccion;
    private Rol rol;
    private Boolean estado;

    public static UsuarioRespuestaDto from(Usuario usuario) {
        return new UsuarioRespuestaDto(
            usuario.getId(),
            usuario.getNombre(),
            usuario.getApellidos(),
            usuario.getEmail(),
            usuario.getTelefono(),
            usuario.getDireccion(),
            usuario.getRol(),
            usuario.getEstado());
    }
}
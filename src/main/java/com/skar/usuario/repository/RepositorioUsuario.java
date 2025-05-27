package com.skar.usuario.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skar.usuario.model.Rol;
import com.skar.usuario.model.Usuario;

@Repository
public interface RepositorioUsuario extends JpaRepository<Usuario, Long> {

    Usuario findByEmail(String email);

    Optional<Usuario> findByTelefono(String telefono);

    List<Usuario> findByNombre(String nombre);

    Optional<Usuario> findByApellidos(String apellidos);

    List<Usuario> findByEstado(boolean estado);

    List<Usuario> findByRol(Rol rol);

    List<Usuario> findByNombreAndApellidos(String nombre, String apellidos);

}

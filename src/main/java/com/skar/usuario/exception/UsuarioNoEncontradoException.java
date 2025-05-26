package com.skar.usuario.exception;

public class UsuarioNoEncontradoException extends Exception {

    public UsuarioNoEncontradoException(String message) {
        super("Usuario no encontrado: " + message);
    }


}

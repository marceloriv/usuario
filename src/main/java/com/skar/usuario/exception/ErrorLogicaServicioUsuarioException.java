package com.skar.usuario.exception;

public class ErrorLogicaServicioUsuarioException extends Exception {

    public ErrorLogicaServicioUsuarioException(String message) {
        super("Error en la lógica del servicio de usuario: ");
    }

}

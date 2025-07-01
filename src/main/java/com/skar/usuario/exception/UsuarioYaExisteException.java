package com.skar.usuario.exception;

/**
 * Excepción personalizada que se lanza cuando se intenta registrar un usuario
 * que ya existe en el sistema.
 * <p>
 * Esta excepción se produce típicamente cuando:
 * </p>
 * <ul>
 * <li>Se intenta registrar un usuario con un email que ya está en uso</li>
 * <li>Se intenta registrar un usuario con un teléfono que ya está
 * registrado</li>
 * <li>Se intenta crear un usuario con datos únicos duplicados</li>
 * </ul>
 * <p>
 * Extiende de {@link Exception} por lo que es una excepción checked que debe
 * ser manejada explícitamente en el código que la puede producir.
 * </p>
 *
 * @author Sistema de Gestión de Usuarios
 * @version 1.0.0
 * @since 1.0.0
 * @see com.skar.usuario.service.UsuarioService
 * @see com.skar.usuario.handler.UsuarioExceptionHandler
 */
public class UsuarioYaExisteException extends Exception {

    /**
     * Construye una nueva instancia de UsuarioYaExisteException con el mensaje
     * especificado.
     * <p>
     * El mensaje proporcionado debe describir específicamente qué campo o
     * criterio causó la duplicación (por ejemplo: "email", "teléfono", etc.).
     * </p>
     *
     * @param message mensaje descriptivo que explica por qué el usuario ya
     * existe. Debería incluir información específica sobre el campo duplicado.
     *
     * @example      <pre>
     * throw new UsuarioYaExisteException("Ya existe un usuario con el email: juan@example.com");
     * throw new UsuarioYaExisteException("Ya existe un usuario con el teléfono: +34123456789");
     * </pre>
     */
    public UsuarioYaExisteException(String message) {
        super(message);
    }
}

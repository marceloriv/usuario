package com.skar.usuario.exception;

/**
 * Excepción personalizada que se lanza cuando se intenta acceder a un usuario
 * que no existe en el sistema.
 * <p>
 * Esta excepción se produce típicamente cuando:
 * </p>
 * <ul>
 * <li>Se busca un usuario por email y no se encuentra en la base de datos</li>
 * <li>Se busca un usuario por teléfono y no existe</li>
 * <li>Se intenta actualizar o eliminar un usuario con un ID inexistente</li>
 * <li>Se accede a un usuario que fue eliminado previamente</li>
 * </ul>
 * <p>
 * Extiende de {@link Exception} por lo que es una excepción checked que debe
 * ser manejada explícitamente en el código que la puede producir.
 * </p>
 * <p>
 * El constructor automáticamente prefija el mensaje con "Usuario no encontrado:
 * " para proporcionar contexto claro sobre el tipo de error.
 * </p>
 *
 * @author Sistema de Gestión de Usuarios
 * @version 1.0.0
 * @since 1.0.0
 * @see com.skar.usuario.service.UsuarioService
 * @see com.skar.usuario.handler.UsuarioExceptionHandler
 * @see com.skar.usuario.repository.RepositorioUsuario
 */
public class UsuarioNoEncontradoException extends Exception {

    /**
     * Construye una nueva instancia de UsuarioNoEncontradoException con el
     * mensaje especificado.
     * <p>
     * El mensaje se prefija automáticamente con "Usuario no encontrado: " para
     * proporcionar contexto claro. El mensaje proporcionado debe describir
     * específicamente qué criterio de búsqueda se utilizó.
     * </p>
     *
     * @param message mensaje descriptivo que explica qué usuario se estaba
     *                buscando. Debería incluir información específica sobre el
     *                criterio de
     *                búsqueda utilizado (email, teléfono, ID, etc.).
     *
     * @example
     * 
     *          <pre>
     *          throw new UsuarioNoEncontradoException("juan@example.com");
     *          // Resultado: "Usuario no encontrado: juan@example.com"
     *
     *          throw new UsuarioNoEncontradoException("con teléfono +34123456789");
     *          // Resultado: "Usuario no encontrado: con teléfono +34123456789"
     *
     *          throw new UsuarioNoEncontradoException("con ID 123");
     *          // Resultado: "Usuario no encontrado: con ID 123"
     *          </pre>
     */
    public UsuarioNoEncontradoException(String message) {
        super("Usuario no encontrado: " + message);
    }

}

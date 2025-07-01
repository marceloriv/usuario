package com.skar.usuario.exception;

/**
 * Excepción personalizada que se lanza cuando ocurre un error en la lógica de
 * negocio del servicio de usuarios que no puede ser categorizado como un error
 * específico.
 * <p>
 * Esta excepción se produce típicamente cuando:
 * </p>
 * <ul>
 * <li>Fallan las reglas de negocio durante el procesamiento de usuarios</li>
 * <li>Ocurren errores de validación complejos a nivel de servicio</li>
 * <li>Se producen inconsistencias en el estado de los datos</li>
 * <li>Hay problemas con operaciones transaccionales</li>
 * <li>Fallan las operaciones de base de datos de manera inesperada</li>
 * <li>Se detectan condiciones de error que requieren intervención manual</li>
 * </ul>
 * <p>
 * Extiende de {@link Exception} por lo que es una excepción checked que debe
 * ser manejada explícitamente en el código que la puede producir.
 * </p>
 * <p>
 * El constructor automáticamente prefija el mensaje con "Error en la lógica del
 * servicio de usuario: " para proporcionar contexto claro sobre el origen del
 * error.
 * </p>
 *
 * @author Sistema de Gestión de Usuarios
 * @version 1.0.0
 * @since 1.0.0
 * @see com.skar.usuario.service.UsuarioService
 * @see com.skar.usuario.service.UsuarioServiceImp
 * @see com.skar.usuario.handler.GlobalExceptionHandler
 */
public class ErrorLogicaServicioUsuarioException extends Exception {

    /**
     * Construye una nueva instancia de ErrorLogicaServicioUsuarioException con
     * el mensaje especificado.
     * <p>
     * El mensaje se prefija automáticamente con "Error en la lógica del
     * servicio de usuario: " para proporcionar contexto claro sobre el origen
     * del error. El mensaje proporcionado debe describir específicamente qué
     * operación o regla de negocio falló.
     * </p>
     *
     * @param message mensaje descriptivo que explica qué error específico
     * ocurrió en la lógica del servicio. Debería incluir información detallada
     * sobre la operación que falló y, si es posible, sugerencias para la
     * resolución.
     *
     * @example      <pre>
     * throw new ErrorLogicaServicioUsuarioException("No se puede eliminar un usuario activo con pedidos pendientes");
     * // Resultado: "Error en la lógica del servicio de usuario: No se puede eliminar un usuario activo con pedidos pendientes"
     *
     * throw new ErrorLogicaServicioUsuarioException("El rol especificado no es válido para este tipo de usuario");
     * // Resultado: "Error en la lógica del servicio de usuario: El rol especificado no es válido para este tipo de usuario"
     *
     * throw new ErrorLogicaServicioUsuarioException("Falló la transacción al actualizar el perfil del usuario");
     * // Resultado: "Error en la lógica del servicio de usuario: Falló la transacción al actualizar el perfil del usuario"
     * </pre>
     */
    public ErrorLogicaServicioUsuarioException(String message) {
        super("Error en la lógica del servicio de usuario: " + message);
    }

}

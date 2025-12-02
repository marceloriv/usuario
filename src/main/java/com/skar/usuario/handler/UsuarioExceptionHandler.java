package com.skar.usuario.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.skar.usuario.dto.ApiRespuestaDto;
import com.skar.usuario.dto.ApiRespuestaEstados;
import com.skar.usuario.exception.UsuarioNoEncontradoException;
import com.skar.usuario.exception.UsuarioYaExisteException;
import com.skar.usuario.exception.ErrorLogicaServicioUsuarioException;

import io.swagger.v3.oas.annotations.Hidden;

/**
 * Manejador especializado de excepciones específicas del dominio de usuarios.
 * <p>
 * Esta clase se encarga de interceptar y manejar las excepciones personalizadas
 * relacionadas con las operaciones de usuarios, proporcionando respuestas HTTP
 * apropiadas y mensajes de error consistentes.
 * </p>
 * <p>
 * Trabaja en conjunto con {@link GlobalExceptionHandler} para proporcionar un
 * manejo completo de errores en la aplicación, donde este manejador se
 * especializa en errores específicos del dominio de usuarios.
 * </p>
 * <p>
 * La anotación {@code @Hidden} oculta este controlador de la documentación de
 * Swagger ya que no representa endpoints públicos sino manejo de errores.
 * </p>
 *
 * @author Sistema de Gestión de Usuarios
 * @version 1.0.0
 * @since 1.0.0
 * @see GlobalExceptionHandler
 * @see com.skar.usuario.exception.UsuarioNoEncontradoException
 * @see com.skar.usuario.exception.UsuarioYaExisteException
 * @see org.springframework.web.bind.annotation.RestControllerAdvice
 */
@Hidden
@RestControllerAdvice
public class UsuarioExceptionHandler {

    /**
     * Maneja excepciones de tipo UsuarioNoEncontradoException.
     * <p>
     * Este método se invoca automáticamente cuando se lanza una
     * {@link UsuarioNoEncontradoException} desde cualquier controlador de la
     * aplicación. Convierte la excepción en una respuesta HTTP apropiada con
     * estado 404 (NOT_FOUND).
     * </p>
     * <p>
     * Típicamente se produce cuando:
     * </p>
     * <ul>
     * <li>Se busca un usuario por email y no existe</li>
     * <li>Se intenta obtener un usuario por teléfono inexistente</li>
     * <li>Se accede a un usuario que fue eliminado</li>
     * </ul>
     *
     * @param exception la excepción UsuarioNoEncontradoException que contiene
     *                  el mensaje específico sobre qué usuario no se encontró
     * @return ResponseEntity con estado HTTP 404 (NOT_FOUND) y un cuerpo que
     *         contiene el estado de error y el mensaje de la excepción
     *
     * @see UsuarioNoEncontradoException
     * @see HttpStatus#NOT_FOUND
     */
    @ExceptionHandler(value = UsuarioNoEncontradoException.class)
    public ResponseEntity<ApiRespuestaDto> usuarioNoEncontradoExceptionHandler(UsuarioNoEncontradoException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiRespuestaDto(ApiRespuestaEstados.ERROR, exception.getMessage()));
    }

    /**
     * Maneja excepciones de tipo UsuarioYaExisteException.
     * <p>
     * Este método se invoca automáticamente cuando se lanza una
     * {@link UsuarioYaExisteException} desde cualquier controlador de la
     * aplicación. Convierte la excepción en una respuesta HTTP apropiada con
     * estado 409 (CONFLICT).
     * </p>
     * <p>
     * Típicamente se produce cuando:
     * </p>
     * <ul>
     * <li>Se intenta registrar un usuario con un email ya existente</li>
     * <li>Se intenta crear un usuario con un teléfono duplicado</li>
     * <li>Se violan restricciones de unicidad en campos únicos</li>
     * </ul>
     *
     * @param exception la excepción UsuarioYaExisteException que contiene el
     *                  mensaje específico sobre qué campo del usuario está
     *                  duplicado
     * @return ResponseEntity con estado HTTP 409 (CONFLICT) y un cuerpo que
     *         contiene el estado de error y el mensaje de la excepción
     *
     * @see UsuarioYaExisteException
     * @see HttpStatus#CONFLICT
     */
    @ExceptionHandler(value = UsuarioYaExisteException.class)
    public ResponseEntity<ApiRespuestaDto> usuarioYaExisteExceptionHandler(UsuarioYaExisteException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiRespuestaDto(ApiRespuestaEstados.ERROR, exception.getMessage()));
    }

    /**
     * Maneja errores de lógica del servicio usuario.
     * Devuelve 422 (UNPROCESSABLE_ENTITY) para indicar que la petición es válida sintácticamente
     * pero no puede procesarse por reglas de negocio.
     */
    @ExceptionHandler(value = ErrorLogicaServicioUsuarioException.class)
    public ResponseEntity<ApiRespuestaDto> errorLogicaServicioUsuarioExceptionHandler(ErrorLogicaServicioUsuarioException exception) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ApiRespuestaDto(ApiRespuestaEstados.ERROR, exception.getMessage()));
    }

}

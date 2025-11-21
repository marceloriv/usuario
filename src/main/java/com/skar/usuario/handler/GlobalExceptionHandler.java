package com.skar.usuario.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.skar.usuario.dto.ApiRespuestaDto;
import com.skar.usuario.dto.ApiRespuestaEstados;

import io.swagger.v3.oas.annotations.Hidden;

/**
 * Manejador global de excepciones para toda la aplicación.
 * <p>
 * Esta clase intercepta y maneja excepciones generales que pueden ocurrir
 * en cualquier parte de la aplicación, proporcionando respuestas HTTP
 * consistentes y mensajes de error estandarizados.
 * </p>
 * <p>
 * Trabaja en conjunto con {@link UsuarioExceptionHandler} para proporcionar
 * un manejo completo de errores, donde este manejador se encarga de errores
 * generales del framework Spring y HTTP, mientras que el otro se especializa
 * en errores específicos del dominio de usuarios.
 * </p>
 * <p>
 * La anotación {@code @Hidden} oculta este controlador de la documentación
 * de Swagger ya que no representa endpoints públicos sino manejo de errores.
 * </p>
 *
 * @author Sistema de Gestión de Usuarios
 * @version 1.0.0
 * @since 1.0.0
 * @see UsuarioExceptionHandler
 * @see org.springframework.web.bind.annotation.RestControllerAdvice
 * @see org.springframework.web.bind.annotation.ExceptionHandler
 */
@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja excepciones de tipo NoHandlerFoundException.
     * <p>
     * Esta excepción se lanza cuando Spring Boot no puede encontrar un controlador
     * que maneje la URL solicitada por el cliente. Típicamente ocurre cuando se
     * hace una petición a un endpoint que no existe en la aplicación.
     * </p>
     * <p>
     * Ejemplos de cuándo se produce:
     * </p>
     * <ul>
     * <li>GET /api/v1/endpoint-inexistente</li>
     * <li>POST /api/v2/usuarios (si no existe la versión v2)</li>
     * <li>PUT /api/v1/usuarios/accion-inexistente</li>
     * </ul>
     * 
     * @param exception la excepción NoHandlerFoundException que contiene
     *                  información
     *                  sobre la URL no encontrada y el método HTTP utilizado
     * @return ResponseEntity con estado HTTP 404 (NOT_FOUND) y un mensaje
     *         indicando que el recurso no fue encontrado
     * 
     * @since 1.0.0
     * @see NoHandlerFoundException
     * @see HttpStatus#NOT_FOUND
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiRespuestaDto> noHandlerFoundExceptionHandler(NoHandlerFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiRespuestaDto(ApiRespuestaEstados.ERROR, "Recurso no encontrado"));
    }

    /**
     * Maneja excepciones de tipo HttpMessageNotReadableException.
     * <p>
     * Esta excepción se produce cuando Spring Boot no puede deserializar el
     * cuerpo de la petición HTTP (generalmente JSON) debido a problemas
     * en la estructura o formato de los datos enviados.
     * </p>
     * <p>
     * Casos comunes que producen esta excepción:
     * </p>
     * <ul>
     * <li>JSON mal formado (sintaxis incorrecta, llaves sin cerrar, etc.)</li>
     * <li>Tipos de datos incompatibles (enviar string donde se espera número)</li>
     * <li>Estructura JSON que no coincide con el DTO esperado</li>
     * <li>Caracteres especiales no válidos o codificación incorrecta</li>
     * <li>Campos requeridos omitidos en el JSON</li>
     * </ul>
     * 
     * @param ex la excepción HttpMessageNotReadableException que contiene
     *           detalles sobre el error de deserialización del mensaje HTTP
     * @return ResponseEntity con estado HTTP 400 (BAD_REQUEST) y un mensaje
     *         indicando que la solicitud está mal formada
     * 
     * @since 1.0.0
     * @see HttpMessageNotReadableException
     * @see HttpStatus#BAD_REQUEST
     * 
     * @example
     * 
     *          <pre>
     * // JSON mal formado que produciría esta excepción:
     * {"nombre": "Juan" "apellidos": "Pérez"}  // Falta coma
     * {"email": 12345}                         // Tipo incorrecto
     * {"nombre": }                             // Valor faltante
     *          </pre>
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiRespuestaDto> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest()
                .body(new ApiRespuestaDto(ApiRespuestaEstados.ERROR, "Solicitud mal formada o datos no válidos"));
    }

    /**
     * Maneja excepciones de tipo MethodArgumentNotValidException.
     * <p>
     * Esta excepción se produce cuando falla la validación de Bean Validation
     * en los argumentos de los métodos del controlador que están anotados con
     * {@code @Valid}. Las validaciones incluyen anotaciones como:
     * </p>
     * <ul>
     * <li>{@code @NotNull} - El campo no puede ser nulo</li>
     * <li>{@code @NotBlank} - El campo no puede estar vacío o contener solo
     * espacios</li>
     * <li>{@code @Email} - El campo debe ser un email válido</li>
     * <li>{@code @Size} - El campo debe cumplir restricciones de tamaño</li>
     * <li>{@code @Pattern} - El campo debe coincidir con una expresión regular</li>
     * <li>{@code @Min/@Max} - Validaciones de valores numéricos</li>
     * </ul>
     * <p>
     * El método extrae el primer error de validación encontrado y construye
     * un mensaje descriptivo que incluye el nombre del campo y el mensaje
     * de error específico. Si no hay errores específicos de campo disponibles,
     * retorna un mensaje genérico.
     * </p>
     *
     * @param ex la excepción MethodArgumentNotValidException que contiene los
     *           errores de validación del BindingResult
     * @return ResponseEntity con estado HTTP 400 (BAD_REQUEST) y un mensaje
     *         detallado del error de validación en formato "campo: mensaje"
     * 
     * @since 1.0.0
     * @see MethodArgumentNotValidException
     * @see org.springframework.validation.BindingResult
     * @see jakarta.validation.Valid
     * @see HttpStatus#BAD_REQUEST
     * 
     * @example
     * 
     *          <pre>
     * // Entrada que produciría esta excepción:
     * {"email": "correo-invalido", "nombre": ""}
     * // Respuesta: "email: debe ser una dirección de correo electrónico válida"
     * 
     * {"telefono": "+34123", "email": "test@example.com"}
     * // Respuesta: "telefono: debe tener al menos 10 caracteres"
     *          </pre>
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiRespuestaDto> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String mensaje = "Error de validación";

        var fieldError = ex.getBindingResult().getFieldError();
        if (fieldError != null) {
            String campo = fieldError.getField();
            String error = fieldError.getDefaultMessage();
            mensaje = campo + ": " + error;
        }

        return ResponseEntity.badRequest()
                .body(new ApiRespuestaDto(ApiRespuestaEstados.ERROR, mensaje));
    }

}

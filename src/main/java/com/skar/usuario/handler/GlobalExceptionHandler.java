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

@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

    /*
     * Metodo para manejar excepciones de tipo NoHandlerFoundException.
     * Esta excepción se lanza cuando no se encuentra un controlador para una
     * solicitud.
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiRespuestaDto> noHandlerFoundExceptionHandler(NoHandlerFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiRespuestaDto(ApiRespuestaEstados.ERROR, "Recurso no encontrado"));
    }

    /*
     * Metodo para manejar excepciones de tipo Exception.
     * Esta excepción se lanza cuando ocurre un error inesperado en la aplicación.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiRespuestaDto> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest()
                .body(new ApiRespuestaDto(ApiRespuestaEstados.ERROR, "Solicitud mal formada o datos no válidos"));
    }

    /*
     * Metodo para manejar excepciones de tipo MethodArgumentNotValidException.
     * Esta excepción se lanza cuando hay errores de validación en los argumentos
     * del método.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiRespuestaDto> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String mensaje = "Error de validación";

        if (ex.getBindingResult().getFieldError() != null) {
            String campo = ex.getBindingResult().getFieldError().getField();
            String error = ex.getBindingResult().getFieldError().getDefaultMessage();
            mensaje = campo + ": " + error;
        }

        return ResponseEntity.badRequest()
                .body(new ApiRespuestaDto(ApiRespuestaEstados.ERROR, mensaje));
    }

}

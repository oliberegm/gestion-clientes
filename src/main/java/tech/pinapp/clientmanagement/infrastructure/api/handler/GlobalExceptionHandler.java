package tech.pinapp.clientmanagement.infrastructure.api.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tech.pinapp.clientmanagement.domain.exception.ClientNotFoundException;
import tech.pinapp.clientmanagement.infrastructure.api.dto.ErrorDetail;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the REST API.
 * Intercepts exceptions and translates them into a standard ErrorDetail response.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handles ClientNotFoundException (custom business exception).
     * Returns HTTP 404 Not Found.
     */
    @ExceptionHandler(ClientNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDetail handleClientNotFoundException(ClientNotFoundException ex, HttpServletRequest request) {
        log.warn("ClientNotFoundException: {} en la ruta {}", ex.getMessage(), request.getRequestURI());
        return ErrorDetail.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.NOT_FOUND.value())
            .error("Not Found")
            .message(ex.getMessage())
            .path(request.getRequestURI())
            .build();
    }

    /**
     * Handles validation errors for @Valid on @RequestBody.
     * Returns HTTP 400 Bad Request.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDetail handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage()));

        log.warn("MethodArgumentNotValidException: {} errores de validación en la ruta {}", errors.size(), request.getRequestURI());

        return ErrorDetail.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .error("Bad Request")
            .message("Error de validación. Por favor, revise los campos.")
            .path(request.getRequestURI())
            .validationErrors(errors)
            .build();
    }

    /**
     * Handles validation errors for path variables and request parameters.
     * Returns HTTP 400 Bad Request.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDetail handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {
        log.warn("ConstraintViolationException: {} en la ruta {}", ex.getMessage(), request.getRequestURI());
        return ErrorDetail.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .error("Bad Request")
            .message(ex.getMessage())
            .path(request.getRequestURI())
            .build();
    }

    /**
     * Generic handler for any other unhandled exceptions.
     * Returns HTTP 500 Internal Server Error.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDetail handleGlobalException(Exception ex, HttpServletRequest request) {
        log.error("Excepción no controlada: " + ex.getMessage(), ex); // Log the full stack trace
        return ErrorDetail.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .error("Internal Server Error")
            .message("Ocurrió un error inesperado en el servidor.")
            .path(request.getRequestURI())
            .build();
    }

    /**
     * Handles bad credentials during login.
     * Returns HTTP 401 Unauthorized.
     */
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDetail handleBadCredentialsException(BadCredentialsException ex, HttpServletRequest request) {
        log.warn("BadCredentialsException: Intento de login fallido en la ruta {}", request.getRequestURI());
        return ErrorDetail.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.UNAUTHORIZED.value())
            .error("Unauthorized")
            .message("Credenciales inválidas.")
            .path(request.getRequestURI())
            .build();
    }
}
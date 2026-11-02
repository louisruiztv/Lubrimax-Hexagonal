package org.lubrimax.com.infrastructure.adapter.in.rest;

import org.lubrimax.com.application.exception.ServicioExternoNoDisponibleException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Map;

@RestControllerAdvice(assignableTypes = EjecucionDeMantenimientoController.class)
public class EjecucionDeMantenimientoExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> datosInvalidos(IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(Map.of("mensaje", exception.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> conflicto(IllegalStateException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("mensaje", exception.getMessage()));
    }

    @ExceptionHandler(ServicioExternoNoDisponibleException.class)
    public ResponseEntity<Map<String, String>> dependenciaNoDisponible(ServicioExternoNoDisponibleException exception) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Map.of("mensaje", exception.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> integridad() {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("mensaje", "No se pudo guardar la ejecución: existe un conflicto de integridad de datos."));
    }
}

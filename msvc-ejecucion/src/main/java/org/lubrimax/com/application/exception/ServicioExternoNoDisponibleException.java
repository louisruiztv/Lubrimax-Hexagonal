package org.lubrimax.com.application.exception;

public class ServicioExternoNoDisponibleException extends RuntimeException {
    public ServicioExternoNoDisponibleException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}

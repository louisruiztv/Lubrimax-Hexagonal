package org.lubrimax.com.application.port.out;

public interface OrdenClientPort {
    boolean estaAutorizada(Long ordenId);
    void sincronizarEjecucion(Long ordenId, Long ejecucionId);
}

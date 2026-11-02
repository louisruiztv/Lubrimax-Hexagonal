package org.lubrimax.com.application.port.out;

public interface EjecucionClientPort {
    String estadoDeEjecucion(Long ordenId, Long ejecucionId);
}

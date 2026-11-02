package org.lubrimax.com.domain.model;

public enum EstadoEjecucion {
    PENDIENTE,
    EN_PROCESO,
    FINALIZADA,
    CANCELADA;

    public boolean permiteTransicionA(EstadoEjecucion siguiente) {
        if (this == siguiente) {
            return true;
        }
        return switch (this) {
            case PENDIENTE -> siguiente == EN_PROCESO || siguiente == CANCELADA;
            case EN_PROCESO -> siguiente == FINALIZADA || siguiente == CANCELADA;
            case FINALIZADA, CANCELADA -> false;
        };
    }
}

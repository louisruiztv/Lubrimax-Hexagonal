package org.lubrimax.com.domain.model;

public enum EstadoTarea {
    PENDIENTE,
    EN_PROCESO,
    FINALIZADA,
    NO_REALIZADA;

    public boolean permiteTransicionA(EstadoTarea siguiente) {
        if (this == siguiente) {
            return true;
        }
        return switch (this) {
            case PENDIENTE -> siguiente == EN_PROCESO || siguiente == FINALIZADA || siguiente == NO_REALIZADA;
            case EN_PROCESO -> siguiente == FINALIZADA || siguiente == NO_REALIZADA;
            case FINALIZADA, NO_REALIZADA -> false;
        };
    }
}

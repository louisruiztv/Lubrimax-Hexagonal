package org.lubrimax.com.domain.model;

import java.util.Locale;

public enum EstadoOrden {
    PENDIENTE, AUTORIZADA, EN_PROCESO, FINALIZADA, CANCELADA;

    public static EstadoOrden desde(String valor) {
        if (valor != null) {
            try { return valueOf(valor.trim().toUpperCase(Locale.ROOT)); }
            catch (IllegalArgumentException ignored) { }
        }
        throw new IllegalArgumentException("Estado de orden inválido. Valores permitidos: PENDIENTE, AUTORIZADA, EN_PROCESO, FINALIZADA, CANCELADA.");
    }

    public boolean permite(EstadoOrden destino) {
        if (this == destino) return true;
        return switch (this) {
            case PENDIENTE -> destino == AUTORIZADA || destino == CANCELADA;
            case AUTORIZADA -> destino == EN_PROCESO || destino == CANCELADA;
            case EN_PROCESO -> destino == FINALIZADA || destino == CANCELADA;
            case FINALIZADA, CANCELADA -> false;
        };
    }
}

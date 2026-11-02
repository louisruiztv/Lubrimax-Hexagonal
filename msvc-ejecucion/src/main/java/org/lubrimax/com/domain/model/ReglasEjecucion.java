package org.lubrimax.com.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

final class ReglasEjecucion {
    private ReglasEjecucion() {} 
    static void positivo(Long id, String campo) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(campo + " debe ser positivo.");
        }
    }

    static void texto(String valor, String campo, int maximo) {
        if (valor == null || valor.isBlank() || valor.length() > maximo) {
            throw new IllegalArgumentException(campo + " es obligatorio y admite hasta " + maximo + " caracteres.");
        }
    }

    static void fechas(LocalDateTime inicio, LocalDateTime fin, String campo) {
        if (fin != null && (inicio == null || fin.isBefore(inicio))) {
            throw new IllegalArgumentException("El fin de " + campo + " requiere inicio y no puede ser anterior.");
        }
    }

    static <E extends Enum<E>> E estado(Class<E> tipo, String valor) {
        if (valor == null) {
            throw new IllegalArgumentException(tipo.getSimpleName() + " es obligatorio.");
        }
        try {
            return Enum.valueOf(tipo, valor);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(tipo.getSimpleName() + " inválido. Valores permitidos: "
                    + Arrays.toString(tipo.getEnumConstants()));
        }
    }

    static void decimal(BigDecimal valor, String campo, int escala, String maximo, boolean permiteCero) {
        if (valor == null || valor.signum() < 0 || (!permiteCero && valor.signum() == 0)
                || valor.stripTrailingZeros().scale() > escala || valor.compareTo(new BigDecimal(maximo)) > 0) {
            throw new IllegalArgumentException(campo + " tiene un valor o una precisión inválidos.");
        }
    }
}

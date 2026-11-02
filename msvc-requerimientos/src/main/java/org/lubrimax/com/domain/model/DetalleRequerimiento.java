package org.lubrimax.com.domain.model;

import lombok.*;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DetalleRequerimiento {
    private Long detalleId;
    private Long productoId;
    private Long presentacionId;
    private BigDecimal cantidadRequerida;
    private BigDecimal cantidadDisponible;
    private EstadoDisponibilidad estadoDisponibilidad;
    private Boolean reservado;
    private String observacion;

    public void actualizarCantidades(BigDecimal requerida, BigDecimal disponible) {
        EstadoDisponibilidad disponibilidad = disponibilidad(requerida, disponible);
        validarReserva(Boolean.TRUE.equals(reservado), disponibilidad);
        cantidadRequerida = requerida;
        cantidadDisponible = disponible;
        estadoDisponibilidad = disponibilidad;
    }

    public EstadoDisponibilidad calcularDisponibilidad() {
        return disponibilidad(cantidadRequerida, cantidadDisponible);
    }

    public void cambiarReserva(boolean reservado) {
        if (reservado) {
            validarReserva(true, calcularDisponibilidad());
        }
        this.reservado = reservado;
    }

    private EstadoDisponibilidad disponibilidad(BigDecimal requerida, BigDecimal disponible) {
        cantidad(requerida, "cantidadRequerida", false);
        cantidad(disponible, "cantidadDisponible", true);
        return disponible.signum() == 0 ? EstadoDisponibilidad.FALTANTE
                : disponible.compareTo(requerida) < 0 ? EstadoDisponibilidad.PARCIAL : EstadoDisponibilidad.DISPONIBLE;
    }

    private void validarReserva(boolean reservada, EstadoDisponibilidad disponibilidad) {
        if (reservada && disponibilidad != EstadoDisponibilidad.DISPONIBLE) {
            throw new IllegalArgumentException("Solo se puede marcar reservado cuando la cantidad requerida está disponible.");
        }
    }

    private void cantidad(BigDecimal valor, String campo, boolean permiteCero) {
        if (valor == null || valor.signum() < 0 || (!permiteCero && valor.signum() == 0)
                || valor.stripTrailingZeros().scale() > 3
                || valor.compareTo(new BigDecimal("999999999.999")) > 0) {
            throw new IllegalArgumentException(campo + " debe ser " + (permiteCero ? "no negativa" : "positiva")
                    + ", hasta 999999999.999 y con máximo tres decimales.");
        }
    }
}

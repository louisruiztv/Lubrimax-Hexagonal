package org.lubrimax.com.domain.model;

import lombok.*;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RequerimientoDeMantenimiento {
    private Long requerimientoId;
    private Long ordenId;
    private String estado;
    private List<DetalleRequerimiento> detalles;

    public void agregarDetalle(DetalleRequerimiento detalle) {
        if (detalle == null) {
            throw new IllegalArgumentException("La lista no admite detalles nulos.");
        }
        if (detalle.getProductoId() == null || detalle.getProductoId() <= 0
                || detalle.getPresentacionId() == null || detalle.getPresentacionId() <= 0) {
            throw new IllegalArgumentException("productoId y presentacionId deben ser positivos.");
        }
        List<DetalleRequerimiento> actuales = detalles == null ? List.of() : detalles;
        for (DetalleRequerimiento actual : actuales) {
            if (detalle.getDetalleId() != null && detalle.getDetalleId().equals(actual.getDetalleId())) {
                throw new IllegalArgumentException("El detalleId está repetido.");
            }
            if (detalle.getProductoId().equals(actual.getProductoId())
                    && detalle.getPresentacionId().equals(actual.getPresentacionId())) {
                throw new IllegalArgumentException("No se permite repetir un producto y su presentación.");
            }
        }
        List<DetalleRequerimiento> nuevos = new ArrayList<>(actuales);
        nuevos.add(detalle);
        detalles = nuevos;
    }

    public void eliminarDetalle(Long detalleId) {
        if (detalleId == null) {
            throw new IllegalArgumentException("El detalleId es obligatorio.");
        }
        if (detalles == null || detalles.stream().noneMatch(d -> detalleId.equals(d.getDetalleId()))) {
            throw new IllegalArgumentException("El detalle no pertenece al requerimiento.");
        }
        List<DetalleRequerimiento> restantes = new ArrayList<>(detalles);
        restantes.removeIf(d -> detalleId.equals(d.getDetalleId()));
        detalles = restantes;
    }
    public void validarDatos(Set<Long> idsPermitidos) {
        idPositivo(this.getOrdenId(), "ordenId");
        String estado = this.getEstado();
        if (estado == null || estado.isBlank() || estado.length() > 50) {
            throw new IllegalArgumentException("El estado es obligatorio y admite hasta 50 caracteres.");
        }
        if (this.getDetalles() == null) {
            throw new IllegalArgumentException("Se debe enviar detalles, aunque sea una lista vacía.");
        }
        RequerimientoDeMantenimiento validado = new RequerimientoDeMantenimiento();
        for (DetalleRequerimiento detalle : this.getDetalles()) {
            validado.agregarDetalle(detalle);
            Long id = detalle.getDetalleId();
            if (id != null && !idsPermitidos.contains(id)) {
                throw new IllegalArgumentException("El detalleId es ajeno, inexistente o está repetido.");
            }
            if (detalle.getReservado() == null) {
                throw new IllegalArgumentException("reservado debe ser true o false.");
            }
            detalle.actualizarCantidades(detalle.getCantidadRequerida(), detalle.getCantidadDisponible());
            detalle.cambiarReserva(detalle.getReservado());
            if (detalle.getObservacion() != null && detalle.getObservacion().length() > 1000) {
                throw new IllegalArgumentException("La observacion admite hasta 1000 caracteres.");
            }
        }
    }

    private void idPositivo(Long id, String campo) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(campo + " debe ser positivo.");
        }
    }

}

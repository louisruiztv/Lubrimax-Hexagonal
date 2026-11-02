package org.lubrimax.com.infrastructure.adapter.out.persistence.mapper;

import org.lubrimax.com.domain.model.*;
import org.lubrimax.com.infrastructure.adapter.out.persistence.entity.*;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class RequerimientoDeMantenimientoPersistenceMapper {
    // Preserve managed collection and child identities for orphanRemoval.
    public void updateEntity(RequerimientoDeMantenimiento requerimiento, RequerimientoDeMantenimientoEntity entity) {
        entity.setOrdenId(requerimiento.getOrdenId());
        entity.setEstado(requerimiento.getEstado());
        Map<Long, DetalleRequerimientoEntity> existentes = entity.getDetalles().stream()
                .collect(Collectors.toMap(DetalleRequerimientoEntity::getDetalleId, d -> d));
        Set<Long> recibidos = requerimiento.getDetalles().stream().map(DetalleRequerimiento::getDetalleId)
                .filter(Objects::nonNull).collect(Collectors.toSet());
        entity.getDetalles().removeIf(d -> !recibidos.contains(d.getDetalleId()));
        for (DetalleRequerimiento detalle : requerimiento.getDetalles()) {
            DetalleRequerimientoEntity interna;
            if (detalle.getDetalleId() == null) {
                interna = new DetalleRequerimientoEntity();
                interna.setRequerimiento(entity);
                entity.getDetalles().add(interna);
            } else {
                interna = existentes.get(detalle.getDetalleId());
                if (interna == null) {
                    throw new IllegalArgumentException("El detalle no pertenece al requerimiento.");
                }
            }
            interna.setProductoId(detalle.getProductoId());
            interna.setPresentacionId(detalle.getPresentacionId());
            interna.setCantidadRequerida(detalle.getCantidadRequerida());
            interna.setCantidadDisponible(detalle.getCantidadDisponible());
            interna.setEstadoDisponibilidad(detalle.getEstadoDisponibilidad());
            interna.setReservado(detalle.getReservado());
            interna.setObservacion(detalle.getObservacion());
        }
    }

    public RequerimientoDeMantenimiento toModel(RequerimientoDeMantenimientoEntity entity) {
        return new RequerimientoDeMantenimiento(entity.getRequerimientoId(), entity.getOrdenId(), entity.getEstado(),
                entity.getDetalles().stream().map(d -> new DetalleRequerimiento(
                        d.getDetalleId(), d.getProductoId(), d.getPresentacionId(), d.getCantidadRequerida(),
                        d.getCantidadDisponible(), d.getEstadoDisponibilidad(), d.getReservado(), d.getObservacion())).toList());
    }
}

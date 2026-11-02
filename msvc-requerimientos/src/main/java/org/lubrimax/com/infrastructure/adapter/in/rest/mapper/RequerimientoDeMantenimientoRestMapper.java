package org.lubrimax.com.infrastructure.adapter.in.rest.mapper;

import org.lubrimax.com.domain.model.*;
import org.lubrimax.com.infrastructure.adapter.in.rest.dto.*;
import org.springframework.stereotype.Component;

@Component
public class RequerimientoDeMantenimientoRestMapper {
    public RequerimientoDeMantenimiento toDomain(RequerimientoDeMantenimientoDto dto) {
        return new RequerimientoDeMantenimiento(dto.getRequerimientoId(), dto.getOrdenId(), dto.getEstado(),
                dto.getDetalles() == null ? null : dto.getDetalles().stream().map(d -> d == null ? null :
                        new DetalleRequerimiento(d.getDetalleId(), d.getProductoId(), d.getPresentacionId(),
                                d.getCantidadRequerida(), d.getCantidadDisponible(), d.getEstadoDisponibilidad(),
                                d.getReservado(), d.getObservacion())).toList());
    }

    public RequerimientoDeMantenimientoDto toDto(RequerimientoDeMantenimiento model) {
        return new RequerimientoDeMantenimientoDto(model.getRequerimientoId(), model.getOrdenId(), model.getEstado(),
                model.getDetalles() == null ? null : model.getDetalles().stream().map(d -> d == null ? null :
                        new DetalleRequerimientoDto(d.getDetalleId(), d.getProductoId(), d.getPresentacionId(),
                                d.getCantidadRequerida(), d.getCantidadDisponible(), d.getEstadoDisponibilidad(),
                                d.getReservado(), d.getObservacion())).toList());
    }
}

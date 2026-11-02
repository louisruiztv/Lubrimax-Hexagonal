package org.lubrimax.com.infrastructure.adapter.in.rest;

import org.lubrimax.com.domain.model.*;
import org.lubrimax.com.infrastructure.adapter.in.rest.dto.*;
import org.springframework.stereotype.Component;

@Component
public class ServicioDeMantenimientoRestMapper {
    public ServicioDeMantenimiento toDomain(ServicioDeMantenimientoDto dto) {
        return new ServicioDeMantenimiento(dto.getServicioId(), dto.getNombre(), dto.getTipoServicio(),
                dto.getDescripcion(), dto.getEstado(), dto.getRequiere() == null ? null : dto.getRequiere().stream()
                .map(tipo -> tipo == null ? null : new TipoProductoRequerido(
                        tipo.categoriaProducto(), tipo.tipoProducto(), tipo.obligatorio())).toList());
    }

    public ServicioDeMantenimientoDto toDto(ServicioDeMantenimiento model) {
        return new ServicioDeMantenimientoDto(model.getServicioId(), model.getNombre(), model.getTipoServicio(),
                model.getDescripcion(), model.getEstado(), model.getRequiere() == null ? null : model.getRequiere().stream()
                .map(tipo -> tipo == null ? null : new TipoProductoRequeridoDto(
                        tipo.categoriaProducto(), tipo.tipoProducto(), tipo.obligatorio())).toList());
    }
}

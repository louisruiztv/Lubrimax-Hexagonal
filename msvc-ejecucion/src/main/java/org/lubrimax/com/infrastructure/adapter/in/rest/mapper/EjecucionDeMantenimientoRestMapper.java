package org.lubrimax.com.infrastructure.adapter.in.rest.mapper;

import org.lubrimax.com.domain.model.*;
import org.lubrimax.com.infrastructure.adapter.in.rest.dto.*;
import org.springframework.stereotype.Component;

@Component
public class EjecucionDeMantenimientoRestMapper {
    public EjecucionDeMantenimiento toModel(EjecucionDeMantenimientoDto dto) {
        if (dto == null) return null;
        var liquidacion = dto.getLiquidacion();
        var proximo = dto.getProximoServicio();
        return new EjecucionDeMantenimiento(dto.getEjecucionId(), dto.getOrdenId(), dto.getTecnicoId(),
                dto.getEstado(), dto.getFechaInicio(), dto.getFechaFin(),
                liquidacion == null ? null : new LiquidacionDeServicio(liquidacion.subtotalProductos(), liquidacion.subtotalManoObra(), liquidacion.total()),
                proximo == null ? null : new ProgramacionDeProximoServicio(proximo.fechaSugerida(), proximo.kilometrajeSugerido()),
                dto.getTareas() == null ? null : dto.getTareas().stream().map(this::toModel).toList(),
                dto.getConsumos() == null ? null : dto.getConsumos().stream().map(this::toModel).toList());
    }

    public EjecucionDeMantenimientoDto toDto(EjecucionDeMantenimiento model) {
        if (model == null) return null;
        var liquidacion = model.getLiquidacion();
        var proximo = model.getProximoServicio();
        return new EjecucionDeMantenimientoDto(model.getEjecucionId(), model.getOrdenId(), model.getTecnicoId(),
                model.getEstado(), model.getFechaInicio(), model.getFechaFin(),
                liquidacion == null ? null : new LiquidacionDeServicioDto(liquidacion.subtotalProductos(), liquidacion.subtotalManoObra(), liquidacion.total()),
                proximo == null ? null : new ProgramacionDeProximoServicioDto(proximo.fechaSugerida(), proximo.kilometrajeSugerido()),
                model.getTareas() == null ? null : model.getTareas().stream().map(this::toDto).toList(),
                model.getConsumos() == null ? null : model.getConsumos().stream().map(this::toDto).toList());
    }

    public ConsumoReal toModel(ConsumoRealDto dto) {
        return dto == null ? null : new ConsumoReal(dto.getConsumoId(), dto.getProductoId(), dto.getPresentacionId(), dto.getCantidadReal(), dto.getPrecioAplicado());
    }

    private ConsumoRealDto toDto(ConsumoReal model) {
        return model == null ? null : new ConsumoRealDto(model.getConsumoId(), model.getProductoId(), model.getPresentacionId(), model.getCantidadReal(), model.getPrecioAplicado());
    }

    private TareaEjecutada toModel(TareaEjecutadaDto dto) {
        return dto == null ? null : new TareaEjecutada(dto.getTareaId(), dto.getTipoTarea(), dto.getEstado(), dto.getInicio(), dto.getFin());
    }

    private TareaEjecutadaDto toDto(TareaEjecutada model) {
        return model == null ? null : new TareaEjecutadaDto(model.getTareaId(), model.getTipoTarea(), model.getEstado(), model.getInicio(), model.getFin());
    }
}

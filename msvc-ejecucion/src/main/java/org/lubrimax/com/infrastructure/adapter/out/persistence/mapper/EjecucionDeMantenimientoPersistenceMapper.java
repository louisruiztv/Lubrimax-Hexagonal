package org.lubrimax.com.infrastructure.adapter.out.persistence.mapper;

import org.lubrimax.com.domain.model.*;
import org.lubrimax.com.infrastructure.adapter.out.persistence.entity.*;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class EjecucionDeMantenimientoPersistenceMapper {
    public void updateEntity(EjecucionDeMantenimiento ejecucion, EjecucionDeMantenimientoEntity entity) {
        entity.setOrdenId(ejecucion.getOrdenId());
        entity.setTecnicoId(ejecucion.getTecnicoId());
        entity.setEstado(ejecucion.getEstado());
        entity.setFechaInicio(ejecucion.getFechaInicio());
        entity.setFechaFin(ejecucion.getFechaFin());

        var liquidacion = ejecucion.getLiquidacion();
        entity.setLiquidacion(new LiquidacionDeServicioEmbeddable(liquidacion.subtotalProductos(),
                liquidacion.subtotalManoObra(), liquidacion.total()));

        var proximo = ejecucion.getProximoServicio();
        entity.setProximoServicio(proximo == null ? null : new ProgramacionDeProximoServicioEmbeddable(
                proximo.fechaSugerida(), proximo.kilometrajeSugerido()));

        actualizarTareas(entity, ejecucion.getTareas());
        actualizarConsumos(entity, ejecucion.getConsumos());
    }

    private void actualizarTareas(EjecucionDeMantenimientoEntity entity, List<TareaEjecutada> lista) {
        Map<Long, TareaEjecutadaEntity> existentes = entity.getTareas().stream()
                .collect(Collectors.toMap(TareaEjecutadaEntity::getTareaId, item -> item));
        Set<Long> recibidos = lista.stream().map(TareaEjecutada::getTareaId).filter(Objects::nonNull).collect(Collectors.toSet());
        entity.getTareas().removeIf(item -> !recibidos.contains(item.getTareaId()));
        for (TareaEjecutada modelo : lista) {
            TareaEjecutadaEntity interna;
            if (modelo.getTareaId() == null) {
                interna = new TareaEjecutadaEntity();
                interna.setEjecucion(entity);
                entity.getTareas().add(interna);
            } else {
                interna = existentes.get(modelo.getTareaId());
                if (interna == null) {
                    throw new IllegalArgumentException("La entidad interna no pertenece a esta ejecución.");
                }
            }
            interna.setTipoTarea(modelo.getTipoTarea());
            interna.setEstado(modelo.getEstado());
            interna.setInicio(modelo.getInicio());
            interna.setFin(modelo.getFin());
        }
    }

    private void actualizarConsumos(EjecucionDeMantenimientoEntity entity, List<ConsumoReal> lista) {
        Map<Long, ConsumoRealEntity> existentes = entity.getConsumos().stream()
                .collect(Collectors.toMap(ConsumoRealEntity::getConsumoId, item -> item));
        Set<Long> recibidos = lista.stream().map(ConsumoReal::getConsumoId).filter(Objects::nonNull).collect(Collectors.toSet());
        entity.getConsumos().removeIf(item -> !recibidos.contains(item.getConsumoId()));
        for (ConsumoReal modelo : lista) {
            ConsumoRealEntity interna;
            if (modelo.getConsumoId() == null) {
                interna = new ConsumoRealEntity();
                interna.setEjecucion(entity);
                entity.getConsumos().add(interna);
            } else {
                interna = existentes.get(modelo.getConsumoId());
                if (interna == null) {
                    throw new IllegalArgumentException("La entidad interna no pertenece a esta ejecución.");
                }
            }
            interna.setProductoId(modelo.getProductoId());
            interna.setPresentacionId(modelo.getPresentacionId());
            interna.setCantidadReal(modelo.getCantidadReal());
            interna.setPrecioAplicado(modelo.getPrecioAplicado());
        }
    }

    public EjecucionDeMantenimiento toModel(EjecucionDeMantenimientoEntity entity) {
        var liquidacion = entity.getLiquidacion();
        var proximo = entity.getProximoServicio();
        return new EjecucionDeMantenimiento(entity.getEjecucionId(), entity.getOrdenId(), entity.getTecnicoId(),
                entity.getEstado(), entity.getFechaInicio(), entity.getFechaFin(),
                new LiquidacionDeServicio(liquidacion.getSubtotalProductos(), liquidacion.getSubtotalManoObra(), liquidacion.getTotal()),
                proximo == null ? null : new ProgramacionDeProximoServicio(proximo.getFechaSugerida(), proximo.getKilometrajeSugerido()),
                entity.getTareas().stream().map(t -> new TareaEjecutada(
                        t.getTareaId(), t.getTipoTarea(), t.getEstado(), t.getInicio(), t.getFin())).toList(),
                entity.getConsumos().stream().map(c -> new ConsumoReal(c.getConsumoId(), c.getProductoId(),
                        c.getPresentacionId(), c.getCantidadReal(), c.getPrecioAplicado())).toList());
    }
}

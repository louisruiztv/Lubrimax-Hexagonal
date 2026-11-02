package org.lubrimax.com.infrastructure.adapter.out.persistence.mapper;

import org.lubrimax.com.domain.model.*;
import org.lubrimax.com.infrastructure.adapter.out.persistence.entity.*;
import org.springframework.stereotype.Component;

@Component
public class ServicioDeMantenimientoPersistenceMapper {
    // Preserve the managed collection: JPA tracks its contents and positional order.
    public void updateEntity(ServicioDeMantenimiento servicio, ServicioDeMantenimientoEntity entity) {
        entity.setNombre(servicio.getNombre());
        entity.setTipoServicio(servicio.getTipoServicio());
        entity.setDescripcion(servicio.getDescripcion());
        entity.setEstado(servicio.getEstado());
        entity.getRequiere().clear();
        servicio.getRequiere().forEach(tipo -> entity.getRequiere().add(
                new TipoProductoRequeridoEmbeddable(tipo.categoriaProducto(), tipo.tipoProducto(), tipo.obligatorio())));
    }

    public ServicioDeMantenimiento toModel(ServicioDeMantenimientoEntity entity) {
        return new ServicioDeMantenimiento(entity.getServicioId(), entity.getNombre(),
                entity.getTipoServicio(), entity.getDescripcion(), entity.getEstado(),
                entity.getRequiere().stream().map(tipo -> new TipoProductoRequerido(
                        tipo.getCategoriaProducto(), tipo.getTipoProducto(), tipo.getObligatorio())).toList());
    }
}

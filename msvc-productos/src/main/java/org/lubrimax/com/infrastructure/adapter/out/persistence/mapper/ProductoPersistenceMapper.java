package org.lubrimax.com.infrastructure.adapter.out.persistence.mapper;

import org.lubrimax.com.domain.model.*;
import org.lubrimax.com.infrastructure.adapter.out.persistence.entity.*;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ProductoPersistenceMapper {
    // Update managed entities in place so identity, parent links and orphan removal are retained.
    public void updateEntity(Producto producto, ProductoEntity entity) {
        entity.setCodigo(producto.getCodigo());
        entity.setNombre(producto.getNombre());
        entity.setDescripcion(producto.getDescripcion());
        entity.setMarca(producto.getMarca());
        entity.setCategoria(producto.getCategoria());
        entity.setTipo(producto.getTipo());
        entity.setEstado(producto.getEstado());
        Map<Long, PresentacionDeProductoEntity> existentes = entity.getPresentaciones().stream()
                .collect(Collectors.toMap(PresentacionDeProductoEntity::getPresentacionId, p -> p));
        Set<Long> recibidos = producto.getPresentaciones().stream()
                .map(PresentacionDeProducto::getPresentacionId).filter(Objects::nonNull).collect(Collectors.toSet());
        entity.getPresentaciones().removeIf(p -> !recibidos.contains(p.getPresentacionId()));
        for (PresentacionDeProducto presentacion : producto.getPresentaciones()) {
            PresentacionDeProductoEntity hija;
            if (presentacion.getPresentacionId() == null) {
                hija = new PresentacionDeProductoEntity();
                hija.setProducto(entity);
                entity.getPresentaciones().add(hija);
            } else {
                hija = existentes.get(presentacion.getPresentacionId());
                if (hija == null) {
                    throw new IllegalArgumentException("La presentación no pertenece al producto.");
                }
            }
            hija.setDescripcion(presentacion.getDescripcion());
            hija.setContenido(presentacion.getContenido());
            hija.setUnidadMedida(presentacion.getUnidadMedida());
            hija.setEstado(presentacion.getEstado());
        }
    }

    public Producto toModel(ProductoEntity entity) {
        return new Producto(entity.getProductoId(), entity.getCodigo(), entity.getNombre(),
                entity.getDescripcion(), entity.getMarca(), entity.getCategoria(), entity.getTipo(),
                entity.getEstado(), entity.getPresentaciones().stream()
                    .map(p -> new PresentacionDeProducto(p.getPresentacionId(), p.getDescripcion(),
                            p.getContenido(), p.getUnidadMedida(), p.getEstado())).toList());
    }
}

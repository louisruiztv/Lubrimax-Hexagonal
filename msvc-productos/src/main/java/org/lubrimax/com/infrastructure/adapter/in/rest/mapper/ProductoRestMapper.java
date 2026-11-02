package org.lubrimax.com.infrastructure.adapter.in.rest.mapper;

import org.lubrimax.com.domain.model.*;
import org.lubrimax.com.infrastructure.adapter.in.rest.dto.*;
import org.springframework.stereotype.Component;

@Component
public class ProductoRestMapper {
    public Producto toDomain(ProductoDto dto) {
        return new Producto(dto.getProductoId(), dto.getCodigo(), dto.getNombre(), dto.getDescripcion(),
                dto.getMarca(), dto.getCategoria(), dto.getTipo(), dto.getEstado(),
                dto.getPresentaciones() == null ? null : dto.getPresentaciones().stream()
                        .map(p -> p == null ? null : new PresentacionDeProducto(p.getPresentacionId(),
                                p.getDescripcion(), p.getContenido(), p.getUnidadMedida(), p.getEstado())).toList());
    }

    public ProductoDto toDto(Producto model) {
        return new ProductoDto(model.getProductoId(), model.getCodigo(), model.getNombre(), model.getDescripcion(),
                model.getMarca(), model.getCategoria(), model.getTipo(), model.getEstado(),
                model.getPresentaciones() == null ? null : model.getPresentaciones().stream()
                        .map(p -> p == null ? null : new PresentacionDeProductoDto(p.getPresentacionId(),
                                p.getDescripcion(), p.getContenido(), p.getUnidadMedida(), p.getEstado())).toList());
    }
}

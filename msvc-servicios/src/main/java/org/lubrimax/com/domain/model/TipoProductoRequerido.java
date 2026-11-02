package org.lubrimax.com.domain.model;
public record TipoProductoRequerido(
        String categoriaProducto,
        String tipoProducto,
        Boolean obligatorio) {
}

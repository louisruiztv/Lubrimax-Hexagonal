package org.lubrimax.com.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TipoProductoRequeridoEmbeddable {
    @Column(name = "categoria_producto", nullable = false, length = 100)
    private String categoriaProducto;
    @Column(name = "tipo_producto", nullable = false, length = 100)
    private String tipoProducto;
    @Column(nullable = false)
    private Boolean obligatorio;
}

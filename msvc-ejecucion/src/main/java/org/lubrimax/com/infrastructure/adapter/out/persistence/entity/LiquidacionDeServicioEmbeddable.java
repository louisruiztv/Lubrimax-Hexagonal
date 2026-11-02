package org.lubrimax.com.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LiquidacionDeServicioEmbeddable {
    @Column(name = "subtotal_productos", nullable = false, precision = 19, scale = 2)
    private BigDecimal subtotalProductos;
    @Column(name = "subtotal_mano_obra", nullable = false, precision = 19, scale = 2)
    private BigDecimal subtotalManoObra;
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal total;
}

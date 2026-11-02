package org.lubrimax.com.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "consumo_real")
@NoArgsConstructor
@Getter
@Setter
public class ConsumoRealEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long consumoId;
    @Column(name = "producto_id", nullable = false)
    private Long productoId;
    @Column(name = "presentacion_id", nullable = false)
    private Long presentacionId;
    @Column(name = "cantidad_real", nullable = false, precision = 12, scale = 3)
    private BigDecimal cantidadReal;
    @Column(name = "precio_aplicado", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioAplicado;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ejecucion_id", nullable = false)
    private EjecucionDeMantenimientoEntity ejecucion;
}

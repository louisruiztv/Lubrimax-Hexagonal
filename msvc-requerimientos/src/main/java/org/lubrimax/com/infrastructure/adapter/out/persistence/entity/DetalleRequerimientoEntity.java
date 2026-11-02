package org.lubrimax.com.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.lubrimax.com.domain.model.EstadoDisponibilidad;
import java.math.BigDecimal;

@Entity
@Table(name = "detalle_requerimiento")
@NoArgsConstructor
@Getter
@Setter
public class DetalleRequerimientoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long detalleId;
    @Column(name = "producto_id", nullable = false)
    private Long productoId;
    @Column(name = "presentacion_id", nullable = false)
    private Long presentacionId;
    @Column(name = "cantidad_requerida", nullable = false, precision = 12, scale = 3)
    private BigDecimal cantidadRequerida;
    @Column(name = "cantidad_disponible", nullable = false, precision = 12, scale = 3)
    private BigDecimal cantidadDisponible;
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_disponibilidad", nullable = false, length = 20)
    private EstadoDisponibilidad estadoDisponibilidad;
    @Column(nullable = false)
    private Boolean reservado;
    @Column(length = 1000)
    private String observacion;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "requerimiento_id", nullable = false)
    private RequerimientoDeMantenimientoEntity requerimiento;
}

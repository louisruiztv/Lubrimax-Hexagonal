package org.lubrimax.com.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "presentacion_producto")
@NoArgsConstructor
@Getter
@Setter
public class PresentacionDeProductoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long presentacionId;
    @Column(nullable = false, length = 250)
    private String descripcion;
    @Column(nullable = false, precision = 12, scale = 3)
    private BigDecimal contenido;
    @Column(name = "unidad_medida", nullable = false, length = 30)
    private String unidadMedida;
    @Column(nullable = false, length = 50)
    private String estado;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "producto_id", nullable = false)
    private ProductoEntity producto;
}

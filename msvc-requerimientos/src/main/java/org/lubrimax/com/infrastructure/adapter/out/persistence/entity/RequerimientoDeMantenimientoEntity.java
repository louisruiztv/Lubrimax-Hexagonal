package org.lubrimax.com.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "requerimiento_mantenimiento")
@NoArgsConstructor
@Getter
@Setter
public class RequerimientoDeMantenimientoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requerimientoId;
    @Column(name = "orden_id", nullable = false)
    private Long ordenId;
    @Column(nullable = false, length = 50)
    private String estado;
    @OneToMany(mappedBy = "requerimiento", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("detalleId ASC")
    private List<DetalleRequerimientoEntity> detalles = new ArrayList<>();
}

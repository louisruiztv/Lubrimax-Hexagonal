package org.lubrimax.com.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ejecucion_mantenimiento",
        uniqueConstraints = @UniqueConstraint(name = "uk_ejecucion_orden", columnNames = "orden_id"))
@NoArgsConstructor
@Getter
@Setter
public class EjecucionDeMantenimientoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ejecucionId;
    @Column(name = "orden_id", nullable = false)
    private Long ordenId;
    @Column(name = "tecnico_id", nullable = false)
    private Long tecnicoId;
    @Column(nullable = false, length = 50)
    private String estado;
    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;
    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;
    @Embedded
    private LiquidacionDeServicioEmbeddable liquidacion;
    @Embedded
    private ProgramacionDeProximoServicioEmbeddable proximoServicio;
    @OneToMany(mappedBy = "ejecucion", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("tareaId ASC")
    private List<TareaEjecutadaEntity> tareas = new ArrayList<>();
    @OneToMany(mappedBy = "ejecucion", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("consumoId ASC")
    private List<ConsumoRealEntity> consumos = new ArrayList<>();
}

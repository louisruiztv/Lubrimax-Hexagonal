package org.lubrimax.com.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "tarea_ejecutada")
@NoArgsConstructor
@Getter
@Setter
public class TareaEjecutadaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tareaId;
    @Column(name = "tipo_tarea", nullable = false, length = 100)
    private String tipoTarea;
    @Column(nullable = false, length = 50)
    private String estado;
    @Column
    private LocalDateTime inicio;
    private LocalDateTime fin;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ejecucion_id", nullable = false)
    private EjecucionDeMantenimientoEntity ejecucion;
}

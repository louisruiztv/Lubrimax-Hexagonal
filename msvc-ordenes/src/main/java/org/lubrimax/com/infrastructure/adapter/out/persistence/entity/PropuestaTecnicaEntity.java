package org.lubrimax.com.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "propuesta_tecnica")
@NoArgsConstructor
@Getter
@Setter
public class PropuestaTecnicaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long propuestaId;
    @Column(nullable = false, length = 50)
    private String estado;
    @Column(length = 1000)
    private String observaciones;
    @ElementCollection
    @CollectionTable(name = "propuesta_servicios", joinColumns = @JoinColumn(name = "propuesta_id"))
    @OrderColumn(name = "posicion")
    @Column(name = "servicio_id", nullable = false)
    private List<Long> servicioIds = new ArrayList<>();
}

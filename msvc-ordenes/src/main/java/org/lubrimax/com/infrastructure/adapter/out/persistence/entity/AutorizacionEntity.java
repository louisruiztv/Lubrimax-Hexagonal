package org.lubrimax.com.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "autorizacion")
@NoArgsConstructor
@Getter
@Setter
public class AutorizacionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long autorizacionId;
    @Column(nullable = false, length = 100)
    private String medio;
    @Column(nullable = false, length = 500)
    private String alcance;
    @Column(nullable = false)
    private LocalDateTime fecha;
    @ElementCollection
    @CollectionTable(name = "autorizacion_servicios", joinColumns = @JoinColumn(name = "autorizacion_id"))
    @OrderColumn(name = "posicion")
    @Column(name = "servicio_id", nullable = false)
    private List<Long> servicioIds = new ArrayList<>();
}

package org.lubrimax.com.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProgramacionDeProximoServicioEmbeddable {
    @Column(name = "fecha_sugerida")
    private LocalDate fechaSugerida;
    @Column(name = "kilometraje_sugerido")
    private Integer kilometrajeSugerido;
}

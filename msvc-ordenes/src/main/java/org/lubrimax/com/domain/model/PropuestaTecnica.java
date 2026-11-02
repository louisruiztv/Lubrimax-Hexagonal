package org.lubrimax.com.domain.model;

import lombok.*;
import java.util.List;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PropuestaTecnica {
    private Long propuestaId;
    private String estado;
    private String observaciones;
    private List<Long> servicioIds;
}

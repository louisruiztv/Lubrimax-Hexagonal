package org.lubrimax.com.domain.model;

import lombok.*;
import java.util.List;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Autorizacion {
    private Long autorizacionId;
    private String medio;
    private String alcance;
    private LocalDateTime fecha;
    private List<Long> servicioIds;
}

package org.lubrimax.com.infrastructure.adapter.in.rest.dto;

import lombok.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PropuestaTecnicaDto {
    private Long propuestaId;
    private String estado;
    private String observaciones;
    private List<Long> servicioIds;
}

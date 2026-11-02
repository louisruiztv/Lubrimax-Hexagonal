package org.lubrimax.com.infrastructure.adapter.in.rest.dto;

import lombok.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TareaEjecutadaDto {
    private Long tareaId;
    private String tipoTarea;
    private String estado;
    private LocalDateTime inicio;
    private LocalDateTime fin;

}

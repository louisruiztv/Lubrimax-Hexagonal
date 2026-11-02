package org.lubrimax.com.infrastructure.adapter.in.rest.dto;

import lombok.*;
import java.util.List;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AutorizacionDto {
    private Long autorizacionId;
    private String medio;
    private String alcance;
    private LocalDateTime fecha;
    private List<Long> servicioIds;
}

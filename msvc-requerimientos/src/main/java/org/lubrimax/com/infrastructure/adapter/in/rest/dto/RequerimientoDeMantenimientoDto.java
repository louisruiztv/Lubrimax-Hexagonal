package org.lubrimax.com.infrastructure.adapter.in.rest.dto;

import lombok.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RequerimientoDeMantenimientoDto {
    private Long requerimientoId;
    private Long ordenId;
    private String estado;
    private List<DetalleRequerimientoDto> detalles;
}

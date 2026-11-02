package org.lubrimax.com.infrastructure.adapter.in.rest.dto;

import lombok.*;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PresentacionDeProductoDto {
    private Long presentacionId;
    private String descripcion;
    private BigDecimal contenido;
    private String unidadMedida;
    private String estado;
}

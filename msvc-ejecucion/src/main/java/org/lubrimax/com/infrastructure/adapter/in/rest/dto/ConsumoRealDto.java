package org.lubrimax.com.infrastructure.adapter.in.rest.dto;

import lombok.*;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ConsumoRealDto {
    private Long consumoId;
    private Long productoId;
    private Long presentacionId;
    private BigDecimal cantidadReal;
    private BigDecimal precioAplicado;

}

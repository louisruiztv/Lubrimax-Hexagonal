package org.lubrimax.com.infrastructure.adapter.in.rest.dto;

import lombok.*;
import java.math.BigDecimal;
import org.lubrimax.com.domain.model.EstadoDisponibilidad;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DetalleRequerimientoDto {
    private Long detalleId;
    private Long productoId;
    private Long presentacionId;
    private BigDecimal cantidadRequerida;
    private BigDecimal cantidadDisponible;
    private EstadoDisponibilidad estadoDisponibilidad;
    private Boolean reservado;
    private String observacion;
}

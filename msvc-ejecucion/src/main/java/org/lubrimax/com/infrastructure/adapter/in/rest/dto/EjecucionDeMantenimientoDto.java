package org.lubrimax.com.infrastructure.adapter.in.rest.dto;

import lombok.*;
import java.util.List;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class EjecucionDeMantenimientoDto {
    private Long ejecucionId;
    private Long ordenId;
    private Long tecnicoId;
    private String estado;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private LiquidacionDeServicioDto liquidacion;
    private ProgramacionDeProximoServicioDto proximoServicio;
    private List<TareaEjecutadaDto> tareas;
    private List<ConsumoRealDto> consumos;

}

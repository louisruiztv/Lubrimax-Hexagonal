package org.lubrimax.com.infrastructure.adapter.in.rest.dto;

import lombok.*;
import java.util.List;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrdenDeServicioDto {
    private Long ordenId;
    private Long vehiculoId;
    private Integer kilometraje;
    private String mantenimientoSolicitado;
    private String estado;
    private LocalDateTime fecha;
    private List<Long> servicioIds;
    private DatosDelClienteDto datosCliente;
    private PropuestaTecnicaDto propuestaTecnica;
    private AutorizacionDto autorizacion;
}

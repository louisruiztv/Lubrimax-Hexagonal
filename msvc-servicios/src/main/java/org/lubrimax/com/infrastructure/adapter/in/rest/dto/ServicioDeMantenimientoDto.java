package org.lubrimax.com.infrastructure.adapter.in.rest.dto;

import lombok.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ServicioDeMantenimientoDto {
    private Long servicioId;
    private String nombre;
    private String tipoServicio;
    private String descripcion;
    private String estado;
    private List<TipoProductoRequeridoDto> requiere;
}

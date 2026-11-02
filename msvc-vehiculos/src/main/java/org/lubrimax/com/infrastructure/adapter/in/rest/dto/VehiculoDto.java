package org.lubrimax.com.infrastructure.adapter.in.rest.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VehiculoDto {
    private Long vehiculoId;
    private String placa;
    private String marca;
    private String modelo;
    private Integer anio;
    private String tipoCombustible;
    private Integer kilometrajeActual;
    private String estado;
    private FichaTecnicaDto fichaTecnica;
}

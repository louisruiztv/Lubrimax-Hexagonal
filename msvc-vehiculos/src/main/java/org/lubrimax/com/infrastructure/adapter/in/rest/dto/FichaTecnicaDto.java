package org.lubrimax.com.infrastructure.adapter.in.rest.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FichaTecnicaDto {
    private String marca;
    private String modelo;
    private Integer anio;
    private String motor;
    private String cilindrada;
    private String tipoCombustible;
}

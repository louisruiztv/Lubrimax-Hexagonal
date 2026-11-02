package org.lubrimax.com.domain.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class FichaTecnica {
    private String marca;
    private String modelo;
    private Integer anio;
    private String motor;
    private String cilindrada;
    private String tipoCombustible;
}

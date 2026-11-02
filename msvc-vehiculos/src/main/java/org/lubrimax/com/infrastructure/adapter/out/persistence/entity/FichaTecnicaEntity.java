package org.lubrimax.com.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FichaTecnicaEntity {
    @Column(name = "ficha_marca", nullable = false, length = 100)
    private String marca;
    @Column(name = "ficha_modelo", nullable = false, length = 100)
    private String modelo;
    @Column(name = "ficha_anio", nullable = false)
    private Integer anio;
    @Column(name = "motor", nullable = false, length = 100)
    private String motor;
    @Column(name = "cilindrada", nullable = false, length = 50)
    private String cilindrada;
    @Column(name = "ficha_tipo_combustible", nullable = false, length = 50)
    private String tipoCombustible;
}

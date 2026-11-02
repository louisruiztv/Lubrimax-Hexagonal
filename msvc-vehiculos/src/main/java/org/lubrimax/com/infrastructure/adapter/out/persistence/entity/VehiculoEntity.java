package org.lubrimax.com.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vehiculo", uniqueConstraints = @UniqueConstraint(name = "uk_vehiculo_placa", columnNames = "placa"))
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class VehiculoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vehiculoId;
    @Column(nullable = false, length = 20)
    private String placa;
    @Column(nullable = false, length = 100)
    private String marca;
    @Column(nullable = false, length = 100)
    private String modelo;
    @Column(nullable = false)
    private Integer anio;
    @Column(name = "tipo_combustible", nullable = false, length = 50)
    private String tipoCombustible;
    @Column(name = "kilometraje_actual", nullable = false)
    private Integer kilometrajeActual;
    @Column(nullable = false, length = 50)
    private String estado;
    @Embedded
    private FichaTecnicaEntity fichaTecnica;
}

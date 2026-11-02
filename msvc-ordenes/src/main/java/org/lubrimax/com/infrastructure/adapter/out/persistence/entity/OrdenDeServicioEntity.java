package org.lubrimax.com.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orden_servicio")
@NoArgsConstructor
@Getter
@Setter
public class OrdenDeServicioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ordenId;
    @Column(name = "vehiculo_id", nullable = false)
    private Long vehiculoId;
    @Column(nullable = false)
    private Integer kilometraje;
    @Column(name = "mantenimiento_solicitado", nullable = false, length = 500)
    private String mantenimientoSolicitado;
    @Column(nullable = false, length = 50)
    private String estado;
    @Column(nullable = false)
    private LocalDateTime fecha;
    @Embedded
    private DatosDelClienteEmbeddable datosCliente;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "propuesta_id", unique = true)
    private PropuestaTecnicaEntity propuestaTecnica;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "autorizacion_id", unique = true)
    private AutorizacionEntity autorizacion;
    @ElementCollection
    @CollectionTable(name = "orden_servicios", joinColumns = @JoinColumn(name = "orden_id"))
    @OrderColumn(name = "posicion")
    @Column(name = "servicio_id", nullable = false)
    private List<Long> servicioIds = new ArrayList<>();
}

package org.lubrimax.com.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "servicio_mantenimiento")
@NoArgsConstructor
@Getter
@Setter
public class ServicioDeMantenimientoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long servicioId;
    @Column(nullable = false, length = 150)
    private String nombre;
    @Column(name = "tipo_servicio", nullable = false, length = 100)
    private String tipoServicio;
    @Column(nullable = false, length = 500)
    private String descripcion;
    @Column(nullable = false, length = 50)
    private String estado;
    @ElementCollection
    @CollectionTable(name = "servicio_tipo_producto_requerido",
            joinColumns = @JoinColumn(name = "servicio_id", nullable = false))
    @OrderColumn(name = "posicion")
    private List<TipoProductoRequeridoEmbeddable> requiere = new ArrayList<>();
}

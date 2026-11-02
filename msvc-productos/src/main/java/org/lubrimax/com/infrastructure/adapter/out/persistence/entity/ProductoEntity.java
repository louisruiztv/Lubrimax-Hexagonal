package org.lubrimax.com.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "producto", uniqueConstraints = @UniqueConstraint(name = "uk_producto_codigo", columnNames = "codigo"))
@NoArgsConstructor
@Getter
@Setter
public class ProductoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productoId;
    @Column(nullable = false, length = 50)
    private String codigo;
    @Column(nullable = false, length = 150)
    private String nombre;
    @Column(nullable = false, length = 500)
    private String descripcion;
    @Column(nullable = false, length = 100)
    private String marca;
    @Column(nullable = false, length = 100)
    private String categoria;
    @Column(nullable = false, length = 100)
    private String tipo;
    @Column(nullable = false, length = 50)
    private String estado;
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("presentacionId ASC")
    private List<PresentacionDeProductoEntity> presentaciones = new ArrayList<>();
}

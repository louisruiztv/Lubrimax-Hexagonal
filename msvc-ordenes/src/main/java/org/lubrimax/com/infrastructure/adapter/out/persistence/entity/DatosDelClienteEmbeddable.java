package org.lubrimax.com.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DatosDelClienteEmbeddable {
    @Column(name = "cliente_nombre", nullable = false, length = 150)
    private String nombre;
    @Column(name = "cliente_telefono", nullable = false, length = 30)
    private String telefono;
    @Column(name = "cliente_documento", nullable = false, length = 30)
    private String documento;
}

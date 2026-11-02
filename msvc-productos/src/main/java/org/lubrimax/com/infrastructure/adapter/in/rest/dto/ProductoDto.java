package org.lubrimax.com.infrastructure.adapter.in.rest.dto;

import lombok.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductoDto {
    private Long productoId;
    private String codigo;
    private String nombre;
    private String descripcion;
    private String marca;
    private String categoria;
    private String tipo;
    private String estado;
    private List<PresentacionDeProductoDto> presentaciones;
}

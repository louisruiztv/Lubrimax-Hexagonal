package org.lubrimax.com.domain.model;

import lombok.*;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PresentacionDeProducto {
    private Long presentacionId;
    private String descripcion;
    private BigDecimal contenido;
    private String unidadMedida;
    private String estado;

    public void actualizarDatos(String descripcion, BigDecimal contenido, String unidadMedida, String estado) {
        validarDatos(descripcion, contenido, unidadMedida, estado);
        this.descripcion = descripcion;
        this.contenido = contenido;
        this.unidadMedida = unidadMedida;
        this.estado = estado;
    }

    static void validarDatos(String descripcion, BigDecimal contenido, String unidadMedida, String estado) {
        texto(descripcion, "presentacion.descripcion", 250);
        texto(unidadMedida, "presentacion.unidadMedida", 30);
        texto(estado, "presentacion.estado", 50);
        if (contenido == null || contenido.signum() <= 0
                || contenido.stripTrailingZeros().scale() > 3
                || contenido.compareTo(new BigDecimal("999999999.999")) > 0) {
            throw new IllegalArgumentException("El contenido debe ser positivo, hasta 999999999.999 y con máximo tres decimales.");
        }
    }

    private static void texto(String valor, String campo, int maximo) {
        if (valor == null || valor.isBlank() || valor.length() > maximo) {
            throw new IllegalArgumentException("El campo " + campo + " es obligatorio y admite hasta " + maximo + " caracteres.");
        }
    }
}

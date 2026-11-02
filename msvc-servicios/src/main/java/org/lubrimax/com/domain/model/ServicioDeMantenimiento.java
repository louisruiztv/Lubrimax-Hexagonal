package org.lubrimax.com.domain.model;

import lombok.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ServicioDeMantenimiento {
    private Long servicioId;
    private String nombre;
    private String tipoServicio;
    private String descripcion;
    private String estado;
    private List<TipoProductoRequerido> requiere;

    public void validarDatos() {
        texto(nombre, "nombre", 150);
        texto(tipoServicio, "tipoServicio", 100);
        texto(descripcion, "descripcion", 500);
        texto(estado, "estado", 50);
        definirTiposRequeridos(requiere);
    }

    public void definirTiposRequeridos(List<TipoProductoRequerido> tipos) {
        if (tipos == null) {
            throw new IllegalArgumentException("Se debe enviar requiere, aunque sea una lista vacía.");
        }
        for (TipoProductoRequerido tipo : tipos) {
            if (tipo == null) {
                throw new IllegalArgumentException("La lista requiere no admite elementos nulos.");
            }
            texto(tipo.categoriaProducto(), "requiere.categoriaProducto", 100);
            texto(tipo.tipoProducto(), "requiere.tipoProducto", 100);
            if (tipo.obligatorio() == null) {
                throw new IllegalArgumentException("El campo requiere.obligatorio debe ser true o false.");
            }
        }
        requiere = List.copyOf(tipos);
    }

    private void texto(String valor, String campo, int maximo) {
        if (valor == null || valor.isBlank() || valor.length() > maximo) {
            throw new IllegalArgumentException("El campo " + campo + " es obligatorio y admite hasta " + maximo + " caracteres.");
        }
    }
}

package org.lubrimax.com.domain.model;

import lombok.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Producto {
    private Long productoId;
    private String codigo;
    private String nombre;
    private String descripcion;
    private String marca;
    private String categoria;
    private String tipo;
    private String estado;
    private List<PresentacionDeProducto> presentaciones;

    public void agregarPresentacion(PresentacionDeProducto presentacion) {
        if (presentacion == null) {
            throw new IllegalArgumentException("La lista no admite presentaciones nulas.");
        }
        PresentacionDeProducto.validarDatos(presentacion.getDescripcion(), presentacion.getContenido(),
                presentacion.getUnidadMedida(), presentacion.getEstado());
        List<PresentacionDeProducto> actuales = presentaciones == null ? List.of() : presentaciones;
        Long id = presentacion.getPresentacionId();
        if (id != null && actuales.stream().anyMatch(p -> id.equals(p.getPresentacionId()))) {
            throw new IllegalArgumentException("El ID de presentación está repetido.");
        }
        List<PresentacionDeProducto> nuevas = new ArrayList<>(actuales);
        nuevas.add(presentacion);
        presentaciones = nuevas;
    }

    public void eliminarPresentacion(Long presentacionId) {
        if (presentacionId == null) {
            throw new IllegalArgumentException("El ID de presentación es obligatorio.");
        }
        if (presentaciones == null || presentaciones.stream()
                .noneMatch(p -> presentacionId.equals(p.getPresentacionId()))) {
            throw new IllegalArgumentException("La presentación no pertenece al producto.");
        }
        List<PresentacionDeProducto> restantes = new ArrayList<>(presentaciones);
        restantes.removeIf(p -> presentacionId.equals(p.getPresentacionId()));
        presentaciones = restantes;
    }

    public void validarDatos(Set<Long> idsPermitidos) {
        texto(this.getCodigo(), "codigo", 50);
        this.setCodigo(this.getCodigo().trim().toUpperCase(Locale.ROOT));
        texto(this.getNombre(), "nombre", 150);
        texto(this.getDescripcion(), "descripcion", 500);
        texto(this.getMarca(), "marca", 100);
        texto(this.getCategoria(), "categoria", 100);
        texto(this.getTipo(), "tipo", 100);
        texto(this.getEstado(), "estado", 50);
        if (this.getPresentaciones() == null) {
            throw new IllegalArgumentException("Se debe enviar la lista de presentaciones, aunque esté vacía.");
        }
        Producto validado = new Producto();
        for (PresentacionDeProducto presentacion : this.getPresentaciones()) {
            if (presentacion == null) {
                throw new IllegalArgumentException("La lista no admite presentaciones nulas.");
            }
            Long id = presentacion.getPresentacionId();
            if (id != null && !idsPermitidos.contains(id)) {
                throw new IllegalArgumentException("El ID de presentación es ajeno, inexistente o está repetido.");
            }
            validado.agregarPresentacion(presentacion);
        }
    }

    private void texto(String valor, String campo, int maximo) {
        if (valor == null || valor.isBlank() || valor.length() > maximo) {
            throw new IllegalArgumentException("El campo " + campo + " es obligatorio y admite hasta " + maximo + " caracteres.");
        }
    }
}

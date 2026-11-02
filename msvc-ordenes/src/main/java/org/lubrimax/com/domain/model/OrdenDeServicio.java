package org.lubrimax.com.domain.model;

import lombok.*;
import java.util.List;
import java.util.LinkedHashSet;
import java.util.Set;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrdenDeServicio {
    private Long ordenId;
    private Long vehiculoId;
    private Integer kilometraje;
    private String mantenimientoSolicitado;
    private String estado;
    private LocalDateTime fecha;
    private List<Long> servicioIds;
    private DatosDelCliente datosCliente;
    private PropuestaTecnica propuestaTecnica;
    private Autorizacion autorizacion;

    public void validarDatos() {
        if (vehiculoId == null || vehiculoId <= 0) {
            throw new IllegalArgumentException("vehiculoId debe ser positivo.");
        }
        if (kilometraje == null || kilometraje < 0) {
            throw new IllegalArgumentException("El kilometraje debe ser cero o mayor.");
        }
        texto(mantenimientoSolicitado, "mantenimientoSolicitado", 500);
        texto(estado, "estado", 50);
        estado = EstadoOrden.desde(estado).name();
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha es obligatoria.");
        }
        if (datosCliente == null) {
            throw new IllegalArgumentException("Los datosCliente son obligatorios.");
        }
        texto(datosCliente.nombre(), "datosCliente.nombre", 150);
        texto(datosCliente.telefono(), "datosCliente.telefono", 30);
        texto(datosCliente.documento(), "datosCliente.documento", 30);
        ids(servicioIds, "servicioIds");
        validarPropuesta(propuestaTecnica);
        validarAutorizacion(autorizacion, propuestaTecnica);
        validarEstadoConAutorizacion(autorizacion);
    }

    public void registrarPropuesta(PropuestaTecnica propuesta) {
        validarPropuesta(propuesta);
        validarAutorizacion(autorizacion, propuesta);
        if (propuesta == null) {
            propuestaTecnica = null;
            return;
        }
        Long id = identidad(propuesta.getPropuestaId(),
                propuestaTecnica == null ? null : propuestaTecnica.getPropuestaId(), "propuestaId");
        propuestaTecnica = new PropuestaTecnica(id, propuesta.getEstado(), propuesta.getObservaciones(),
                List.copyOf(propuesta.getServicioIds()));
    }

    public void registrarAutorizacion(Autorizacion nueva) {
        validarEstadoConAutorizacion(nueva);
        validarPropuesta(propuestaTecnica);
        validarAutorizacion(nueva, propuestaTecnica);
        if (nueva == null) {
            autorizacion = null;
            return;
        }
        Long id = identidad(nueva.getAutorizacionId(),
                autorizacion == null ? null : autorizacion.getAutorizacionId(), "autorizacionId");
        autorizacion = new Autorizacion(id, nueva.getMedio(), nueva.getAlcance(), nueva.getFecha(),
                List.copyOf(nueva.getServicioIds()));
    }

    public void validarEditable() {
        EstadoOrden actual = EstadoOrden.desde(estado);
        if (actual == EstadoOrden.CANCELADA || actual == EstadoOrden.EN_PROCESO) {
            throw new IllegalStateException("No se puede editar una orden " + actual + ".");
        }
        if (estado != null && "FINALIZADA".equalsIgnoreCase(estado.trim())) {
            throw new IllegalStateException("Una orden finalizada no se puede editar.");
        }
    }

    public void autorizar(Autorizacion nueva) {
        validarEditable();
        if (nueva == null) throw new IllegalArgumentException("La autorización es obligatoria.");
        validarTransicion(EstadoOrden.AUTORIZADA);
        registrarAutorizacion(nueva);
        estado = EstadoOrden.AUTORIZADA.name();
    }

    public void validarTransicion(EstadoOrden destino) {
        EstadoOrden actual = EstadoOrden.desde(estado);
        if (!actual.permite(destino)) {
            throw new IllegalStateException("No se permite cambiar la orden de " + actual + " a " + destino + ".");
        }
    }

    public void sincronizarEjecucion(String estadoEjecucion) {
        EstadoOrden actual = EstadoOrden.desde(estado);
        EstadoOrden destino = switch (estadoEjecucion) {
            case "PENDIENTE" -> EstadoOrden.AUTORIZADA;
            case "EN_PROCESO" -> EstadoOrden.EN_PROCESO;
            case "FINALIZADA" -> EstadoOrden.FINALIZADA;
            case "CANCELADA" -> EstadoOrden.CANCELADA;
            default -> throw new IllegalArgumentException("Estado de ejecución inválido.");
        };
        if (actual == destino) return;
        if (!estaAutorizada()) throw new IllegalStateException("La orden requiere una autorización válida.");
        // Una notificación anterior puede haber fallado. El estado persistido final
        // permite recuperar el avance omitido sin reabrir estados terminales.
        if (actual == EstadoOrden.AUTORIZADA && destino == EstadoOrden.FINALIZADA) {
            estado = EstadoOrden.FINALIZADA.name();
            return;
        }
        validarTransicion(destino);
        estado = destino.name();
    }

    public boolean estaAutorizada() {
        if (estado == null || !Set.of("AUTORIZADA", "EN_PROCESO", "FINALIZADA").contains(estado.trim().toUpperCase(java.util.Locale.ROOT)) || autorizacion == null) {
            return false;
        }
        try {
            validarPropuesta(propuestaTecnica);
            validarAutorizacion(autorizacion, propuestaTecnica);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    private boolean estadoAutorizada() {
        return estado != null && "AUTORIZADA".equalsIgnoreCase(estado.trim());
    }

    private void validarEstadoConAutorizacion(Autorizacion nueva) {
        if (nueva == null && estadoAutorizada()) {
            throw new IllegalArgumentException("Una orden AUTORIZADA requiere una autorización.");
        }
    }

    private void validarPropuesta(PropuestaTecnica propuesta) {
        if (propuesta == null) {
            return;
        }
        texto(propuesta.getEstado(), "propuestaTecnica.estado", 50);
        if (propuesta.getObservaciones() != null && propuesta.getObservaciones().length() > 1000) {
            throw new IllegalArgumentException("Las observaciones admiten hasta 1000 caracteres.");
        }
        ids(propuesta.getServicioIds(), "propuestaTecnica.servicioIds");
    }

    private void validarAutorizacion(Autorizacion nueva, PropuestaTecnica propuesta) {
        if (nueva == null) {
            return;
        }
        texto(nueva.getMedio(), "autorizacion.medio", 100);
        texto(nueva.getAlcance(), "autorizacion.alcance", 500);
        if (nueva.getFecha() == null) {
            throw new IllegalArgumentException("La fecha de autorización es obligatoria.");
        }
        Set<Long> autorizados = ids(nueva.getServicioIds(), "autorizacion.servicioIds");
        if (propuesta == null || autorizados.isEmpty()
                || !ids(propuesta.getServicioIds(), "propuestaTecnica.servicioIds").containsAll(autorizados)) {
            throw new IllegalArgumentException("La autorización debe incluir servicios de la propuesta técnica.");
        }
    }

    private Long identidad(Long recibida, Long actual, String campo) {
        if (actual != null && recibida != null && !actual.equals(recibida)) {
            throw new IllegalArgumentException("El " + campo + " no pertenece a esta orden.");
        }
        return actual == null ? recibida : actual;
    }

    private Set<Long> ids(List<Long> lista, String campo) {
        if (lista == null) {
            throw new IllegalArgumentException("La lista " + campo + " es obligatoria; puede estar vacía.");
        }
        Set<Long> resultado = new LinkedHashSet<>();
        for (Long id : lista) {
            if (id == null || id <= 0 || !resultado.add(id)) {
                throw new IllegalArgumentException(campo + " contiene IDs inválidos o repetidos.");
            }
        }
        return resultado;
    }

    private void texto(String valor, String campo, int maximo) {
        if (valor == null || valor.isBlank() || valor.length() > maximo) {
            throw new IllegalArgumentException("El campo " + campo + " es obligatorio y admite hasta " + maximo + " caracteres.");
        }
    }
}

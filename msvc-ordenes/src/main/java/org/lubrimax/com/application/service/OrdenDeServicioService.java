package org.lubrimax.com.application.service;

import lombok.RequiredArgsConstructor;
import org.lubrimax.com.application.port.out.*;
import org.lubrimax.com.domain.model.*;
import org.lubrimax.com.application.port.in.OrdenDeServicioUseCase;
import java.util.*;

@RequiredArgsConstructor
public class OrdenDeServicioService implements OrdenDeServicioUseCase {
    private final OrdenDeServicioRepositoryPort ordenRepositoryPort;
    private final VehiculoClientPort vehiculoClientPort;
    private final ServicioClientPort servicioClientPort;
    private final EjecucionClientPort ejecucionClientPort;

    @Override
    public List<OrdenDeServicio> listar() {
        return ordenRepositoryPort.listar();
    }

    @Override
    public Optional<OrdenDeServicio> buscarPorId(Long id) {
        return ordenRepositoryPort.buscarPorId(id);
    }


    @Override
    public OrdenDeServicio guardarOrden(OrdenDeServicio orden) {
        if (orden.getOrdenId() != null) {
            throw new IllegalArgumentException("El ID de la orden se genera automáticamente.");
        }
        if (orden.getEstado() == null) orden.validarDatos();
        if (EstadoOrden.desde(orden.getEstado()) != EstadoOrden.PENDIENTE) {
            throw new IllegalStateException("Una orden nueva debe estar PENDIENTE.");
        }
        if (orden.getAutorizacion() != null) {
            throw new IllegalStateException("Utilice la acción autorizar para registrar la autorización.");
        }
        validar(orden, null);
        return ordenRepositoryPort.guardarOrden(orden);
    }


    @Override
    public Optional<OrdenDeServicio> actualizar(Long id, OrdenDeServicio orden) {
        return ordenRepositoryPort.buscarPorId(id).map(existente -> {
            existente.validarEditable();
            if (EstadoOrden.desde(existente.getEstado()) != EstadoOrden.PENDIENTE) {
                throw new IllegalStateException("Solo se pueden editar los datos de una orden PENDIENTE.");
            }
            if (EstadoOrden.desde(orden.getEstado()) != EstadoOrden.PENDIENTE || orden.getAutorizacion() != null) {
                throw new IllegalStateException("El estado y la autorización no se pueden cambiar mediante edición general.");
            }
            validar(orden, existente);
            existente.setVehiculoId(orden.getVehiculoId());
            existente.setKilometraje(orden.getKilometraje());
            existente.setMantenimientoSolicitado(orden.getMantenimientoSolicitado());
            existente.setEstado(orden.getEstado());
            existente.setFecha(orden.getFecha());
            existente.setServicioIds(orden.getServicioIds());
            existente.setDatosCliente(orden.getDatosCliente());
            existente.setPropuestaTecnica(orden.getPropuestaTecnica());
            existente.setAutorizacion(orden.getAutorizacion());
            return ordenRepositoryPort.guardarOrden(existente);
        });
    }

    @Override
    public boolean borrarOrden(Long id) {
        var existente = ordenRepositoryPort.buscarPorId(id);
        if (existente.isEmpty()) {
            return false;
        }
        if (EstadoOrden.desde(existente.get().getEstado()) != EstadoOrden.PENDIENTE) {
            throw new IllegalStateException("Solo se puede eliminar una orden PENDIENTE.");
        }
        ordenRepositoryPort.borrarOrden(id);
        return true;
    }

    private void validar(OrdenDeServicio orden, OrdenDeServicio existente) {
        orden.validarDatos();
        Set<Long> referencias = new LinkedHashSet<>(orden.getServicioIds());
        PropuestaTecnica propuesta = orden.getPropuestaTecnica();
        if (propuesta != null) {
            validarPertenencia(propuesta.getPropuestaId(), existente == null || existente.getPropuestaTecnica() == null
                    ? null : existente.getPropuestaTecnica().getPropuestaId(), "propuestaId");
        }
        Autorizacion autorizacion = orden.getAutorizacion();
        if (autorizacion != null) {
            validarPertenencia(autorizacion.getAutorizacionId(), existente == null || existente.getAutorizacion() == null
                    ? null : existente.getAutorizacion().getAutorizacionId(), "autorizacionId");
        }

        OrdenDeServicio candidata = new OrdenDeServicio();
        candidata.registrarPropuesta(propuesta);
        candidata.registrarAutorizacion(autorizacion);
        candidata.setEstado(orden.getEstado());
        if (propuesta != null) {
            referencias.addAll(propuesta.getServicioIds());
        }
        if (autorizacion != null) {
            referencias.addAll(autorizacion.getServicioIds());
        }
        if (!vehiculoClientPort.existePorId(orden.getVehiculoId())) {
            throw new IllegalArgumentException("El vehículo indicado no existe.");
        }
        for (Long servicioId : referencias) {
            if (!servicioClientPort.existePorId(servicioId)) {
                throw new IllegalArgumentException("El servicio " + servicioId + " no existe.");
            }
        }
        orden.setPropuestaTecnica(candidata.getPropuestaTecnica());
        orden.setAutorizacion(candidata.getAutorizacion());
    }

    private void validarPertenencia(Long recibida, Long existente, String campo) {
        if (recibida != null && !recibida.equals(existente)) {
            throw new IllegalArgumentException("El " + campo + " no pertenece a esta orden.");
        }
    }

    @Override
    public Optional<Boolean> estaAutorizada(Long id) {
        return ordenRepositoryPort.buscarPorId(id).map(OrdenDeServicio::estaAutorizada);
    }

    @Override
    public Optional<OrdenDeServicio> autorizar(Long id, Autorizacion autorizacion) {
        return ordenRepositoryPort.buscarPorId(id).map(orden -> {
            orden.autorizar(autorizacion);
            return ordenRepositoryPort.guardarOrden(orden);
        });
    }

    @Override
    public Optional<OrdenDeServicio> sincronizarEjecucion(Long id, Long ejecucionId) {
        return ordenRepositoryPort.buscarPorId(id).map(orden -> {
            String estado = ejecucionClientPort.estadoDeEjecucion(id, ejecucionId);
            orden.sincronizarEjecucion(estado);
            return ordenRepositoryPort.guardarOrden(orden);
        });
    }
}

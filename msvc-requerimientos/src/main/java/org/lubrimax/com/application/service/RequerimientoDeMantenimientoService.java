package org.lubrimax.com.application.service;

import lombok.RequiredArgsConstructor;
import org.lubrimax.com.application.port.out.*;
import org.lubrimax.com.domain.model.*;
import org.lubrimax.com.application.port.in.RequerimientoDeMantenimientoUseCase;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class RequerimientoDeMantenimientoService implements RequerimientoDeMantenimientoUseCase {
    private final RequerimientoDeMantenimientoRepositoryPort requerimientoRepositoryPort;
    private final OrdenClientPort ordenClientPort;
    private final ProductoClientPort productoClientPort;

    @Override
    public List<RequerimientoDeMantenimiento> listar() {
        return requerimientoRepositoryPort.listar();
    }

    @Override
    public Optional<RequerimientoDeMantenimiento> buscarPorId(Long id) {
        return requerimientoRepositoryPort.buscarPorId(id);
    }


    @Override
    public RequerimientoDeMantenimiento guardarRequerimiento(RequerimientoDeMantenimiento requerimiento) {
        if (requerimiento.getRequerimientoId() != null) {
            throw new IllegalArgumentException("El ID del requerimiento se genera automáticamente.");
        }
        validar(requerimiento, Set.of());
        return requerimientoRepositoryPort.guardarRequerimiento(requerimiento);
    }


    @Override
    public Optional<RequerimientoDeMantenimiento> actualizar(Long id, RequerimientoDeMantenimiento requerimiento) {
        return requerimientoRepositoryPort.buscarPorId(id).map(existente -> {
            Set<Long> ids = existente.getDetalles().stream()
                    .map(DetalleRequerimiento::getDetalleId).collect(Collectors.toSet());
            validar(requerimiento, ids);
            existente.setOrdenId(requerimiento.getOrdenId());
            existente.setEstado(requerimiento.getEstado());
            List<DetalleRequerimiento> recibidos = new ArrayList<>(requerimiento.getDetalles());
            ids.forEach(existente::eliminarDetalle);
            recibidos.forEach(existente::agregarDetalle);
            return requerimientoRepositoryPort.guardarRequerimiento(existente);
        });
    }


    @Override
    public boolean borrarRequerimiento(Long id) {
        if (requerimientoRepositoryPort.buscarPorId(id).isEmpty()) {
            return false;
        }
        requerimientoRepositoryPort.borrarRequerimiento(id);
        return true;
    }

    private void validar(RequerimientoDeMantenimiento requerimiento, Set<Long> idsPermitidos) {
        requerimiento.validarDatos(idsPermitidos);
        if (!ordenClientPort.existePorId(requerimiento.getOrdenId())) {
            throw new IllegalArgumentException("La orden indicada no existe.");
        }
        for (DetalleRequerimiento detalle : requerimiento.getDetalles()) {
            if (!productoClientPort.existePresentacion(detalle.getProductoId(), detalle.getPresentacionId())) {
                throw new IllegalArgumentException("El producto no existe o la presentación no pertenece a él.");
            }
        }
    }

}

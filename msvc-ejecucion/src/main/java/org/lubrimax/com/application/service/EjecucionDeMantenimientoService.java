package org.lubrimax.com.application.service;

import lombok.RequiredArgsConstructor;
import org.lubrimax.com.application.port.out.*;
import org.lubrimax.com.application.port.in.EjecucionDeMantenimientoUseCase;
import org.lubrimax.com.domain.model.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class EjecucionDeMantenimientoService implements EjecucionDeMantenimientoUseCase {

    private final EjecucionDeMantenimientoRepositoryPort repositoryPort;
    private final OrdenClientPort ordenClientPort;
    private final ProductoClientPort productoClientPort;

    @Override
    public List<EjecucionDeMantenimiento> listar() {
        return repositoryPort.listar();
    }

    @Override
    public Optional<EjecucionDeMantenimiento> buscarPorId(Long id) {
        return repositoryPort.buscarPorId(id);
    }


    @Override
    public EjecucionDeMantenimiento guardarEjecucion(EjecucionDeMantenimiento ejecucion) {
        if (ejecucion.getEjecucionId() != null) {
            throw new IllegalArgumentException("El ID de la ejecución se genera automáticamente.");
        }
        if (ejecucion.getEstado() == null) ejecucion.validarDatos();
        if (!"PENDIENTE".equals(ejecucion.getEstado())) throw new IllegalStateException("Una ejecución nueva debe estar PENDIENTE.");
        if (ejecucion.getFechaInicio() != null || ejecucion.getFechaFin() != null) throw new IllegalStateException("Registre las fechas mediante Iniciar y Finalizar mantenimiento.");
        validarOrdenUnica(ejecucion.getOrdenId(), null);
        validar(ejecucion, Map.of(), Set.of(), null);
        return repositoryPort.guardarEjecucion(ejecucion);
    }


    @Override
    public Optional<EjecucionDeMantenimiento> actualizar(Long id, EjecucionDeMantenimiento ejecucion) {
        return repositoryPort.buscarPorId(id).map(existente -> {
            validarAbierta(existente);
            if (!Objects.equals(existente.getOrdenId(), ejecucion.getOrdenId())) throw new IllegalStateException("No se puede cambiar la orden de una ejecución.");
            if (!Objects.equals(existente.getEstado(), ejecucion.getEstado())) throw new IllegalStateException("Utilice las acciones de ejecución para cambiar su estado.");
            if (!Objects.equals(existente.getFechaInicio(), ejecucion.getFechaInicio()) || !Objects.equals(existente.getFechaFin(), ejecucion.getFechaFin()))
                throw new IllegalStateException("Registre las fechas mediante Iniciar y Finalizar mantenimiento.");
            validarOrdenUnica(ejecucion.getOrdenId(), id);
            validar(ejecucion, existente.getTareas().stream().collect(Collectors.toMap(
                            TareaEjecutada::getTareaId, TareaEjecutada::getEstado)),
                    existente.getConsumos().stream().map(ConsumoReal::getConsumoId).collect(Collectors.toSet()),
                    existente.getEstado());
            existente.setOrdenId(ejecucion.getOrdenId());
            existente.setTecnicoId(ejecucion.getTecnicoId());
            existente.setEstado(ejecucion.getEstado());
            existente.setFechaInicio(ejecucion.getFechaInicio());
            existente.setFechaFin(ejecucion.getFechaFin());
            existente.setLiquidacion(ejecucion.getLiquidacion());
            existente.setProximoServicio(ejecucion.getProximoServicio());
            existente.setTareas(ejecucion.getTareas());
            existente.setConsumos(ejecucion.getConsumos());
            return repositoryPort.guardarEjecucion(existente);
        });
    }

    @Override
    public Optional<EjecucionDeMantenimiento> iniciarMantenimiento(Long id, LocalDateTime fechaInicio) {
        return repositoryPort.buscarPorId(id).map(existente -> {
            existente.iniciar(fechaInicio);
            var guardada = repositoryPort.guardarEjecucion(existente);
            ordenClientPort.sincronizarEjecucion(guardada.getOrdenId(), guardada.getEjecucionId());
            return guardada;
        });
    }


    @Override
    public Optional<EjecucionDeMantenimiento> finalizarMantenimiento(Long id, LocalDateTime fechaFin) {
        return repositoryPort.buscarPorId(id).map(existente -> {
            existente.finalizar(fechaFin);
            var guardada = repositoryPort.guardarEjecucion(existente);
            ordenClientPort.sincronizarEjecucion(guardada.getOrdenId(), guardada.getEjecucionId());
            return guardada;
        });
    }

    @Override
    public Optional<EjecucionDeMantenimiento> cancelarMantenimiento(Long id) {
        return repositoryPort.buscarPorId(id).map(existente -> {
            existente.cancelar();
            var guardada = repositoryPort.guardarEjecucion(existente);
            ordenClientPort.sincronizarEjecucion(guardada.getOrdenId(), guardada.getEjecucionId());
            return guardada;
        });
    }

    @Override
    public Optional<EjecucionDeMantenimiento> iniciarTarea(Long ejecucionId, Long tareaId, LocalDateTime fechaInicio) {
        return repositoryPort.buscarPorId(ejecucionId).map(ejecucion -> {
            validarAbierta(ejecucion);
            ejecucion.validarTrabajoIniciado();
            TareaEjecutada tarea = buscarTarea(ejecucion, tareaId);
            tarea.iniciar(fechaInicio);
            return repositoryPort.guardarEjecucion(ejecucion);
        });
    }

    @Override
    public Optional<EjecucionDeMantenimiento> finalizarTarea(Long ejecucionId, Long tareaId, LocalDateTime fechaFin) {
        return repositoryPort.buscarPorId(ejecucionId).map(ejecucion -> {
            validarAbierta(ejecucion);
            ejecucion.validarTrabajoIniciado();
            TareaEjecutada tarea = buscarTarea(ejecucion, tareaId);
            tarea.finalizar(fechaFin);
            return repositoryPort.guardarEjecucion(ejecucion);
        });
    }

    @Override
    public Optional<EjecucionDeMantenimiento> marcarTareaNoRealizada(Long ejecucionId, Long tareaId) {
        return repositoryPort.buscarPorId(ejecucionId).map(ejecucion -> {
            validarAbierta(ejecucion);
            ejecucion.validarTrabajoIniciado();
            TareaEjecutada tarea = buscarTarea(ejecucion, tareaId);
            tarea.marcarNoRealizada();
            return repositoryPort.guardarEjecucion(ejecucion);
        });
    }

    @Override
    public Optional<EjecucionDeMantenimiento> registrarConsumo(Long ejecucionId, ConsumoReal consumo) {
        return repositoryPort.buscarPorId(ejecucionId).map(ejecucion -> {
            if (consumo == null) {
                throw new IllegalArgumentException("El consumo es obligatorio.");
            }
            if (!productoClientPort.existePresentacion(
                    consumo.getProductoId(),
                    consumo.getPresentacionId())) {
                throw new IllegalArgumentException("El producto no existe o la presentación no pertenece a él.");
            }
            ejecucion.registrarConsumo(consumo);
            return repositoryPort.guardarEjecucion(ejecucion);
        });
    }


    @Override
    public boolean borrarEjecucion(Long id) {
        var existente = repositoryPort.buscarPorId(id);
        if (existente.isEmpty()) {
            return false;
        }
        if (!"PENDIENTE".equals(existente.get().getEstado())) throw new IllegalStateException("Solo se puede eliminar una ejecución PENDIENTE.");
        repositoryPort.borrarEjecucion(id);
        return true;
    }


    private void validar(EjecucionDeMantenimiento ejecucion, Map<Long, String> tareasAnteriores,
                         Set<Long> consumosPermitidos, String estadoAnterior) {
        if (estadoAnterior != null) {
            ejecucion.validarTransicionDesde(estadoAnterior);
        }
        ejecucion.validarDatos();
        Set<Long> tareasRecibidas = new HashSet<>();
        for (TareaEjecutada tarea : ejecucion.getTareas()) {
            identidad(tarea.getTareaId(), tareasAnteriores.keySet(), tareasRecibidas, "tareaId");
            if (tarea.getTareaId() != null) {
                tarea.validarTransicionDesde(tareasAnteriores.get(tarea.getTareaId()));
            }
        }
        Set<Long> consumosRecibidos = new HashSet<>();
        Set<ReferenciaProducto> productos = new LinkedHashSet<>();
        for (ConsumoReal consumo : ejecucion.getConsumos()) {
            identidad(consumo.getConsumoId(), consumosPermitidos, consumosRecibidos, "consumoId");
            productos.add(new ReferenciaProducto(consumo.getProductoId(), consumo.getPresentacionId()));
        }
        if (!ordenClientPort.estaAutorizada(ejecucion.getOrdenId())) {
            throw new IllegalArgumentException("La orden no existe o no está autorizada.");
        }
        for (ReferenciaProducto referencia : productos) {
            if (!productoClientPort.existePresentacion(referencia.productoId(), referencia.presentacionId())) {
                throw new IllegalArgumentException("El producto no existe o la presentación no pertenece a él.");
            }
        }
    }

    private void validarOrdenUnica(Long ordenId, Long ejecucionId) {
        if (ordenId == null || ordenId <= 0) {
            throw new IllegalArgumentException("ordenId debe ser positivo.");
        }
        if (repositoryPort.existePorOrdenId(ordenId, ejecucionId)) {
            throw new IllegalStateException("Ya existe una ejecución para la orden " + ordenId + ".");
        }
    }

    private record ReferenciaProducto(Long productoId, Long presentacionId) {
    }

    private void identidad(Long id, Set<Long> permitidos, Set<Long> recibidos, String campo) {
        if (id != null && (!permitidos.contains(id) || !recibidos.add(id))) {
            throw new IllegalArgumentException(campo + " es ajeno, inexistente o está repetido.");
        }
    }

    private TareaEjecutada buscarTarea(EjecucionDeMantenimiento ejecucion, Long tareaId) {
        return ejecucion.getTareas().stream()
                .filter(t -> Objects.equals(t.getTareaId(), tareaId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("La tarea no pertenece a la ejecución."));
    }

    private void validarAbierta(EjecucionDeMantenimiento ejecucion) {
        if (Set.of("FINALIZADA", "CANCELADA").contains(ejecucion.getEstado()))
            throw new IllegalStateException("Una ejecución cerrada es de solo consulta.");
    }

    @Override
    public Optional<EjecucionDeMantenimiento> sincronizarOrden(Long id) {
        return repositoryPort.buscarPorId(id).map(ejecucion -> {
            ordenClientPort.sincronizarEjecucion(ejecucion.getOrdenId(), ejecucion.getEjecucionId());
            return ejecucion;
        });
    }
}

package org.lubrimax.com.application.port.in;

import org.lubrimax.com.domain.model.ConsumoReal;
import org.lubrimax.com.domain.model.EjecucionDeMantenimiento;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EjecucionDeMantenimientoUseCase {
    List<EjecucionDeMantenimiento> listar();
    Optional<EjecucionDeMantenimiento> buscarPorId(Long id);
    EjecucionDeMantenimiento guardarEjecucion(EjecucionDeMantenimiento ejecucion);
    Optional<EjecucionDeMantenimiento> actualizar(Long id, EjecucionDeMantenimiento ejecucion);
    Optional<EjecucionDeMantenimiento> iniciarMantenimiento(Long id, LocalDateTime fechaInicio);
    Optional<EjecucionDeMantenimiento> finalizarMantenimiento(Long id, LocalDateTime fechaFin);
    Optional<EjecucionDeMantenimiento> cancelarMantenimiento(Long id);
    Optional<EjecucionDeMantenimiento> iniciarTarea(Long ejecucionId, Long tareaId, LocalDateTime fechaInicio);
    Optional<EjecucionDeMantenimiento> finalizarTarea(Long ejecucionId, Long tareaId, LocalDateTime fechaFin);
    Optional<EjecucionDeMantenimiento> marcarTareaNoRealizada(Long ejecucionId, Long tareaId);
    Optional<EjecucionDeMantenimiento> registrarConsumo(Long ejecucionId, ConsumoReal consumo);
    boolean borrarEjecucion(Long id);
    Optional<EjecucionDeMantenimiento> sincronizarOrden(Long id);
}

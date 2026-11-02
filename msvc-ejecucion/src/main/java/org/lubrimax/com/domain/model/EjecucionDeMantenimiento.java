package org.lubrimax.com.domain.model;

import lombok.*;
import java.util.List;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class EjecucionDeMantenimiento {
    private Long ejecucionId;
    private Long ordenId;
    private Long tecnicoId;
    private String estado;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private LiquidacionDeServicio liquidacion;
    private ProgramacionDeProximoServicio proximoServicio;
    private List<TareaEjecutada> tareas;
    private List<ConsumoReal> consumos;

    public void validarTrabajoIniciado() {
        if (!EstadoEjecucion.EN_PROCESO.name().equals(estado))
            throw new IllegalStateException("Debe iniciar el mantenimiento antes de gestionar sus tareas.");
    }

    public void iniciar(LocalDateTime fechaInicio) {
        cambiarEstado(EstadoEjecucion.EN_PROCESO, fechaInicio, fechaFin);
    }

    public void finalizar(LocalDateTime fechaFin) {
        cambiarEstado(EstadoEjecucion.FINALIZADA, fechaInicio, fechaFin);
    }

    public void cancelar() {
        cambiarEstado(EstadoEjecucion.CANCELADA, fechaInicio, fechaFin);
    }

    public void registrarConsumo(ConsumoReal consumo) {
        if (EstadoEjecucion.FINALIZADA.name().equals(estado) || EstadoEjecucion.CANCELADA.name().equals(estado)) {
            throw new IllegalStateException("No se pueden registrar consumos en una ejecución finalizada o cancelada.");
        }
        if (consumo == null) {
            throw new IllegalArgumentException("No se admiten consumos nulos.");
        }
        consumo.calcularImporte();
        List<ConsumoReal> actuales = consumos == null ? List.of() : consumos;
        if (consumo.getConsumoId() != null && actuales.stream()
                .anyMatch(c -> consumo.getConsumoId().equals(c.getConsumoId()))) {
            throw new IllegalArgumentException("consumoId está repetido.");
        }
        List<ConsumoReal> nuevos = new ArrayList<>(actuales);
        nuevos.add(consumo);
        consumos = nuevos;
    }

    public LiquidacionDeServicio calcularLiquidacion(BigDecimal subtotalManoObra) {
        ReglasEjecucion.decimal(subtotalManoObra, "subtotalManoObra", 2, "99999999999999999.99", true);
        if (consumos == null) {
            throw new IllegalArgumentException("Las listas tareas y consumos son obligatorias; pueden estar vacías.");
        }
        BigDecimal subtotal = BigDecimal.ZERO;
        for (ConsumoReal consumo : consumos) {
            if (consumo == null) {
                throw new IllegalArgumentException("No se admiten consumos nulos.");
            }
            subtotal = subtotal.add(consumo.calcularImporte());
        }
        subtotal = subtotal.setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subtotal.add(subtotalManoObra).setScale(2, RoundingMode.HALF_UP);
        ReglasEjecucion.decimal(total, "total calculado", 2, "99999999999999999.99", true);
        liquidacion = new LiquidacionDeServicio(subtotal, subtotalManoObra.setScale(2), total);
        return liquidacion;
    }

    public void validarTransicionDesde(String anterior) {
        EstadoEjecucion origen = ReglasEjecucion.estado(EstadoEjecucion.class, anterior);
        EstadoEjecucion destino = ReglasEjecucion.estado(EstadoEjecucion.class, estado);
        if (!origen.permiteTransicionA(destino)) {
            throw new IllegalStateException("No se permite cambiar la ejecución de " + origen + " a " + destino + ".");
        }
    }

    public void validarDatos() {
        ReglasEjecucion.positivo(ordenId, "ordenId");
        ReglasEjecucion.positivo(tecnicoId, "tecnicoId");
        validarCierre(estado, fechaInicio, fechaFin);
        if (proximoServicio != null && (proximoServicio.fechaSugerida() == null
                || proximoServicio.kilometrajeSugerido() == null || proximoServicio.kilometrajeSugerido() < 0)) {
            throw new IllegalArgumentException("El próximo servicio requiere fechaSugerida y kilometrajeSugerido no negativo.");
        }
        if (liquidacion == null) {
            throw new IllegalArgumentException("La liquidacion con subtotalManoObra es obligatoria.");
        }
        calcularLiquidacion(liquidacion.subtotalManoObra());
    }

    private void cambiarEstado(EstadoEjecucion destino, LocalDateTime inicio, LocalDateTime fin) {
        EjecucionDeMantenimiento candidata = new EjecucionDeMantenimiento();
        candidata.setEstado(destino.name());
        candidata.validarTransicionDesde(estado);
        validarCierre(destino.name(), inicio, fin);
        estado = destino.name();
        fechaInicio = inicio;
        fechaFin = fin;
    }

    private void validarCierre(String estado, LocalDateTime inicio, LocalDateTime fin) {
        EstadoEjecucion actual = ReglasEjecucion.estado(EstadoEjecucion.class, estado);
        ReglasEjecucion.fechas(inicio, fin, "ejecución");
        if (actual == EstadoEjecucion.EN_PROCESO && inicio == null) {
            throw new IllegalArgumentException("Una ejecución EN_PROCESO requiere fechaInicio.");
        }
        if (actual == EstadoEjecucion.FINALIZADA && fin == null) {
            throw new IllegalArgumentException("Una ejecución FINALIZADA requiere fechaFin.");
        }
        if (tareas == null || consumos == null) {
            throw new IllegalArgumentException("Las listas tareas y consumos son obligatorias; pueden estar vacías.");
        }
        for (TareaEjecutada tarea : tareas) {
            if (tarea == null) {
                throw new IllegalArgumentException("No se admiten tareas nulas.");
            }
            tarea.validarDatos();
            if (actual == EstadoEjecucion.PENDIENTE && (!EstadoTarea.PENDIENTE.name().equals(tarea.getEstado()) || tarea.getInicio() != null || tarea.getFin() != null))
                throw new IllegalStateException("Debe iniciar el mantenimiento antes de gestionar sus tareas.");
            EstadoTarea estadoTarea = ReglasEjecucion.estado(EstadoTarea.class, tarea.getEstado());
            if (actual == EstadoEjecucion.FINALIZADA
                    && (estadoTarea == EstadoTarea.PENDIENTE || estadoTarea == EstadoTarea.EN_PROCESO)) {
                throw new IllegalArgumentException("Una ejecución FINALIZADA no puede contener tareas abiertas.");
            }
            dentroDelIntervalo(tarea.getInicio(), inicio, fin);
            dentroDelIntervalo(tarea.getFin(), inicio, fin);
        }
    }

    private void dentroDelIntervalo(LocalDateTime fecha, LocalDateTime inicio, LocalDateTime fin) {
        if (fecha != null && (inicio == null || fecha.isBefore(inicio) || (fin != null && fecha.isAfter(fin)))) {
            throw new IllegalArgumentException("Las fechas de la tarea deben estar dentro de la ejecución.");
        }
    }
}

package org.lubrimax.com.domain.model;

import lombok.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TareaEjecutada {
    private Long tareaId;
    private String tipoTarea;
    private String estado;
    private LocalDateTime inicio;
    private LocalDateTime fin;

    public void iniciar(LocalDateTime inicio) {
        cambiarEstado(EstadoTarea.EN_PROCESO, inicio, null);
    }

    public void finalizar(LocalDateTime fin) {
        cambiarEstado(EstadoTarea.FINALIZADA, inicio, fin);
    }

    public void marcarNoRealizada() {
        cambiarEstado(EstadoTarea.NO_REALIZADA, inicio, fin);
    }

    public void validarTransicionDesde(String anterior) {
        EstadoTarea origen = ReglasEjecucion.estado(EstadoTarea.class, anterior);
        EstadoTarea destino = ReglasEjecucion.estado(EstadoTarea.class, estado);
        if (!origen.permiteTransicionA(destino)) {
            throw new IllegalStateException("No se permite cambiar la tarea de " + origen + " a " + destino + ".");
        }
    }

    public void validarDatos() {
        ReglasEjecucion.texto(tipoTarea, "tipoTarea", 100);
        validarFechas(estado, inicio, fin);
    }

    private void cambiarEstado(EstadoTarea destino, LocalDateTime inicio, LocalDateTime fin) {
        TareaEjecutada candidata = new TareaEjecutada(tareaId, tipoTarea, destino.name(), inicio, fin);
        candidata.validarTransicionDesde(estado);
        candidata.validarDatos();
        this.estado = destino.name();
        this.inicio = inicio;
        this.fin = fin;
    }

    private void validarFechas(String estado, LocalDateTime inicio, LocalDateTime fin) {
        EstadoTarea actual = ReglasEjecucion.estado(EstadoTarea.class, estado);
        ReglasEjecucion.fechas(inicio, fin, "tarea");
        if (actual == EstadoTarea.FINALIZADA && fin == null) {
            throw new IllegalArgumentException("Una tarea FINALIZADA requiere fin.");
        }
        if (actual == EstadoTarea.EN_PROCESO && (inicio == null || fin != null)) {
            throw new IllegalArgumentException("Una tarea EN_PROCESO requiere inicio y fin nulo.");
        }
    }
}

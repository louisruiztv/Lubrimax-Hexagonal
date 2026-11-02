package org.lubrimax.com.infrastructure.adapter.in.rest;

import lombok.RequiredArgsConstructor;
import org.lubrimax.com.application.port.in.EjecucionDeMantenimientoUseCase;
import org.lubrimax.com.infrastructure.adapter.in.rest.dto.ConsumoRealDto;
import org.lubrimax.com.infrastructure.adapter.in.rest.dto.EjecucionDeMantenimientoDto;
import org.lubrimax.com.infrastructure.adapter.in.rest.mapper.EjecucionDeMantenimientoRestMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ejecuciones")
@RequiredArgsConstructor
public class EjecucionDeMantenimientoController {
    private final EjecucionDeMantenimientoUseCase ejecucionService;
    private final EjecucionDeMantenimientoRestMapper mapper;

    @GetMapping
    public ResponseEntity<List<EjecucionDeMantenimientoDto>> listar() {
        return ResponseEntity.ok(ejecucionService.listar().stream().map(mapper::toDto).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EjecucionDeMantenimientoDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.of(ejecucionService.buscarPorId(id).map(mapper::toDto));
    }

    @PostMapping
    public ResponseEntity<EjecucionDeMantenimientoDto> guardarEjecucion(@RequestBody EjecucionDeMantenimientoDto ejecucion) {
        EjecucionDeMantenimientoDto guardado = mapper.toDto(ejecucionService.guardarEjecucion(mapper.toModel(ejecucion)));
        return ResponseEntity.created(URI.create("/api/ejecuciones/" + guardado.getEjecucionId())).body(guardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EjecucionDeMantenimientoDto> actualizar(@PathVariable Long id, @RequestBody EjecucionDeMantenimientoDto ejecucion) {
        return ResponseEntity.of(ejecucionService.actualizar(id, mapper.toModel(ejecucion)).map(mapper::toDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarEjecucion(@PathVariable Long id) {
        return ejecucionService.borrarEjecucion(id) ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/iniciar")
    public ResponseEntity<EjecucionDeMantenimientoDto> iniciar(@PathVariable Long id, @RequestBody Map<String, LocalDateTime> request) {
        return ResponseEntity.of(ejecucionService.iniciarMantenimiento(id, request.get("fechaInicio")).map(mapper::toDto));
    }

    @PutMapping("/{id}/finalizar")
    public ResponseEntity<EjecucionDeMantenimientoDto> finalizar(@PathVariable Long id, @RequestBody Map<String, java.time.LocalDateTime> request) {
        return ResponseEntity.of(ejecucionService.finalizarMantenimiento(id, request.get("fechaFin")).map(mapper::toDto));
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<EjecucionDeMantenimientoDto> cancelar(@PathVariable Long id) {
        return ResponseEntity.of(ejecucionService.cancelarMantenimiento(id).map(mapper::toDto));
    }

    @PutMapping("/{ejecucionId}/tareas/{tareaId}/iniciar")
    public ResponseEntity<EjecucionDeMantenimientoDto> iniciarTarea(@PathVariable Long ejecucionId, @PathVariable Long tareaId, @RequestBody Map<String, LocalDateTime> request) {
        return ResponseEntity.of(ejecucionService.iniciarTarea(ejecucionId, tareaId, request.get("fechaInicio")).map(mapper::toDto));
    }

    @PutMapping("/{ejecucionId}/tareas/{tareaId}/finalizar")
    public ResponseEntity<EjecucionDeMantenimientoDto> finalizarTarea(@PathVariable Long ejecucionId, @PathVariable Long tareaId, @RequestBody Map<String, LocalDateTime> request) {
        return ResponseEntity.of(ejecucionService.finalizarTarea(ejecucionId, tareaId, request.get("fechaFin")).map(mapper::toDto));
    }

    @PutMapping("/{ejecucionId}/tareas/{tareaId}/no-realizada")
    public ResponseEntity<EjecucionDeMantenimientoDto> marcarTareaNoRealizada(@PathVariable Long ejecucionId, @PathVariable Long tareaId) {
        return ResponseEntity.of(ejecucionService.marcarTareaNoRealizada(ejecucionId, tareaId).map(mapper::toDto));
    }

    @PostMapping("/{id}/consumos")
    public ResponseEntity<EjecucionDeMantenimientoDto> registrarConsumo(@PathVariable Long id, @RequestBody ConsumoRealDto consumo) {
        return ejecucionService.registrarConsumo(id, mapper.toModel(consumo)).map(mapper::toDto).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}/sincronizar-orden")
    public ResponseEntity<EjecucionDeMantenimientoDto> sincronizarOrden(@PathVariable Long id) {
        return ResponseEntity.of(ejecucionService.sincronizarOrden(id).map(mapper::toDto));
    }
}

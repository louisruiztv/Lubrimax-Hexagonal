package org.lubrimax.com.infrastructure.adapter.in.rest;

import lombok.RequiredArgsConstructor;
import org.lubrimax.com.application.port.in.OrdenDeServicioUseCase;
import org.lubrimax.com.infrastructure.adapter.in.rest.dto.*;
import org.lubrimax.com.infrastructure.adapter.in.rest.mapper.OrdenDeServicioRestMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/ordenes")
@RequiredArgsConstructor
public class OrdenDeServicioController {
    private final OrdenDeServicioUseCase ordenService;
    private final OrdenDeServicioRestMapper mapper;

    @GetMapping
    public ResponseEntity<List<OrdenDeServicioDto>> listar() {
        return ResponseEntity.ok(ordenService.listar().stream().map(mapper::toDto).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdenDeServicioDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.of(ordenService.buscarPorId(id).map(mapper::toDto));
    }

    @PostMapping
    public ResponseEntity<OrdenDeServicioDto> guardarOrden(@RequestBody OrdenDeServicioDto orden) {
        OrdenDeServicioDto guardada = mapper.toDto(ordenService.guardarOrden(mapper.toDomain(orden)));
        return ResponseEntity.created(URI.create("/api/ordenes/" + guardada.getOrdenId())).body(guardada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrdenDeServicioDto> actualizar(@PathVariable Long id, @RequestBody OrdenDeServicioDto orden) {
        return ResponseEntity.of(ordenService.actualizar(id, mapper.toDomain(orden)).map(mapper::toDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarOrden(@PathVariable Long id) {
        return ordenService.borrarOrden(id) ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/autorizada")
    public ResponseEntity<Boolean> estaAutorizada(@PathVariable Long id) {
        return ResponseEntity.of(ordenService.estaAutorizada(id));
    }

    @PutMapping("/{id}/autorizar")
    public ResponseEntity<OrdenDeServicioDto> autorizar(@PathVariable Long id, @RequestBody AutorizacionDto autorizacion) {
        return ResponseEntity.of(ordenService.autorizar(id, mapper.toDomain(autorizacion)).map(mapper::toDto));
    }
    @PutMapping("/{id}/sincronizar-ejecucion/{ejecucionId}")
    public ResponseEntity<OrdenDeServicioDto> sincronizarEjecucion(@PathVariable Long id, @PathVariable Long ejecucionId) {
        return ResponseEntity.of(ordenService.sincronizarEjecucion(id, ejecucionId).map(mapper::toDto));
    }
}

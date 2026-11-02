package org.lubrimax.com.infrastructure.adapter.in.rest;

import lombok.RequiredArgsConstructor;
import org.lubrimax.com.application.port.in.ServicioDeMantenimientoUseCase;
import org.lubrimax.com.infrastructure.adapter.in.rest.dto.ServicioDeMantenimientoDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/servicios")
@RequiredArgsConstructor
public class ServicioDeMantenimientoController {
    private final ServicioDeMantenimientoUseCase servicioService;
    private final ServicioDeMantenimientoRestMapper mapper;

    @GetMapping
    public ResponseEntity<List<ServicioDeMantenimientoDto>> listar() {
        return ResponseEntity.ok(servicioService.listar().stream().map(mapper::toDto).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicioDeMantenimientoDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.of(servicioService.buscarPorId(id).map(mapper::toDto));
    }

    @PostMapping
    public ResponseEntity<ServicioDeMantenimientoDto> guardarServicio(@RequestBody ServicioDeMantenimientoDto servicio) {
        ServicioDeMantenimientoDto guardado = mapper.toDto(servicioService.guardarServicio(mapper.toDomain(servicio)));
        return ResponseEntity.created(URI.create("/api/servicios/" + guardado.getServicioId())).body(guardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicioDeMantenimientoDto> actualizar(@PathVariable Long id,
            @RequestBody ServicioDeMantenimientoDto servicio) {
        return ResponseEntity.of(servicioService.actualizar(id, mapper.toDomain(servicio)).map(mapper::toDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarServicio(@PathVariable Long id) {
        return servicioService.borrarServicio(id) ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}

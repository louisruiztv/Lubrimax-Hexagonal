package org.lubrimax.com.infrastructure.adapter.in.rest;

import lombok.RequiredArgsConstructor;
import org.lubrimax.com.application.port.in.RequerimientoDeMantenimientoUseCase;
import org.lubrimax.com.infrastructure.adapter.in.rest.dto.RequerimientoDeMantenimientoDto;
import org.lubrimax.com.infrastructure.adapter.in.rest.mapper.RequerimientoDeMantenimientoRestMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/requerimientos")
@RequiredArgsConstructor
public class RequerimientoDeMantenimientoController {
    private final RequerimientoDeMantenimientoUseCase requerimientoService;
    private final RequerimientoDeMantenimientoRestMapper mapper;

    @GetMapping
    public ResponseEntity<List<RequerimientoDeMantenimientoDto>> listar() {
        return ResponseEntity.ok(requerimientoService.listar().stream().map(mapper::toDto).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RequerimientoDeMantenimientoDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.of(requerimientoService.buscarPorId(id).map(mapper::toDto));
    }

    @PostMapping
    public ResponseEntity<RequerimientoDeMantenimientoDto> guardarRequerimiento(@RequestBody RequerimientoDeMantenimientoDto requerimiento) {
        RequerimientoDeMantenimientoDto guardado = mapper.toDto(requerimientoService.guardarRequerimiento(mapper.toDomain(requerimiento)));
        return ResponseEntity.created(URI.create("/api/requerimientos/" + guardado.getRequerimientoId())).body(guardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RequerimientoDeMantenimientoDto> actualizar(@PathVariable Long id, @RequestBody RequerimientoDeMantenimientoDto requerimiento) {
        return ResponseEntity.of(requerimientoService.actualizar(id, mapper.toDomain(requerimiento)).map(mapper::toDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarRequerimiento(@PathVariable Long id) {
        return requerimientoService.borrarRequerimiento(id) ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}

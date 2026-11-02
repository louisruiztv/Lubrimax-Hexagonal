package org.lubrimax.com.infrastructure.adapter.in.rest;

import lombok.RequiredArgsConstructor;
import org.lubrimax.com.application.port.in.VehiculoUseCase;
import org.lubrimax.com.infrastructure.adapter.in.rest.dto.VehiculoDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/vehiculos")
@RequiredArgsConstructor
public class VehiculoController {
    private final VehiculoUseCase vehiculoService;
    private final VehiculoRestMapper mapper;

    @GetMapping
    public ResponseEntity<List<VehiculoDto>> listar() {
        return ResponseEntity.ok(vehiculoService.listar().stream().map(mapper::toDto).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehiculoDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.of(vehiculoService.buscarPorId(id).map(mapper::toDto));
    }

    @PostMapping
    public ResponseEntity<VehiculoDto> guardarVehiculo(@RequestBody VehiculoDto vehiculo) {
        VehiculoDto guardado = mapper.toDto(vehiculoService.guardarVehiculo(mapper.toDomain(vehiculo)));
        return ResponseEntity.created(URI.create("/api/vehiculos/" + guardado.getVehiculoId())).body(guardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehiculoDto> actualizar(@PathVariable Long id, @RequestBody VehiculoDto vehiculo) {
        return ResponseEntity.of(vehiculoService.actualizar(id, mapper.toDomain(vehiculo)).map(mapper::toDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarVehiculo(@PathVariable Long id) {
        return vehiculoService.borrarVehiculo(id) ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}

package org.lubrimax.com.infrastructure.adapter.in.rest;

import lombok.RequiredArgsConstructor;
import org.lubrimax.com.application.port.in.ProductoUseCase;
import org.lubrimax.com.infrastructure.adapter.in.rest.dto.ProductoDto;
import org.lubrimax.com.infrastructure.adapter.in.rest.mapper.ProductoRestMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {
    private final ProductoUseCase productoService;
    private final ProductoRestMapper mapper;

    @GetMapping
    public ResponseEntity<List<ProductoDto>> listar() {
        return ResponseEntity.ok(productoService.listar().stream().map(mapper::toDto).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.of(productoService.buscarPorId(id).map(mapper::toDto));
    }

    @PostMapping
    public ResponseEntity<ProductoDto> guardarProducto(@RequestBody ProductoDto producto) {
        ProductoDto guardado = mapper.toDto(productoService.guardarProducto(mapper.toDomain(producto)));
        return ResponseEntity.created(URI.create("/api/productos/" + guardado.getProductoId())).body(guardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoDto> actualizar(@PathVariable Long id, @RequestBody ProductoDto producto) {
        return ResponseEntity.of(productoService.actualizar(id, mapper.toDomain(producto)).map(mapper::toDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarProducto(@PathVariable Long id) {
        return productoService.borrarProducto(id) ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}

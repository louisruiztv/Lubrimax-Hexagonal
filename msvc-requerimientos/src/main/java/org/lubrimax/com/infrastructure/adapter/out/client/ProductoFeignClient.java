package org.lubrimax.com.infrastructure.adapter.out.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@FeignClient(name = "msvc-productos", url = "${clients.productos.url}")
public interface ProductoFeignClient {
    @GetMapping("/api/productos/{id}")
    ProductoRespuesta buscarPorId(@PathVariable("id") Long id);

    @JsonIgnoreProperties(ignoreUnknown = true)
    record ProductoRespuesta(Long productoId, List<PresentacionRespuesta> presentaciones) {}
    @JsonIgnoreProperties(ignoreUnknown = true)
    record PresentacionRespuesta(Long presentacionId) {}
}

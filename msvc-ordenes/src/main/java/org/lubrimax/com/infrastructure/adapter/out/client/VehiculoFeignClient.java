package org.lubrimax.com.infrastructure.adapter.out.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-vehiculos", url = "${clients.vehiculos.url}")
public interface    VehiculoFeignClient {
    @GetMapping("/api/vehiculos/{id}")
    void buscarPorId(@PathVariable("id") Long id);
}

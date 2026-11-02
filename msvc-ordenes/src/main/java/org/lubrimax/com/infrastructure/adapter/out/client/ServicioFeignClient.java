package org.lubrimax.com.infrastructure.adapter.out.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-servicios", url = "${clients.servicios.url}")
public interface ServicioFeignClient {
    @GetMapping("/api/servicios/{id}")
    void buscarPorId(@PathVariable("id") Long id);
}

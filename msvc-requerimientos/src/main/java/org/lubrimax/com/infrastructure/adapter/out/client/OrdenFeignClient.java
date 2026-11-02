package org.lubrimax.com.infrastructure.adapter.out.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "msvc-ordenes", url = "${clients.ordenes.url}")
public interface OrdenFeignClient {
    @GetMapping("/api/ordenes/{id}")
    void buscarPorId(@PathVariable("id") Long id);
}

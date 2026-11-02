package org.lubrimax.com.infrastructure.adapter.out.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "msvc-ejecucion", url = "${clients.ejecucion.url}")
public interface EjecucionFeignClient {
    @GetMapping("/api/ejecuciones/{id}")
    EjecucionRespuesta buscarPorId(@PathVariable("id") Long id);

    @JsonIgnoreProperties(ignoreUnknown = true)
    record EjecucionRespuesta(Long ejecucionId, Long ordenId, String estado) {}
}

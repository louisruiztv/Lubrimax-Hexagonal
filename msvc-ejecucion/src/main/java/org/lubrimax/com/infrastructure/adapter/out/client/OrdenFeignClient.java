package org.lubrimax.com.infrastructure.adapter.out.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@FeignClient(name = "msvc-ordenes", url = "${clients.ordenes.url}")
public interface OrdenFeignClient {
    @GetMapping("/api/ordenes/{id}")
    OrdenRespuesta buscarPorId(@PathVariable("id") Long id);

    @GetMapping("/api/ordenes/{id}/autorizada")
    Boolean estaAutorizada(@PathVariable("id") Long id);

    @PutMapping("/api/ordenes/{id}/sincronizar-ejecucion/{ejecucionId}")
    void sincronizarEjecucion(@PathVariable("id") Long id, @PathVariable("ejecucionId") Long ejecucionId);

    @JsonIgnoreProperties(ignoreUnknown = true)
    record OrdenRespuesta(Long ordenId, String estado, AutorizacionRespuesta autorizacion) {}
    @JsonIgnoreProperties(ignoreUnknown = true)
    record AutorizacionRespuesta(Long autorizacionId, List<Long> servicioIds) {}
}

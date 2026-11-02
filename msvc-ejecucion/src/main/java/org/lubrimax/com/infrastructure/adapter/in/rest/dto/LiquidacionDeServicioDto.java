package org.lubrimax.com.infrastructure.adapter.in.rest.dto;

import java.math.BigDecimal;

public record LiquidacionDeServicioDto(BigDecimal subtotalProductos, BigDecimal subtotalManoObra, BigDecimal total) {
}

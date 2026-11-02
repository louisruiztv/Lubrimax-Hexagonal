package org.lubrimax.com.domain.model;

import java.math.BigDecimal;

public record LiquidacionDeServicio(BigDecimal subtotalProductos, BigDecimal subtotalManoObra, BigDecimal total) {
}

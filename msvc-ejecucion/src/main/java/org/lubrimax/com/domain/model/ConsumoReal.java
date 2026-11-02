package org.lubrimax.com.domain.model;

import lombok.*;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ConsumoReal {
    private Long consumoId;
    private Long productoId;
    private Long presentacionId;
    private BigDecimal cantidadReal;
    private BigDecimal precioAplicado;

    public BigDecimal calcularImporte() {
        ReglasEjecucion.positivo(productoId, "productoId");
        ReglasEjecucion.positivo(presentacionId, "presentacionId");
        ReglasEjecucion.decimal(cantidadReal, "cantidadReal", 3, "999999999.999", false);
        ReglasEjecucion.decimal(precioAplicado, "precioAplicado", 2, "9999999999.99", true);
        return cantidadReal.multiply(precioAplicado);
    }
}

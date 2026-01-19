package com.sofka.ti.backend.dto.movimiento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MovimientoResponse(
            Long movimientoId,
        LocalDateTime fecha,
        String tipoMovimiento,
        BigDecimal valor,
        BigDecimal saldoDisponible,
        Long cuentaId
) {
    
}

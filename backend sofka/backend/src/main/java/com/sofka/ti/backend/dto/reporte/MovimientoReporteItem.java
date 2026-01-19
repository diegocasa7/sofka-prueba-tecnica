package com.sofka.ti.backend.dto.reporte;

import java.math.BigDecimal;
import java.time.LocalDateTime;
public record MovimientoReporteItem(
        Long movimientoId,
        LocalDateTime fecha,
        String tipoMovimiento,
        BigDecimal valor,
        BigDecimal saldoDisponible,
        Long cuentaId,
        String numeroCuenta
) {
    
}

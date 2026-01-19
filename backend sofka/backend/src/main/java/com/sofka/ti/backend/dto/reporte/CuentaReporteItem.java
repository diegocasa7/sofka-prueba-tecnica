package com.sofka.ti.backend.dto.reporte;

import java.math.BigDecimal;

public record CuentaReporteItem(
        Long cuentaId,
        String numeroCuenta,
        String tipoCuenta,
        Boolean estado,
        BigDecimal saldoInicial,
        BigDecimal totalCreditos,
        BigDecimal totalDebitos,
        BigDecimal saldoFinal
) {
    
}

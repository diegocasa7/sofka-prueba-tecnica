package com.sofka.ti.backend.dto.cuenta;

import java.math.BigDecimal;

public record CuentaResponse(
        Long cuentaId,
        String numeroCuenta,
        String tipoCuenta,
        BigDecimal saldoInicial,
        Boolean estado,
        Long clienteId
) {
    
}

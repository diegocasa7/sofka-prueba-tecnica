package com.sofka.ti.backend.dto.cuenta;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
public record CuentaCreateRequest(
        @NotBlank String numeroCuenta,
        @NotBlank String tipoCuenta,
        @NotNull BigDecimal saldoInicial,
        @NotNull Boolean estado
) {
    
}

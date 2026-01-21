package com.sofka.ti.backend.dto.movimiento;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record MovimientoCreateRequest(
        @NotBlank String numeroCuenta,
        @NotBlank String tipoMovimiento,
        @NotNull BigDecimal valor
) {
    
}

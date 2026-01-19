package com.sofka.ti.backend.dto.reporte;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ReporteResponse(
        Long clienteId,
        String clienteNombre,
        String clienteIdentificacion,
        LocalDate desde,
        LocalDate hasta,
        List<CuentaReporteItem> cuentas,
        BigDecimal totalCreditos,
        BigDecimal totalDebitos,
        String pdfBase64,
        List<MovimientoReporteItem> movimientos
) {
    
}

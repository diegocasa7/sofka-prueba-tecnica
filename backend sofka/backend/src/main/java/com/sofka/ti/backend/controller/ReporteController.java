package com.sofka.ti.backend.controller;

import com.sofka.ti.backend.dto.reporte.ReporteResponse;
import com.sofka.ti.backend.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/reportes")
@RequiredArgsConstructor
public class ReporteController {
    private final ReporteService reporteService;

    @GetMapping
    public ResponseEntity<ReporteResponse> generar(
            @RequestParam Long clienteId,
            @RequestParam LocalDate desde,
            @RequestParam LocalDate hasta
    ) {
        return ResponseEntity.ok(reporteService.generar(clienteId, desde, hasta));
    }
}

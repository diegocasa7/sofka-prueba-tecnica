package com.sofka.ti.backend.controller;

import com.sofka.ti.backend.dto.movimiento.*;
import com.sofka.ti.backend.service.MovimientoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movimientos")
@RequiredArgsConstructor
public class MovimientoController {

    private final MovimientoService movimientoService;

    @PostMapping
    public ResponseEntity<MovimientoResponse> crear(@Valid @RequestBody MovimientoCreateRequest req) {
        return ResponseEntity.ok(movimientoService.crear(req));
    }

    @GetMapping
    public ResponseEntity<List<MovimientoResponse>> listar(
        @RequestParam(required = false) Long cuentaId,
        @RequestParam(required = false) String numeroCuenta
    ) {
        return ResponseEntity.ok(movimientoService.listar(cuentaId, numeroCuenta));
    }
}

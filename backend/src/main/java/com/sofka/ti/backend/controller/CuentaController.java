package com.sofka.ti.backend.controller;

import com.sofka.ti.backend.dto.cuenta.*;
import com.sofka.ti.backend.service.CuentaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/cuentas")
@RequiredArgsConstructor
public class CuentaController {

    private final CuentaService cuentaService;

    @PostMapping
    public ResponseEntity<CuentaResponse> crear(@RequestParam Long clienteId, @Valid @RequestBody CuentaCreateRequest req) {
        return ResponseEntity.ok(cuentaService.crear(clienteId, req));
    }

    @GetMapping
    public ResponseEntity<List<CuentaResponse>> listar() {
        return ResponseEntity.ok(cuentaService.listar());
    }
}

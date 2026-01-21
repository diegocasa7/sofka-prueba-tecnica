package com.sofka.ti.backend.service;

import com.sofka.ti.backend.dto.cuenta.*;
import com.sofka.ti.backend.entity.ClienteEntity;
import com.sofka.ti.backend.entity.CuentaEntity;
import com.sofka.ti.backend.exception.NotFoundException;
import com.sofka.ti.backend.repository.ClienteRepository;
import com.sofka.ti.backend.repository.CuentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CuentaService {

    private final CuentaRepository cuentaRepository;
    private final ClienteRepository clienteRepository;

    public CuentaResponse crear(Long clienteId, CuentaCreateRequest req) {
        ClienteEntity cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado"));

        CuentaEntity c = CuentaEntity.builder()
                .numeroCuenta(req.numeroCuenta())
                .tipoCuenta(req.tipoCuenta())
                .saldoInicial(req.saldoInicial())
                .estado(req.estado())
                .cliente(cliente)
                .build();

        CuentaEntity guardada = cuentaRepository.save(c);
        return mapCuenta(guardada);
    }

    public List<CuentaResponse> listar() {
        return cuentaRepository.findAll().stream().map(this::mapCuenta).toList();
    }

    public CuentaEntity obtenerEntityPorNumeroCuenta(String numeroCuenta) {
        return cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new NotFoundException("Cuenta no encontrada"));
    }

    private CuentaResponse mapCuenta(CuentaEntity c) {
        return new CuentaResponse(
                c.getCuentaId(),
                c.getNumeroCuenta(),
                c.getTipoCuenta(),
                c.getSaldoInicial(),
                c.getEstado(),
                c.getCliente().getClienteId()
        );
    }
}

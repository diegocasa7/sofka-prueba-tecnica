package com.sofka.ti.backend.service;

import com.sofka.ti.backend.entity.ClienteEntity;
import com.sofka.ti.backend.entity.CuentaEntity;
import com.sofka.ti.backend.repository.ClienteRepository;
import com.sofka.ti.backend.repository.CuentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CuentaCrudService {
    private final CuentaRepository cuentaRepository;
    private final ClienteRepository clienteRepository;

    public CuentaEntity crear(Long clienteId, CuentaEntity cuenta) {
        ClienteEntity cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        cuenta.setCliente(cliente);
        return cuentaRepository.save(cuenta);
    }

    public List<CuentaEntity> listar() {
        return cuentaRepository.findAll();
    }

    public CuentaEntity obtener(Long id) {
        return cuentaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
    }

    public CuentaEntity actualizar(Long id, CuentaEntity cambios) {
        CuentaEntity actual = obtener(id);

        if (cambios.getNumeroCuenta() != null) actual.setNumeroCuenta(cambios.getNumeroCuenta());
        if (cambios.getTipoCuenta() != null) actual.setTipoCuenta(cambios.getTipoCuenta());
        if (cambios.getSaldoInicial() != null) actual.setSaldoInicial(cambios.getSaldoInicial());
        if (cambios.getEstado() != null) actual.setEstado(cambios.getEstado());

        return cuentaRepository.save(actual);
    }

    public void eliminar(Long id) {
        CuentaEntity actual = obtener(id);
        cuentaRepository.delete(actual);
    }
}

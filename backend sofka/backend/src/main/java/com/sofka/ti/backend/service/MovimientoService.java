package com.sofka.ti.backend.service;

import com.sofka.ti.backend.dto.movimiento.MovimientoCreateRequest;
import com.sofka.ti.backend.dto.movimiento.MovimientoResponse;
import com.sofka.ti.backend.entity.CuentaEntity;
import com.sofka.ti.backend.entity.MovimientoEntity;
import com.sofka.ti.backend.exception.BusinessException;
import com.sofka.ti.backend.repository.MovimientoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final CuentaService cuentaService;

    @Transactional
    public MovimientoResponse crear(MovimientoCreateRequest req) {
        CuentaEntity cuenta = cuentaService.obtenerEntityPorNumeroCuenta(req.numeroCuenta());
        BigDecimal saldoActual = obtenerSaldoActual(cuenta);

        BigDecimal valorNormalizado = req.valor().abs();
        BigDecimal delta;

        if ("DEPOSITO".equalsIgnoreCase(req.tipoMovimiento())) {
            delta = valorNormalizado;
        } else if ("RETIRO".equalsIgnoreCase(req.tipoMovimiento())) {
            delta = valorNormalizado.negate();
        } else {
            throw new BusinessException("Tipo de movimiento inválido");
        }


        BigDecimal nuevoSaldo = saldoActual.add(delta);
        if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("Saldo no disponible");
        }

        MovimientoEntity m = MovimientoEntity.builder()
                .cuenta(cuenta)
                .tipoMovimiento(req.tipoMovimiento())
                .valor(delta)
                .saldoDisponible(nuevoSaldo)
                .build();

        return toResponse(movimientoRepository.save(m));
    }

    public List<MovimientoResponse> listar(Long cuentaId, String numeroCuenta) {
        if (cuentaId != null) {
            return movimientoRepository
                    .findByCuenta_CuentaIdOrderByFechaDescMovimientoIdDesc(cuentaId)
                    .stream()
                    .map(this::toResponse)
                    .toList();
        }

        if (numeroCuenta != null && !numeroCuenta.isBlank()) {
            return movimientoRepository
                    .findByCuenta_NumeroCuentaOrderByFechaDescMovimientoIdDesc(numeroCuenta)
                    .stream()
                    .map(this::toResponse)
                    .toList();
        }

        return movimientoRepository
                .findAllByOrderByFechaDescMovimientoIdDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private BigDecimal obtenerSaldoActual(CuentaEntity cuenta) {
        return movimientoRepository
                .findTopByCuenta_CuentaIdOrderByFechaDescMovimientoIdDesc(cuenta.getCuentaId())
                .map(MovimientoEntity::getSaldoDisponible)
                .orElse(cuenta.getSaldoInicial());
    }

    private MovimientoResponse toResponse(MovimientoEntity e) {
        return new MovimientoResponse(
                e.getMovimientoId(),
                e.getFecha(),
                e.getTipoMovimiento(),
                e.getValor(),
                e.getSaldoDisponible(),
                e.getCuenta().getCuentaId()
        );
    }
}

package com.sofka.ti.backend.repository;

import com.sofka.ti.backend.entity.MovimientoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MovimientoRepository extends JpaRepository<MovimientoEntity, Long> {

    // Reporte / rangos
    List<MovimientoEntity> findByCuenta_CuentaIdAndFechaBetween(Long cuentaId, LocalDateTime desde, LocalDateTime hasta);

    List<MovimientoEntity> findByCuenta_CuentaIdInAndFechaGreaterThanEqualAndFechaLessThanOrderByFechaAsc(
            List<Long> cuentaIds,
            LocalDateTime desde,
            LocalDateTime hastaExclusivo
    );

    Optional<MovimientoEntity> findFirstByCuenta_CuentaIdAndFechaLessThanOrderByFechaDesc(
            Long cuentaId,
            LocalDateTime hastaExclusivo
    );

    // Para calcular saldo actual (último movimiento)
    Optional<MovimientoEntity> findTopByCuenta_CuentaIdOrderByFechaDescMovimientoIdDesc(Long cuentaId);

    // Para historial (lo que te faltaba)
    List<MovimientoEntity> findAllByOrderByFechaDescMovimientoIdDesc();

    List<MovimientoEntity> findByCuenta_CuentaIdOrderByFechaDescMovimientoIdDesc(Long cuentaId);

    List<MovimientoEntity> findByCuenta_NumeroCuentaOrderByFechaDescMovimientoIdDesc(String numeroCuenta);
}

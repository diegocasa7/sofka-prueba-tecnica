package com.sofka.ti.backend.service;

import com.sofka.ti.backend.dto.reporte.CuentaReporteItem;
import com.sofka.ti.backend.dto.reporte.MovimientoReporteItem;
import com.sofka.ti.backend.dto.reporte.ReporteResponse;
import com.sofka.ti.backend.entity.ClienteEntity;
import com.sofka.ti.backend.entity.CuentaEntity;
import com.sofka.ti.backend.entity.MovimientoEntity;
import com.sofka.ti.backend.repository.ClienteRepository;
import com.sofka.ti.backend.repository.CuentaRepository;
import com.sofka.ti.backend.repository.MovimientoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReporteService {

    private final ClienteRepository clienteRepository;
    private final CuentaRepository cuentaRepository;
    private final MovimientoRepository movimientoRepository;
    private final ReportePdfService reportePdfService;

    public ReporteResponse generar(Long clienteId, LocalDate desde, LocalDate hasta) {
        if (clienteId == null) {
            throw new IllegalArgumentException("clienteId es obligatorio");
        }
        if (desde == null || hasta == null) {
            throw new IllegalArgumentException("desde y hasta son obligatorios");
        }
        if (desde.isAfter(hasta)) {
            throw new IllegalArgumentException("desde no puede ser mayor que hasta");
        }

        ClienteEntity cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new NoSuchElementException("Cliente no existe"));

        LocalDateTime desdeDt = desde.atStartOfDay();
        LocalDateTime hastaExclusivo = hasta.plusDays(1).atStartOfDay();

        List<CuentaEntity> cuentas = cuentaRepository.findByCliente_ClienteId(clienteId);

        if (cuentas.isEmpty()) {
            return new ReporteResponse(
                    cliente.getClienteId(),
                    cliente.getPersona().getNombre(),
                    cliente.getPersona().getIdentificacion(),
                    desde,
                    hasta,
                    List.of(),
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    "",
                    List.of()
            );
        }

        List<Long> cuentaIds = cuentas.stream().map(CuentaEntity::getCuentaId).toList();

        List<MovimientoEntity> movimientosEnRango =
                movimientoRepository.findByCuenta_CuentaIdInAndFechaGreaterThanEqualAndFechaLessThanOrderByFechaAsc(
                        cuentaIds, desdeDt, hastaExclusivo
                );

        Map<Long, List<MovimientoEntity>> movsPorCuenta = movimientosEnRango.stream()
                .collect(Collectors.groupingBy(m -> m.getCuenta().getCuentaId()));

        BigDecimal totalCreditos = BigDecimal.ZERO;
        BigDecimal totalDebitos = BigDecimal.ZERO;

        List<CuentaReporteItem> cuentasReporte = new ArrayList<>();

        for (CuentaEntity cuenta : cuentas) {
            List<MovimientoEntity> movs = movsPorCuenta.getOrDefault(cuenta.getCuentaId(), List.of());

            BigDecimal creditos = sumByTipo(movs, "DEPOSITO");
            BigDecimal debitos = sumByTipo(movs, "RETIRO");

            BigDecimal saldoFinal = movimientoRepository
                    .findFirstByCuenta_CuentaIdAndFechaLessThanOrderByFechaDesc(cuenta.getCuentaId(), hastaExclusivo)
                    .map(MovimientoEntity::getSaldoDisponible)
                    .orElse(cuenta.getSaldoInicial());

            totalCreditos = totalCreditos.add(creditos);
            totalDebitos = totalDebitos.add(debitos);

            cuentasReporte.add(new CuentaReporteItem(
                    cuenta.getCuentaId(),
                    cuenta.getNumeroCuenta(),
                    cuenta.getTipoCuenta(),
                    cuenta.getEstado(),
                    cuenta.getSaldoInicial(),
                    creditos,
                    debitos,
                    saldoFinal
            ));
        }

        List<MovimientoReporteItem> movimientosReporte = movimientosEnRango.stream()
                .map(m -> new MovimientoReporteItem(
                        m.getMovimientoId(),
                        m.getFecha(),
                        m.getTipoMovimiento(),
                        m.getValor(),
                        m.getSaldoDisponible(),
                        m.getCuenta().getCuentaId(),
                        m.getCuenta().getNumeroCuenta()
                ))
                .toList();

        ReporteResponse base = new ReporteResponse(
                cliente.getClienteId(),
                cliente.getPersona().getNombre(),
                cliente.getPersona().getIdentificacion(),
                desde,
                hasta,
                cuentasReporte,
                totalCreditos,
                totalDebitos,
                "",
                movimientosReporte
        );

        String pdfBase64 = java.util.Base64.getEncoder()
                .encodeToString(reportePdfService.generarPdf(base));

        return new ReporteResponse(
                base.clienteId(),
                base.clienteNombre(),
                base.clienteIdentificacion(),
                base.desde(),
                base.hasta(),
                base.cuentas(),
                base.totalCreditos(),
                base.totalDebitos(),
                pdfBase64,
                base.movimientos()
        );
    }

    private BigDecimal sumByTipo(List<MovimientoEntity> movs, String tipo) {
        return movs.stream()
                .filter(m -> tipo.equalsIgnoreCase(m.getTipoMovimiento()))
                .map(MovimientoEntity::getValor)
                .filter(Objects::nonNull)
                .map(BigDecimal::abs)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

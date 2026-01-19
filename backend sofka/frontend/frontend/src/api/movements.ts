import { apiFetch } from "./http";
import type { MovimientoCreateRequest, MovimientoResponse } from "./types";

export function crearMovimiento(body: MovimientoCreateRequest) {
  return apiFetch<MovimientoResponse>("/movimientos", {
    method: "POST",
    body: JSON.stringify(body),
  });
}

export function listarMovimientos(params?: {
  numeroCuenta?: string;
  cuentaId?: number;
}) {
  const qs = new URLSearchParams();
  if (params?.numeroCuenta) qs.set("numeroCuenta", params.numeroCuenta);
  if (params?.cuentaId != null) qs.set("cuentaId", String(params.cuentaId));

  const suffix = qs.toString() ? `?${qs.toString()}` : "";
  return apiFetch<MovimientoResponse[]>(`/movimientos${suffix}`);
}

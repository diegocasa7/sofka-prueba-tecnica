import { apiFetch } from "./http";
import type { CuentaCreateRequest, CuentaResponse } from "./types";

export function listarCuentas() {
  return apiFetch<CuentaResponse[]>("/cuentas");
}

export function crearCuenta(clienteId: number, body: CuentaCreateRequest) {
  return apiFetch(`/cuentas?clienteId=${clienteId}`, {
    method: "POST",
    body: JSON.stringify(body),
  });
}


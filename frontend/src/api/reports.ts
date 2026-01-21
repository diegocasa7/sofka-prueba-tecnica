import { apiFetch } from "./http";
import type { ReporteResponse } from "./types";

export function generarReporte(clienteId: number, desde: string, hasta: string) {
  const qs = new URLSearchParams({
    clienteId: String(clienteId),
    desde,
    hasta,
  }).toString();

  return apiFetch<ReporteResponse>(`/reportes?${qs}`);
}

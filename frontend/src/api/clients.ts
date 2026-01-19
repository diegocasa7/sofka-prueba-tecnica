import { apiFetch } from "./http";
import type { ClienteCreateRequest, ClienteResponse } from "./types";

export function listarClientes() {
  return apiFetch<ClienteResponse[]>("/clientes");
}

export function crearCliente(body: ClienteCreateRequest) {
  return apiFetch<ClienteResponse>("/clientes", {
    method: "POST",
    body: JSON.stringify(body),
  });
}

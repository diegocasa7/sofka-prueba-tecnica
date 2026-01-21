import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import Clientes from "../pages/Clientes";

import * as clientsApi from "../api/clients";

describe("Clientes - modal", () => {
  it("abre el modal al presionar Nuevo", async () => {
    vi.spyOn(clientsApi, "listarClientes").mockResolvedValue([]);
    vi.spyOn(clientsApi, "crearCliente").mockResolvedValue(undefined as any);

    render(<Clientes />);

    const btnNuevo = screen.getByRole("button", { name: "Nuevo" });
    await userEvent.click(btnNuevo);

    expect(screen.getByText("Nuevo Cliente")).toBeInTheDocument();
    expect(screen.getByRole("button", { name: "Guardar" })).toBeInTheDocument();
  });
});

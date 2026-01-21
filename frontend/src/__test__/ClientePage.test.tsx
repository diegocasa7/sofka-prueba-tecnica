// import { describe, it, expect } from "vitest";
import { render, screen } from "@testing-library/react";
import Clientes from "../pages/Clientes";
// import "@testing-library/jest-dom";

describe("Clientes", () => {
  it("renderiza la pantalla de clientes", () => {
    render(<Clientes />);
    expect(screen.getByText(/clientes/i)).toBeInTheDocument();
  });
});

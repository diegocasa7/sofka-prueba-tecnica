import { useState } from "react";
import Nav from "./components/Nav";
import Clientes from "./pages/Clientes";
import Cuentas from "./pages/Cuentas";
import Movimientos from "./pages/Movimientos";
import Reportes from "./pages/Reportes";

export default function App() {
  const [page, setPage] = useState("clientes");

  return (
    <div className="app-shell">
      <div className="header">BANCO</div>

      <Nav current={page} onGo={setPage} />

      <div className="content">
        <div className="card">
          {page === "clientes" && <Clientes />}
          {page === "cuentas" && <Cuentas />}
          {page === "movimientos" && <Movimientos />}
          {page === "reportes" && <Reportes />}
        </div>
      </div>
    </div>
  );
}

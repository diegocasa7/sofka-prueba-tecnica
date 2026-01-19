import { useState } from "react";
import Alert from "../components/Alert";
import { generarReporte } from "../api/reports";
import type { ReporteResponse } from "../api/types";

function downloadBase64Pdf(base64: string, filename: string) {
  const binary = atob(base64);
  const bytes = new Uint8Array(binary.length);
  for (let i = 0; i < binary.length; i++) bytes[i] = binary.charCodeAt(i);

  const blob = new Blob([bytes], { type: "application/pdf" });
  const url = URL.createObjectURL(blob);

  const a = document.createElement("a");
  a.href = url;
  a.download = filename;
  document.body.appendChild(a);
  a.click();
  a.remove();

  URL.revokeObjectURL(url);
}

export default function Reportes() {
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const [clienteId, setClienteId] = useState<number>(1);
  const [desde, setDesde] = useState<string>("2026-01-01");
  const [hasta, setHasta] = useState<string>("2026-01-31");

  const [data, setData] = useState<ReporteResponse | null>(null);

  async function onBuscar() {
    setError("");
    setLoading(true);
    try {
      const res = await generarReporte(clienteId, desde, hasta);
      setData(res);
    } catch (e: any) {
      setError(e.message);
      setData(null);
    } finally {
      setLoading(false);
    }
  }

  function onDescargar() {
    if (!data?.pdfBase64) return;
    const name = `reporte_cliente_${data.clienteId}_${data.desde}_a_${data.hasta}.pdf`;
    downloadBase64Pdf(data.pdfBase64, name);
  }

  return (
    <div>
      <h2 style={{ margin: 0 }}>Reportes</h2>

      <div className="toolbar" style={{ alignItems: "center" }}>
        <div style={{ display: "flex", gap: 10, flexWrap: "wrap" }}>
          <label className="label" style={{ width: 140 }}>
            Cliente ID
            <input
              className="input-sm"
              type="number"
              value={clienteId}
              onChange={(e) => setClienteId(Number(e.target.value))}
            />
          </label>

          <label className="label" style={{ width: 180 }}>
            Desde
            <input
              className="input-sm"
              type="date"
              value={desde}
              onChange={(e) => setDesde(e.target.value)}
            />
          </label>

          <label className="label" style={{ width: 180 }}>
            Hasta
            <input
              className="input-sm"
              type="date"
              value={hasta}
              onChange={(e) => setHasta(e.target.value)}
            />
          </label>
        </div>

        <div style={{ display: "flex", gap: 8 }}>
          <button className="btn" onClick={onBuscar} disabled={loading}>
            {loading ? "Buscando..." : "Buscar"}
          </button>
          <button className="btn primary" onClick={onDescargar} disabled={!data?.pdfBase64}>
            Descargar PDF
          </button>
        </div>
      </div>

      <Alert message={error} />

      {!data && (
        <div style={{ padding: 12 }}>
          Ingresa Cliente ID y fechas, luego presiona Buscar.
        </div>
      )}

      {data && (
        <div style={{ display: "grid", gap: 16 }}>
          <div style={{ display: "flex", justifyContent: "space-between", gap: 12, flexWrap: "wrap" }}>
            <div>
              <div><b>Cliente:</b> {data.clienteNombre}</div>
              <div><b>Identificación:</b> {data.clienteIdentificacion}</div>
              <div><b>Rango:</b> {data.desde} a {data.hasta}</div>
            </div>
            <div style={{ textAlign: "right" }}>
              <div><b>Total créditos:</b> {data.totalCreditos}</div>
              <div><b>Total débitos:</b> {data.totalDebitos}</div>
            </div>
          </div>

          <div>
            <h3 style={{ margin: "0 0 8px" }}>Cuentas</h3>
            <table className="table">
              <thead>
                <tr>
                  <th>Número</th>
                  <th>Tipo</th>
                  <th>Saldo inicial</th>
                  <th>Créditos</th>
                  <th>Débitos</th>
                  <th>Saldo final</th>
                  <th>Estado</th>
                </tr>
              </thead>
              <tbody>
                {data.cuentas.map((c) => (
                  <tr key={c.cuentaId}>
                    <td>{c.numeroCuenta}</td>
                    <td>{c.tipoCuenta}</td>
                    <td>{c.saldoInicial}</td>
                    <td>{c.totalCreditos}</td>
                    <td>{c.totalDebitos}</td>
                    <td>{c.saldoFinal}</td>
                    <td>{c.estado ? "Activa" : "Inactiva"}</td>
                  </tr>
                ))}
                {data.cuentas.length === 0 && (
                  <tr>
                    <td colSpan={7} style={{ padding: 14 }}>No hay cuentas.</td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>

          <div>
            <h3 style={{ margin: "0 0 8px" }}>Movimientos</h3>
            <table className="table">
              <thead>
                <tr>
                  <th>Fecha</th>
                  <th>Tipo</th>
                  <th>Valor</th>
                  <th>Saldo</th>
                  <th>Cuenta</th>
                </tr>
              </thead>
              <tbody>
                {data.movimientos.map((m) => (
                  <tr key={m.movimientoId}>
                    <td>{m.fecha}</td>
                    <td>{m.tipoMovimiento}</td>
                    <td>{m.valor}</td>
                    <td>{m.saldoDisponible}</td>
                    <td>{m.numeroCuenta}</td>
                  </tr>
                ))}
                {data.movimientos.length === 0 && (
                  <tr>
                    <td colSpan={5} style={{ padding: 14 }}>No hay movimientos en el rango.</td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </div>
  );
}

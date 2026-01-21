import { useEffect, useState } from "react";
import Alert from "../components/Alert";
import { crearMovimiento, listarMovimientos } from "../api/movements";
import type { MovimientoCreateRequest, MovimientoResponse } from "../api/types";

function Modal({
  title,
  open,
  onClose,
  children,
}: {
  title: string;
  open: boolean;
  onClose: () => void;
  children: React.ReactNode;
}) {
  if (!open) return null;

  return (
    <div className="modal-overlay" onMouseDown={onClose}>
      <div className="modal" onMouseDown={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <b>{title}</b>
          <button className="btn" onClick={onClose}>
            Cerrar
          </button>
        </div>
        {children}
      </div>
    </div>
  );
}

export default function Movimientos() {
  const [error, setError] = useState("");
  const [q, setQ] = useState("");
  const [open, setOpen] = useState(false);

  const [rows, setRows] = useState<MovimientoResponse[]>([]);

  const [form, setForm] = useState<MovimientoCreateRequest>({
    numeroCuenta: "",
    tipoMovimiento: "",
    valor: 0,
  });

  useEffect(() => {
    listarMovimientos()
      .then(setRows)
      .catch(() => setError("Error interno"));
  }, []);

  async function onSearch(v: string) {
    try {
      setError("");
      const data = await listarMovimientos({ numeroCuenta: v });
      setRows(data);
    } catch (e: any) {
      setError(e.message);
    }
  }

  async function onCreate(e: React.FormEvent) {
    e.preventDefault();
    setError("");

    const numeroCuentaCreada = form.numeroCuenta;

    try {
      await crearMovimiento(form);
      setOpen(false);
      setForm({ numeroCuenta: "", tipoMovimiento: "", valor: 0 });

      const data = await listarMovimientos({
        numeroCuenta: numeroCuentaCreada ? numeroCuentaCreada : q,
      });
      setRows(data);
    } catch (e: any) {
      setError(e.message);
    }
  }

  return (
    <div>
      <h2 style={{ margin: 0 }}>Movimientos</h2>

      <div className="toolbar">
        <input
          className="input"
          placeholder="Buscar (número de cuenta)"
          value={q}
          onChange={async (e) => {
            const v = e.target.value;
            setQ(v);
            await onSearch(v);
          }}
        />
        <button className="btn primary" onClick={() => setOpen(true)}>
          Nuevo
        </button>
      </div>

      <Alert message={error} />

      <table className="table">
        <thead>
          <tr>
            <th>Movimiento ID</th>
            <th>Fecha</th>
            <th>Tipo</th>
            <th>Valor</th>
            <th>Saldo disponible</th>
            <th>Cuenta ID</th>
          </tr>
        </thead>
        <tbody>
          {rows.length ? (
            rows.map((m) => (
              <tr key={m.movimientoId}>
                <td>{m.movimientoId}</td>
                <td>{m.fecha}</td>
                <td>{m.tipoMovimiento}</td>
                <td>{m.valor}</td>
                <td>{m.saldoDisponible}</td>
                <td>{m.cuentaId}</td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan={6} style={{ padding: 14 }}>
                No hay movimientos.
              </td>
            </tr>
          )}
        </tbody>
      </table>

      <Modal title="Nuevo Movimiento" open={open} onClose={() => setOpen(false)}>
        <form onSubmit={onCreate} className="form-grid">
          <label className="label full">
            Número de cuenta
            <input
              className="input-sm"
              value={form.numeroCuenta}
              onChange={(e) => setForm({ ...form, numeroCuenta: e.target.value })}
            />
          </label>

          <label className="label">
            Tipo de movimiento
            <input
              className="input-sm"
              placeholder="DEPOSITO / RETIRO"
              value={form.tipoMovimiento}
              onChange={(e) => setForm({ ...form, tipoMovimiento: e.target.value })}
            />
          </label>

          <label className="label">
            Valor
            <input
              className="input-sm"
              type="number"
              value={form.valor}
              onChange={(e) => setForm({ ...form, valor: Number(e.target.value) })}
            />
          </label>

          <div className="full" style={{ display: "flex", justifyContent: "flex-end", gap: 8 }}>
            <button type="button" className="btn" onClick={() => setOpen(false)}>
              Cancelar
            </button>
            <button type="submit" className="btn primary">
              Guardar
            </button>
          </div>
        </form>
      </Modal>
    </div>
  );
}

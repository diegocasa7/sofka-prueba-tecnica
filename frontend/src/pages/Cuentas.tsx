import { useEffect, useMemo, useState } from "react";
import Alert from "../components/Alert";
import { crearCuenta, listarCuentas } from "../api/accounts";
import type { CuentaCreateRequest, CuentaResponse } from "../api/types";

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

export default function Cuentas() {
  const [items, setItems] = useState<CuentaResponse[]>([]);
  const [error, setError] = useState("");
  const [q, setQ] = useState("");
  const [open, setOpen] = useState(false);

  const [clienteId, setClienteId] = useState<number>(1);
  const [form, setForm] = useState<CuentaCreateRequest>({
    numeroCuenta: "",
    tipoCuenta: "",
    saldoInicial: 0,
    estado: true,
  });

  async function load() {
    const data = await listarCuentas();
    setItems(data);
  }

  useEffect(() => {
    load().catch((e) => setError(e.message));
  }, []);

  const filtered = useMemo(() => {
    const t = q.trim().toLowerCase();
    if (!t) return items;
    return items.filter((c) => {
      const nro = c.numeroCuenta?.toLowerCase() || "";
      const tipo = c.tipoCuenta?.toLowerCase() || "";
      const cli = String(c.clienteId || "");
      return nro.includes(t) || tipo.includes(t) || cli.includes(t);
    });
  }, [items, q]);

  async function onCreate(e: React.FormEvent) {
    e.preventDefault();
    setError("");
    try {
      await crearCuenta(clienteId, form);
      setOpen(false);
      setForm({ numeroCuenta: "", tipoCuenta: "", saldoInicial: 0, estado: true });
      await load();
    } catch (e: any) {
      setError(e.message);
    }
  }

  return (
    <div>
      <h2 style={{ margin: 0 }}>Cuentas</h2>

      <div className="toolbar">
        <input
          className="input"
          placeholder="Buscar"
          value={q}
          onChange={(e) => setQ(e.target.value)}
        />
        <button className="btn primary" onClick={() => setOpen(true)}>
          Nuevo
        </button>
      </div>

      <Alert message={error} />

      <table className="table">
        <thead>
          <tr>
            <th>Cuenta ID</th>
            <th>Número</th>
            <th>Tipo</th>
            <th>Saldo inicial</th>
            <th>Estado</th>
            <th>Cliente ID</th>
          </tr>
        </thead>
        <tbody>
          {filtered.map((c) => (
            <tr key={c.cuentaId}>
              <td>{c.cuentaId}</td>
              <td>{c.numeroCuenta}</td>
              <td>{c.tipoCuenta}</td>
              <td>{c.saldoInicial}</td>
              <td>{c.estado ? "Activa" : "Inactiva"}</td>
              <td>{c.clienteId}</td>
            </tr>
          ))}
          {filtered.length === 0 && (
            <tr>
              <td colSpan={6} style={{ padding: 14 }}>
                No hay registros.
              </td>
            </tr>
          )}
        </tbody>
      </table>

      <Modal title="Nueva Cuenta" open={open} onClose={() => setOpen(false)}>
        <form onSubmit={onCreate} className="form-grid">
          <label className="label">
            Cliente ID
            <input
              className="input-sm"
              type="number"
              value={clienteId}
              onChange={(e) => setClienteId(Number(e.target.value))}
            />
          </label>

          <label className="label">
            Número de cuenta
            <input
              className="input-sm"
              value={form.numeroCuenta}
              onChange={(e) => setForm({ ...form, numeroCuenta: e.target.value })}
            />
          </label>

          <label className="label">
            Tipo de cuenta
            <input
              className="input-sm"
              value={form.tipoCuenta}
              onChange={(e) => setForm({ ...form, tipoCuenta: e.target.value })}
            />
          </label>

          <label className="label">
            Saldo inicial
            <input
              className="input-sm"
              type="number"
              value={form.saldoInicial}
              onChange={(e) => setForm({ ...form, saldoInicial: Number(e.target.value) })}
            />
          </label>

          <label className="label full" style={{ display: "flex", gap: 8, alignItems: "center" }}>
            <input
              type="checkbox"
              checked={form.estado}
              onChange={(e) => setForm({ ...form, estado: e.target.checked })}
            />
            Activa
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

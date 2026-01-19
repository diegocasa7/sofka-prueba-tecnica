import { useEffect, useMemo, useState } from "react";
import Alert from "../components/Alert";
import { crearCliente, listarClientes } from "../api/clients";
import type { ClienteCreateRequest, ClienteResponse } from "../api/types";

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

export default function Clientes() {
  const [items, setItems] = useState<ClienteResponse[]>([]);
  const [error, setError] = useState("");
  const [q, setQ] = useState("");
  const [open, setOpen] = useState(false);

  const [form, setForm] = useState<ClienteCreateRequest>({
    persona: {
      nombre: "",
      genero: "",
      edad: 0,
      identificacion: "",
      direccion: "",
      telefono: "",
    },
    contrasena: "",
    estado: true,
  });

  async function load() {
    const data = await listarClientes();
    setItems(data);
  }

  useEffect(() => {
    load().catch((e) => setError(e.message));
  }, []);

  const filtered = useMemo(() => {
    const t = q.trim().toLowerCase();
    if (!t) return items;
    return items.filter((c) => {
      const nombre = c.persona.nombre?.toLowerCase() || "";
      const id = c.persona.identificacion?.toLowerCase() || "";
      return nombre.includes(t) || id.includes(t);
    });
  }, [items, q]);

  async function onCreate(e: React.FormEvent) {
    e.preventDefault();
    setError("");
    try {
      await crearCliente(form);
      setOpen(false);
      setForm({
        persona: {
          nombre: "",
          genero: "",
          edad: 0,
          identificacion: "",
          direccion: "",
          telefono: "",
        },
        contrasena: "",
        estado: true,
      });
      await load();
    } catch (e: any) {
      setError(e.message);
    }
  }

  return (
    <div>
      <h2 style={{ margin: 0 }}>Clientes</h2>

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
            <th>Cliente ID</th>
            <th>Nombre</th>
            <th>Identificación</th>
            <th>Teléfono</th>
            <th>Estado</th>
          </tr>
        </thead>
        <tbody>
          {filtered.map((c) => (
            <tr key={c.clienteId}>
              <td>{c.clienteId}</td>
              <td>{c.persona.nombre}</td>
              <td>{c.persona.identificacion}</td>
              <td>{c.persona.telefono || ""}</td>
              <td>{c.estado ? "Activo" : "Inactivo"}</td>
            </tr>
          ))}
          {filtered.length === 0 && (
            <tr>
              <td colSpan={5} style={{ padding: 14 }}>
                No hay registros.
              </td>
            </tr>
          )}
        </tbody>
      </table>

      <Modal title="Nuevo Cliente" open={open} onClose={() => setOpen(false)}>
        <form onSubmit={onCreate} className="form-grid">
          <label className="label">
            Nombre
            <input
              className="input-sm"
              value={form.persona.nombre}
              onChange={(e) =>
                setForm({ ...form, persona: { ...form.persona, nombre: e.target.value } })
              }
            />
          </label>

          <label className="label">
            Identificación
            <input
              className="input-sm"
              value={form.persona.identificacion}
              onChange={(e) =>
                setForm({
                  ...form,
                  persona: { ...form.persona, identificacion: e.target.value },
                })
              }
            />
          </label>

          <label className="label">
            Género
            <input
              className="input-sm"
              value={form.persona.genero || ""}
              onChange={(e) =>
                setForm({ ...form, persona: { ...form.persona, genero: e.target.value } })
              }
            />
          </label>

          <label className="label">
            Edad
            <input
              className="input-sm"
              type="number"
              value={form.persona.edad ?? 0}
              onChange={(e) =>
                setForm({ ...form, persona: { ...form.persona, edad: Number(e.target.value) } })
              }
            />
          </label>

          <label className="label full">
            Dirección
            <input
              className="input-sm"
              value={form.persona.direccion || ""}
              onChange={(e) =>
                setForm({ ...form, persona: { ...form.persona, direccion: e.target.value } })
              }
            />
          </label>

          <label className="label">
            Teléfono
            <input
              className="input-sm"
              value={form.persona.telefono || ""}
              onChange={(e) =>
                setForm({ ...form, persona: { ...form.persona, telefono: e.target.value } })
              }
            />
          </label>

          <label className="label">
            Contraseña
            <input
              className="input-sm"
              value={form.contrasena}
              onChange={(e) => setForm({ ...form, contrasena: e.target.value })}
            />
          </label>

          <label className="label full" style={{ display: "flex", gap: 8, alignItems: "center" }}>
            <input
              type="checkbox"
              checked={form.estado}
              onChange={(e) => setForm({ ...form, estado: e.target.checked })}
            />
            Activo
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

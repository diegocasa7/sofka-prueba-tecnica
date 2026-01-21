export default function Nav({
  current,
  onGo,
}: {
  current: string;
  onGo: (p: string) => void;
}) {
  const Item = ({ id, label }: { id: string; label: string }) => (
    <div
      className={`item ${current === id ? "active" : ""}`}
      onClick={() => onGo(id)}
    >
      {label}
    </div>
  );

  return (
    <div className="sidebar">
      <Item id="clientes" label="Clientes" />
      <Item id="cuentas" label="Cuentas" />
      <Item id="movimientos" label="Movimientos" />
      <Item id="reportes" label="Reportes" />
    </div>
  );
}

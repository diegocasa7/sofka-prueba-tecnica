type Props = {
  label: string;
  value: string | number;
  onChange: (v: string) => void;
  type?: string;
};

export default function Field({ label, value, onChange, type = "text" }: Props) {
  return (
    <label style={{ display: "grid", gap: 4 }}>
      <span>{label}</span>
      <input
        type={type}
        value={value}
        onChange={(e) => onChange(e.target.value)}
      />
    </label>
  );
}

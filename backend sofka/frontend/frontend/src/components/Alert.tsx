export default function Alert({ message }: { message: string }) {
  if (!message) return null;

  return (
    <div style={{ background: "#ffd7d7", padding: 12, marginBottom: 12 }}>
      {message}
    </div>
  );
}

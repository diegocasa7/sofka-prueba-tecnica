export type PersonaRequest = {
  nombre: string;
  genero?: string;
  edad?: number;
  identificacion: string;
  direccion?: string;
  telefono?: string;
};

export type ClienteCreateRequest = {
  persona: PersonaRequest;
  contrasena: string;
  estado: boolean;
};

export type PersonaResponse = PersonaRequest & { personaId: number };

export type ClienteResponse = {
  clienteId: number;
  persona: PersonaResponse;
  estado: boolean;
};

export type CuentaCreateRequest = {
  numeroCuenta: string;
  tipoCuenta: string;
  saldoInicial: number;
  estado: boolean;
};

export type CuentaResponse = {
  cuentaId: number;
  numeroCuenta: string;
  tipoCuenta: string;
  saldoInicial: number;
  estado: boolean;
  clienteId: number;
};

export type MovimientoCreateRequest = {
  numeroCuenta: string;
  tipoMovimiento: string;
  valor: number;
};

export type MovimientoResponse = {
  movimientoId: number;
  fecha: string;
  tipoMovimiento: string;
  valor: number;
  saldoDisponible: number;
  cuentaId: number;
};

export type ReporteCuentaItem = {
  cuentaId: number;
  numeroCuenta: string;
  tipoCuenta: string;
  estado: boolean;
  saldoInicial: number;
  totalCreditos: number;
  totalDebitos: number;
  saldoFinal: number;
};

export type ReporteMovimientoItem = {
  movimientoId: number;
  fecha: string;
  tipoMovimiento: string;
  valor: number;
  saldoDisponible: number;
  cuentaId: number;
  numeroCuenta: string;
};

export type ReporteResponse = {
  clienteId: number;
  clienteNombre: string;
  clienteIdentificacion: string;
  desde: string;
  hasta: string;
  cuentas: ReporteCuentaItem[];
  totalCreditos: number;
  totalDebitos: number;
  pdfBase64: string;
  movimientos: ReporteMovimientoItem[];
};


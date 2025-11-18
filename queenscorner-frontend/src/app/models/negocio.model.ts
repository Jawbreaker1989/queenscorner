export type EstadoNegocio = 'INICIADO' | 'EN_PROGRESO' | 'FINALIZADO' | 'CANCELADO';

export interface NegocioRequest {
  cotizacionId: number;
  descripcion: string;
  observaciones?: string;
}

export interface NegocioResponse {
  id: number;
  cotizacionId: number;
  codigo: string;
  cliente: {
    id: number;
    nombre: string;
    email: string;
    telefono: string;
    direccion: string;
    ciudad: string;
  };
  descripcion: string;
  observaciones?: string;
  estado: EstadoNegocio;
  fechaCreacion: string;
  fechaInicio?: string;
  fechaFinalizacion?: string;
  monto: number;
}

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
  status: number;
}

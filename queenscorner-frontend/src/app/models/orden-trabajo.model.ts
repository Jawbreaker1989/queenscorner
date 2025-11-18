export type EstadoOrdenTrabajo = 'PENDIENTE' | 'EN_PROGRESO' | 'COMPLETADA' | 'CANCELADA';

export interface DetalleOrdenTrabajo {
  id?: number;
  descripcion: string;
  cantidad: number;
  precioUnitario: number;
  subtotal?: number;
}

export interface OrdenTrabajoRequest {
  negocioId: number;
  descripcion: string;
  fechaInicio: string;
  fechaEstimadaFin?: string;
  observaciones?: string;
  detalles: DetalleOrdenTrabajo[];
}

export interface OrdenTrabajoResponse {
  id: number;
  negocioId: number;
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
  fechaInicio: string;
  fechaEstimadaFin?: string;
  fechaCompletacion?: string;
  observaciones?: string;
  estado: EstadoOrdenTrabajo;
  detalles: DetalleOrdenTrabajo[];
  subtotal: number;
  impuestos: number;
  total: number;
  fechaCreacion: string;
}

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
  status: number;
}

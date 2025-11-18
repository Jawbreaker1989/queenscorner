export type EstadoCotizacion = 'BORRADOR' | 'ENVIADA' | 'APROBADA' | 'RECHAZADA';

export interface ItemCotizacion {
  id?: number;
  descripcion: string;
  cantidad: number;
  precioUnitario: number;
  subtotal?: number;
}

export interface CotizacionRequest {
  clienteId: number;
  descripcion: string;
  fechaValidez: string;
  observaciones?: string;
  items: ItemCotizacion[];
}

export interface ClienteInfo {
  id: number;
  nombre: string;
  email: string;
  telefono: string;
  direccion: string;
  ciudad: string;
}

export interface CotizacionResponse {
  id: number;
  codigo: string;
  cliente: ClienteInfo;
  descripcion: string;
  fechaValidez: string;
  observaciones?: string;
  estado: EstadoCotizacion;
  items: ItemCotizacion[];
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

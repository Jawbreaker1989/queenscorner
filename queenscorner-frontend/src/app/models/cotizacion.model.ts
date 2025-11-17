export interface ItemCotizacion {
  id?: number;
  descripcion: string;
  cantidad: number;
  precioUnitario: number;
  subtotal?: number;
  fechaCreacion?: string;
}

export interface CotizacionRequest {
  clienteId: number;
  descripcion: string;
  fechaValidez: string;
  observaciones?: string;
  items: ItemCotizacion[];
}

export interface CotizacionResponse extends CotizacionRequest {
  id: number;
  codigo: string;
  estado: EstadoCotizacion;
  subtotal: number;
  impuestos: number;
  total: number;
  fechaCreacion: string;
  cliente: {
    id: number;
    nombre: string;
    email: string;
  };
}

export enum EstadoCotizacion {
  BORRADOR = 'BORRADOR',
  ENVIADA = 'ENVIADA',
  APROBADA = 'APROBADA',
  RECHAZADA = 'RECHAZADA'
}

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
  status: number;
}

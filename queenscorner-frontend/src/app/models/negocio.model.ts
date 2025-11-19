export type EstadoNegocio = 'EN_REVISION' | 'CANCELADO' | 'FINALIZADO';

export interface NegocioRequest {
  cotizacionId: number;
  descripcion: string;
  observaciones?: string;
  fechaInicio?: string;
  fechaFinEstimada?: string;
  presupuestoAsignado?: number;
  anticipo?: number;
  responsable?: string;
}

export interface NegocioResponse {
  // ===== DATOS DEL NEGOCIO =====
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
  estado: EstadoNegocio;
  fechaCreacion: string;
  fechaActualizacion?: string;

  // ===== DATOS DESNORMALIZADOS DE COTIZACIÓN (READ-ONLY) =====
  codigoCotizacion?: string;
  estadoCotizacion?: string;
  fechaCotizacion?: string;
  fechaValidezCotizacion?: string;
  descripcionCotizacion?: string;
  subtotalCotizacion?: number;
  impuestosCotizacion?: number;
  totalCotizacion?: number;
  observacionesCotizacion?: string;

  // ===== DATOS EDITABLES DEL NEGOCIO =====
  fechaInicio?: string;
  fechaFinEstimada?: string;
  presupuestoAsignado?: number;
  anticipo?: number;
  saldoPendiente?: number; // READ-ONLY: totalCotizacion - anticipo
  responsable?: string;
  descripcion: string;
  observaciones?: string;
  monto?: number;
}

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
  status: number;
}


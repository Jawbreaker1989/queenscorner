export interface Factura {
  id: number;
  negocioId: number;
  numeroFactura: string;
  fechaEmision: Date;
  fechaVencimiento: Date;
  nombreNegocio: string;
  nombreCliente: string;
  rutCliente: string;
  emailCliente: string;
  telefonoCliente: string;
  anticipo: number;
  saldoPendiente: number;
  subtotalItems: number;
  baseGravable: number;
  iva19: number;
  totalAPagar: number;
  medioPago: string;
  referenciaPago: string;
  estado: EstadoFactura;
  pdfGenerado: boolean;
  rutaPdf: string;
  notas: string;
  condicionesPago: string;
  lineas: LineaFactura[];
  cliente?: {
    id?: number;
    nombre?: string;
    documento?: string;
    email?: string;
    telefono?: string;
    direccion?: string;
  };
  negocio?: {
    id?: number;
    numero?: string;
    fechaCreacion?: Date;
    proyecto?: string;
    totalCotizacion?: number;
    anticipo?: number;
    saldoPendiente?: number;
    cliente?: {
      id?: number;
      nombre?: string;
      documento?: string;
      email?: string;
      telefono?: string;
      direccion?: string;
    };
  };
  // Campos del FacturaResponse del backend
  fechaCreacion?: Date;
  fechaEnvio?: Date;
  subtotal?: number;
  iva?: number;
  total?: number;
  observaciones?: string;
  usuarioCreacion?: string;
  usuarioEnvio?: string;
  pathPdf?: string;
}

export interface LineaFactura {
  id: number;
  numeroLinea: number;
  descripcion: string;
  cantidad: number;
  valorUnitario: number;
  total: number;
}

export enum EstadoFactura {
  EN_REVISION = 'EN_REVISION',
  ENVIADA = 'ENVIADA',
  PAGADA = 'PAGADA',
  ANULADA = 'ANULADA'
}

export interface CrearFacturaRequest {
  negocioId: number;
  cotizacionId: number;
  lineas: LineaFacturaRequest[];
}

export interface LineaFacturaRequest {
  itemCotizacionId: number;
  descripcion: string;
  cantidad: number;
  valorUnitario: number;
}

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
  status: number;
}

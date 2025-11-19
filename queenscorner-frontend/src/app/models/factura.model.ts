export interface Factura {
  id: number;
  numeroFactura: string;
  fechaEmision: Date;
  fechaVencimiento: Date;
  nombreNegocio: string;
  nombreCliente: string;
  rutCliente: string;
  emailCliente: string;
  telefonoCliente: string;
  anticipo: number;
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
}

export interface LineaFactura {
  id: number;
  numeroLinea: number;
  descripcion: string;
  cantidad: number;
  valorUnitario: number;
  valorLinea: number;
}

export enum EstadoFactura {
  BORRADOR = 'BORRADOR',
  EMITIDA = 'EMITIDA',
  ENVIADA = 'ENVIADA',
  PAGADA = 'PAGADA',
  ANULADA = 'ANULADA'
}

export interface CrearFacturaRequest {
  negocioId: number;
  cotizacionId: number;
  fechaVencimiento: Date;
  medioPago: string;
  referenciaPago: string;
  notas: string;
  condicionesPago: string;
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

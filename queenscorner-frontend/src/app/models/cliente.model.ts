export interface ClienteRequest {
  nombre: string;
  email: string;
  telefono: string;
  direccion: string;
  ciudad: string;
}

export interface ClienteResponse extends ClienteRequest {
  id: number;
  fechaCreacion: string;
}

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
  status: number;
}

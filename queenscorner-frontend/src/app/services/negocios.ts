import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { NegocioRequest, NegocioResponse, ApiResponse } from '../models/negocio.model';

@Injectable({
  providedIn: 'root'
})
export class NegociosService {
  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  obtenerTodos(): Observable<ApiResponse<NegocioResponse[]>> {
    return this.http.get<ApiResponse<NegocioResponse[]>>(`${this.apiUrl}/negocios`);
  }

  obtenerPorId(id: number): Observable<ApiResponse<NegocioResponse>> {
    return this.http.get<ApiResponse<NegocioResponse>>(`${this.apiUrl}/negocios/${id}`);
  }

  obtenerPorCodigo(codigo: string): Observable<ApiResponse<NegocioResponse>> {
    return this.http.get<ApiResponse<NegocioResponse>>(`${this.apiUrl}/negocios/codigo/${codigo}`);
  }

  crear(negocio: NegocioRequest): Observable<ApiResponse<NegocioResponse>> {
    return this.http.post<ApiResponse<NegocioResponse>>(`${this.apiUrl}/negocios`, negocio);
  }

  actualizar(id: number, negocio: NegocioRequest): Observable<ApiResponse<NegocioResponse>> {
    return this.http.put<ApiResponse<NegocioResponse>>(`${this.apiUrl}/negocios/${id}`, negocio);
  }

  cambiarEstado(id: number, estado: string): Observable<ApiResponse<NegocioResponse>> {
    return this.http.patch<ApiResponse<NegocioResponse>>(`${this.apiUrl}/negocios/${id}/estado`, { estado });
  }

  eliminar(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/negocios/${id}`);
  }

  crearDesdeAprobada(cotizacionId: number, negocio: NegocioRequest): Observable<ApiResponse<NegocioResponse>> {
    return this.http.post<ApiResponse<NegocioResponse>>(`${this.apiUrl}/negocios/desde-cotizacion/${cotizacionId}`, negocio);
  }
}

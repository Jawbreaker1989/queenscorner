import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CotizacionRequest, CotizacionResponse, ApiResponse } from '../models/cotizacion.model';

@Injectable({
  providedIn: 'root'
})
export class CotizacionesService {
  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  obtenerTodas(): Observable<ApiResponse<CotizacionResponse[]>> {
    return this.http.get<ApiResponse<CotizacionResponse[]>>(`${this.apiUrl}/cotizaciones`);
  }

  obtenerPorId(id: number): Observable<ApiResponse<CotizacionResponse>> {
    return this.http.get<ApiResponse<CotizacionResponse>>(`${this.apiUrl}/cotizaciones/${id}`);
  }

  obtenerPorCodigo(codigo: string): Observable<ApiResponse<CotizacionResponse>> {
    return this.http.get<ApiResponse<CotizacionResponse>>(`${this.apiUrl}/cotizaciones/codigo/${codigo}`);
  }

  crear(cotizacion: CotizacionRequest): Observable<ApiResponse<CotizacionResponse>> {
    return this.http.post<ApiResponse<CotizacionResponse>>(`${this.apiUrl}/cotizaciones`, cotizacion);
  }

  actualizar(id: number, cotizacion: CotizacionRequest): Observable<ApiResponse<CotizacionResponse>> {
    return this.http.put<ApiResponse<CotizacionResponse>>(`${this.apiUrl}/cotizaciones/${id}`, cotizacion);
  }

  cambiarEstado(id: number, estado: string): Observable<ApiResponse<CotizacionResponse>> {
    return this.http.patch<ApiResponse<CotizacionResponse>>(`${this.apiUrl}/cotizaciones/${id}/estado`, { estado });
  }

  eliminar(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/cotizaciones/${id}`);
  }

  generarPdf(id: number): Observable<ApiResponse<string>> {
    return this.http.post<ApiResponse<string>>(`${this.apiUrl}/cotizaciones/${id}/pdf`, {});
  }
}

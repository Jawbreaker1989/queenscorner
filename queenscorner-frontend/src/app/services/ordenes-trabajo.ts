import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { OrdenTrabajoRequest, OrdenTrabajoResponse, ApiResponse } from '../models/orden-trabajo.model';

@Injectable({
  providedIn: 'root'
})
export class OrdenesTrabajoService {
  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  obtenerTodas(): Observable<ApiResponse<OrdenTrabajoResponse[]>> {
    return this.http.get<ApiResponse<OrdenTrabajoResponse[]>>(`${this.apiUrl}/ordenes-trabajo`);
  }

  obtenerPorId(id: number): Observable<ApiResponse<OrdenTrabajoResponse>> {
    return this.http.get<ApiResponse<OrdenTrabajoResponse>>(`${this.apiUrl}/ordenes-trabajo/${id}`);
  }

  obtenerPorCodigo(codigo: string): Observable<ApiResponse<OrdenTrabajoResponse>> {
    return this.http.get<ApiResponse<OrdenTrabajoResponse>>(`${this.apiUrl}/ordenes-trabajo/codigo/${codigo}`);
  }

  crear(ordenTrabajo: OrdenTrabajoRequest): Observable<ApiResponse<OrdenTrabajoResponse>> {
    return this.http.post<ApiResponse<OrdenTrabajoResponse>>(`${this.apiUrl}/ordenes-trabajo`, ordenTrabajo);
  }

  actualizar(id: number, ordenTrabajo: OrdenTrabajoRequest): Observable<ApiResponse<OrdenTrabajoResponse>> {
    return this.http.put<ApiResponse<OrdenTrabajoResponse>>(`${this.apiUrl}/ordenes-trabajo/${id}`, ordenTrabajo);
  }

  cambiarEstado(id: number, estado: string): Observable<ApiResponse<OrdenTrabajoResponse>> {
    return this.http.patch<ApiResponse<OrdenTrabajoResponse>>(`${this.apiUrl}/ordenes-trabajo/${id}/estado`, { estado });
  }

  eliminar(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/ordenes-trabajo/${id}`);
  }

  crearDesdeNegocioFinalizado(negocioId: number, ordenTrabajo: OrdenTrabajoRequest): Observable<ApiResponse<OrdenTrabajoResponse>> {
    return this.http.post<ApiResponse<OrdenTrabajoResponse>>(`${this.apiUrl}/ordenes-trabajo/desde-negocio/${negocioId}`, ordenTrabajo);
  }

  generarPdf(id: number): Observable<ApiResponse<string>> {
    return this.http.post<ApiResponse<string>>(`${this.apiUrl}/ordenes-trabajo/${id}/pdf`, {});
  }
}

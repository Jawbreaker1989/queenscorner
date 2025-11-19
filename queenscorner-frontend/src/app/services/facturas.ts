import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Factura, CrearFacturaRequest, ApiResponse } from '../models/factura.model';

@Injectable({
  providedIn: 'root'
})
export class FacturaService {
  private apiUrl = 'http://localhost:8080/api/facturas';

  constructor(private http: HttpClient) { }

  crearFactura(request: CrearFacturaRequest): Observable<ApiResponse<Factura>> {
    return this.http.post<ApiResponse<Factura>>(this.apiUrl, request);
  }

  obtenerFactura(id: number): Observable<ApiResponse<Factura>> {
    return this.http.get<ApiResponse<Factura>>(`${this.apiUrl}/${id}`);
  }

  listarFacturas(): Observable<ApiResponse<Factura[]>> {
    return this.http.get<ApiResponse<Factura[]>>(this.apiUrl);
  }

  listarPorNegocio(negocioId: number): Observable<ApiResponse<Factura[]>> {
    return this.http.get<ApiResponse<Factura[]>>(`${this.apiUrl}/negocio/${negocioId}`);
  }

  emitirFactura(id: number): Observable<ApiResponse<Factura>> {
    return this.http.post<ApiResponse<Factura>>(`${this.apiUrl}/${id}/emitir`, {});
  }

  cambiarEstado(id: number, estado: string): Observable<ApiResponse<Factura>> {
    return this.http.put<ApiResponse<Factura>>(`${this.apiUrl}/${id}/estado/${estado}`, {});
  }

  anularFactura(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}

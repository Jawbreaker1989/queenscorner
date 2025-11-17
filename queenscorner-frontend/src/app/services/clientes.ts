import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ClienteRequest, ClienteResponse, ApiResponse } from '../models/cliente.model';

@Injectable({
  providedIn: 'root'
})
export class Clientes {
  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  obtenerTodos(): Observable<ApiResponse<ClienteResponse[]>> {
    return this.http.get<ApiResponse<ClienteResponse[]>>(`${this.apiUrl}/clientes`);
  }

  obtenerPorId(id: number): Observable<ApiResponse<ClienteResponse>> {
    return this.http.get<ApiResponse<ClienteResponse>>(`${this.apiUrl}/clientes/${id}`);
  }

  crear(cliente: ClienteRequest): Observable<ApiResponse<ClienteResponse>> {
    return this.http.post<ApiResponse<ClienteResponse>>(`${this.apiUrl}/clientes`, cliente);
  }

  actualizar(id: number, cliente: ClienteRequest): Observable<ApiResponse<ClienteResponse>> {
    return this.http.put<ApiResponse<ClienteResponse>>(`${this.apiUrl}/clientes/${id}`, cliente);
  }

  eliminar(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/clientes/${id}`);
  }
}
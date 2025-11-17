import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class Clientes {
  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  getClientes(): Observable<any> {
    return this.http.get(`${this.apiUrl}/clientes`);
  }
}
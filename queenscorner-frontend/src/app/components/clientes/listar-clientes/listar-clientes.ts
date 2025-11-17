import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Clientes } from '../../../services/clientes';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-listar-clientes',
  templateUrl: './listar-clientes.html',
  styleUrls: ['./listar-clientes.css'],
  standalone: true,
  imports: [CommonModule]
})
export class ListarClientesComponent implements OnInit {
  clientes: any[] = [];
  loading = true;
  error = '';

  constructor(
    private clientesService: Clientes,
    private router: Router
  ) {}

  ngOnInit() {
    this.cargarClientes();
  }

  cargarClientes() {
    this.loading = true;
    this.error = '';

    this.clientesService.getClientes().subscribe({
      next: (response: any) => {
        this.clientes = response.data || [];
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Error al cargar clientes. Por favor intenta de nuevo.';
        this.loading = false;
      }
    });
  }

  volver() {
    this.router.navigate(['/dashboard']);
  }
}
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Clientes } from '../../../services/clientes';
import { CommonModule } from '@angular/common';
import { ClienteResponse } from '../../../models/cliente.model';

@Component({
  selector: 'app-listar-clientes',
  templateUrl: './listar-clientes.html',
  styleUrls: ['./listar-clientes.css'],
  standalone: true,
  imports: [CommonModule]
})
export class ListarClientesComponent implements OnInit {
  clientes: ClienteResponse[] = [];
  loading = true;
  error = '';
  mensajeExito = '';

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
    this.mensajeExito = '';

    this.clientesService.obtenerTodos().subscribe({
      next: (response) => {
        if (response.success) {
          this.clientes = response.data || [];
        } else {
          this.error = response.message || 'Error al cargar clientes';
        }
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Error al cargar clientes. Por favor intenta de nuevo.';
        this.loading = false;
      }
    });
  }

  crear() {
    this.router.navigate(['/clientes/crear']);
  }

  editar(id: number) {
    this.router.navigate([`/clientes/editar/${id}`]);
  }

  ver(id: number) {
    this.router.navigate([`/clientes/detalle/${id}`]);
  }

  eliminar(id: number, nombre: string) {
    if (confirm(`¿Estás seguro de que deseas eliminar a ${nombre}?`)) {
      this.clientesService.eliminar(id).subscribe({
        next: (response) => {
          if (response.success) {
            this.mensajeExito = 'Cliente eliminado exitosamente';
            this.cargarClientes();
          } else {
            this.error = response.message || 'Error al eliminar cliente';
          }
        },
        error: (error) => {
          this.error = 'Error al eliminar cliente. Por favor intenta de nuevo.';
        }
      });
    }
  }

  volver() {
    this.router.navigate(['/dashboard']);
  }
}
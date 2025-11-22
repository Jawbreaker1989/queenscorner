import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Clientes } from '../../../services/clientes';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ClienteResponse } from '../../../models/cliente.model';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-listar-clientes',
  templateUrl: './listar-clientes.html',
  styleUrls: ['./listar-clientes.css'],
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class ListarClientesComponent implements OnInit {
  clientes: ClienteResponse[] = [];
  clientesFiltrados: ClienteResponse[] = [];
  loading = true;
  error = '';
  mensajeExito = '';
  searchTerm: string = '';

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
          this.filtrarClientes();
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

  filtrarClientes() {
    if (!this.searchTerm.trim()) {
      this.clientesFiltrados = [...this.clientes];
      return;
    }

    const term = this.searchTerm.toLowerCase().trim();
    this.clientesFiltrados = this.clientes.filter(cliente =>
      cliente.nombre.toLowerCase().includes(term) ||
      cliente.email.toLowerCase().includes(term) ||
      cliente.telefono.toLowerCase().includes(term) ||
      cliente.ciudad.toLowerCase().includes(term)
    );
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

  verDobleClick(id: number) {
    this.ver(id);
  }

  eliminar(id: number, nombre: string) {
    Swal.fire({
      title: '¿Eliminar cliente?',
      text: `Se eliminará permanentemente a ${nombre}`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#dc3545',
      cancelButtonColor: '#6c757d',
      confirmButtonText: 'Eliminar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.clientesService.eliminar(id).subscribe({
          next: (response) => {
            if (response.success) {
              Swal.fire('Eliminado', 'Cliente eliminado exitosamente', 'success');
              this.cargarClientes();
            } else {
              Swal.fire('Error', response.message || 'Error al eliminar cliente', 'error');
            }
          },
          error: (error) => {
            Swal.fire('Error', 'Error al eliminar cliente. Por favor intenta de nuevo.', 'error');
          }
        });
      }
    });
  }

  volver() {
    this.router.navigate(['/dashboard']);
  }
}
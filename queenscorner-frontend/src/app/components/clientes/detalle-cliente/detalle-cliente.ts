import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Clientes } from '../../../services/clientes';
import { CommonModule } from '@angular/common';
import { ClienteResponse } from '../../../models/cliente.model';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-detalle-cliente',
  templateUrl: './detalle-cliente.html',
  styleUrls: ['./detalle-cliente.css'],
  standalone: true,
  imports: [CommonModule]
})
export class DetalleClienteComponent implements OnInit {
  cliente: ClienteResponse | null = null;
  clienteId: number = 0;
  loading = true;
  error = '';

  constructor(
    private clientesService: Clientes,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    this.clienteId = Number(this.route.snapshot.paramMap.get('id'));
    if (!this.clienteId) {
      this.router.navigate(['/clientes']);
      return;
    }
    this.cargarCliente();
  }

  cargarCliente() {
    this.loading = true;
    this.error = '';

    this.clientesService.obtenerPorId(this.clienteId).subscribe({
      next: (response) => {
        if (response.success && response.data) {
          this.cliente = response.data;
        } else {
          this.error = 'No se pudo cargar el cliente';
        }
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Error al cargar cliente. Por favor intenta de nuevo.';
        this.loading = false;
      }
    });
  }

  editar() {
    this.router.navigate([`/clientes/editar/${this.clienteId}`]);
  }

  crearCotizacion() {
    Swal.fire({
      title: 'Crear cotización',
      text: `¿Crear una nueva cotización para ${this.cliente?.nombre}?`,
      icon: 'info',
      showCancelButton: true,
      confirmButtonColor: '#007bff',
      cancelButtonColor: '#6c757d',
      confirmButtonText: 'Crear',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.router.navigate(['/cotizaciones/crear'], { 
          queryParams: { clienteId: this.clienteId } 
        });
      }
    });
  }

  volver() {
    this.router.navigate(['/clientes']);
  }
}

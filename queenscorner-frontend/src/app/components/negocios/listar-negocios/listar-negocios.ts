import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NegociosService } from '../../../services/negocios';
import { CommonModule } from '@angular/common';
import { NegocioResponse, EstadoNegocio } from '../../../models/negocio.model';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-listar-negocios',
  templateUrl: './listar-negocios.html',
  styleUrls: ['./listar-negocios.css'],
  standalone: true,
  imports: [CommonModule]
})
export class ListarNegociosComponent implements OnInit {
  negocios: NegocioResponse[] = [];
  loading = true;
  error = '';

  constructor(
    private negociosService: NegociosService,
    private router: Router
  ) {}

  ngOnInit() {
    this.cargarNegocios();
  }

  cargarNegocios() {
    this.loading = true;
    this.error = '';

    this.negociosService.obtenerTodos().subscribe({
      next: (response) => {
        if (response.success) {
          this.negocios = response.data || [];
        } else {
          this.error = 'No se pudieron cargar los negocios';
        }
        this.loading = false;
      },
      error: () => {
        this.error = 'Error al cargar negocios. Por favor intenta de nuevo.';
        this.loading = false;
      }
    });
  }

  verDetalle(id: number) {
    this.router.navigate(['/negocios/detalle', id]);
  }

  editar(id: number) {
    this.router.navigate(['/negocios/editar', id]);
  }

  eliminar(id: number, codigo: string) {
    Swal.fire({
      title: '¿Ocultar negocio?',
      text: `¿Deseas ocultar el negocio ${codigo} de la lista?`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#dc3545',
      cancelButtonColor: '#6c757d',
      confirmButtonText: 'Sí, ocultar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        // Simplemente eliminar de la lista visual
        const index = this.negocios.findIndex(n => n.id === id);
        if (index > -1) {
          this.negocios.splice(index, 1);
        }
      }
    });
  }

  getEstadoColor(estado: EstadoNegocio): string {
    switch (estado) {
      case 'EN_REVISION':
        return 'badge-info';
      case 'FINALIZADO':
        return 'badge-success';
      case 'CANCELADO':
        return 'badge-danger';
      default:
        return 'badge-secondary';
    }
  }

  volver() {
    this.router.navigate(['/dashboard']);
  }
}

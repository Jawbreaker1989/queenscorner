import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CotizacionesService } from '../../../services/cotizaciones';
import { CommonModule } from '@angular/common';
import { CotizacionResponse, EstadoCotizacion } from '../../../models/cotizacion.model';

@Component({
  selector: 'app-listar-cotizaciones',
  templateUrl: './listar-cotizaciones.html',
  styleUrls: ['./listar-cotizaciones.css'],
  standalone: true,
  imports: [CommonModule]
})
export class ListarCotizacionesComponent implements OnInit {
  cotizaciones: CotizacionResponse[] = [];
  loading = true;
  error = '';
  mensajeExito = '';

  constructor(
    private cotizacionesService: CotizacionesService,
    private router: Router
  ) {}

  ngOnInit() {
    this.cargarCotizaciones();
  }

  cargarCotizaciones() {
    this.loading = true;
    this.error = '';
    this.mensajeExito = '';

    this.cotizacionesService.obtenerTodas().subscribe({
      next: (response) => {
        if (response.success) {
          this.cotizaciones = response.data || [];
        } else {
          this.error = response.message || 'Error al cargar cotizaciones';
        }
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Error al cargar cotizaciones. Por favor intenta de nuevo.';
        this.loading = false;
      }
    });
  }

  crear() {
    this.router.navigate(['/cotizaciones/crear']);
  }

  editar(id: number) {
    this.router.navigate([`/cotizaciones/editar/${id}`]);
  }

  ver(id: number) {
    this.router.navigate([`/cotizaciones/detalle/${id}`]);
  }

  eliminar(id: number, codigo: string) {
    if (confirm(`¿Estás seguro de que deseas eliminar la cotización ${codigo}?`)) {
      this.cotizacionesService.eliminar(id).subscribe({
        next: (response) => {
          if (response.success) {
            this.mensajeExito = 'Cotización eliminada exitosamente';
            this.cargarCotizaciones();
          } else {
            this.error = response.message || 'Error al eliminar cotización';
          }
        },
        error: (error) => {
          this.error = 'Error al eliminar cotización. Por favor intenta de nuevo.';
        }
      });
    }
  }

  cambiarEstado(id: number, nuevoEstado: EstadoCotizacion) {
    this.cotizacionesService.cambiarEstado(id, nuevoEstado).subscribe({
      next: (response) => {
        if (response.success) {
          this.mensajeExito = 'Estado actualizado exitosamente';
          this.cargarCotizaciones();
        } else {
          this.error = response.message || 'Error al cambiar estado';
        }
      },
      error: (error) => {
        this.error = 'Error al cambiar estado. Por favor intenta de nuevo.';
      }
    });
  }

  generarPdf(id: number) {
    this.cotizacionesService.generarPdf(id).subscribe({
      next: (response) => {
        if (response.success) {
          this.mensajeExito = response.message;
        } else {
          this.error = response.message;
        }
      },
      error: (error) => {
        this.error = 'Error al generar PDF. Por favor intenta de nuevo.';
      }
    });
  }

  getEstadoColor(estado: EstadoCotizacion): string {
    switch (estado) {
      case 'BORRADOR': return 'estado-borrador';
      case 'ENVIADA': return 'estado-enviada';
      case 'APROBADA': return 'estado-aprobada';
      case 'RECHAZADA': return 'estado-rechazada';
      default: return '';
    }
  }

  volver() {
    this.router.navigate(['/dashboard']);
  }
}

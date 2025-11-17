import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { CotizacionesService } from '../../../services/cotizaciones';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CotizacionResponse, EstadoCotizacion } from '../../../models/cotizacion.model';

@Component({
  selector: 'app-detalle-cotizacion',
  templateUrl: './detalle-cotizacion.html',
  styleUrls: ['./detalle-cotizacion.css'],
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class DetalleCotizacionComponent implements OnInit {
  cotizacion: CotizacionResponse | null = null;
  cotizacionId: number = 0;
  loading = true;
  error = '';
  nuevoEstado: EstadoCotizacion | null = null;
  cambiandoEstado = false;
  estados: EstadoCotizacion[] = [EstadoCotizacion.ENVIADA, EstadoCotizacion.APROBADA, EstadoCotizacion.RECHAZADA];

  constructor(
    private cotizacionesService: CotizacionesService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    this.cotizacionId = Number(this.route.snapshot.paramMap.get('id'));
    if (!this.cotizacionId) {
      this.router.navigate(['/cotizaciones']);
      return;
    }
    this.cargarCotizacion();
  }

  cargarCotizacion() {
    this.loading = true;
    this.error = '';

    this.cotizacionesService.obtenerPorId(this.cotizacionId).subscribe({
      next: (response) => {
        if (response.success && response.data) {
          this.cotizacion = response.data;
        } else {
          this.error = 'No se pudo cargar la cotización';
        }
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Error al cargar cotización. Por favor intenta de nuevo.';
        this.loading = false;
      }
    });
  }

  cambiarEstado() {
    if (!this.nuevoEstado) return;

    this.cambiandoEstado = true;
    this.cotizacionesService.cambiarEstado(this.cotizacionId, this.nuevoEstado).subscribe({
      next: (response) => {
        if (response.success) {
          this.cotizacion = response.data;
          this.nuevoEstado = null;
        } else {
          this.error = response.message || 'Error al cambiar estado';
        }
        this.cambiandoEstado = false;
      },
      error: () => {
        this.error = 'Error al cambiar estado';
        this.cambiandoEstado = false;
      }
    });
  }

  generarPdf() {
    this.cotizacionesService.generarPdf(this.cotizacionId).subscribe({
      next: (response) => {
        if (response.success) {
          alert(response.message);
        } else {
          this.error = response.message;
        }
      },
      error: () => {
        this.error = 'Error al generar PDF';
      }
    });
  }

  editar() {
    this.router.navigate([`/cotizaciones/editar/${this.cotizacionId}`]);
  }

  volver() {
    this.router.navigate(['/cotizaciones']);
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
}

import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, NavigationEnd } from '@angular/router';
import { CotizacionesService } from '../../../services/cotizaciones';
import { NegociosService } from '../../../services/negocios';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CotizacionResponse, EstadoCotizacion } from '../../../models/cotizacion.model';
import { delay } from 'rxjs/operators';
import Swal from 'sweetalert2';

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
  nuevoEstado: EstadoCotizacion | null = null;
  cambiandoEstado = false;
  estados: EstadoCotizacion[] = ['ENVIADA', 'APROBADA', 'RECHAZADA'];

  loading = false;
  error = '';
  pdfGenerando = false;
  pdfGenerado = false;

  constructor(
    private cotizacionesService: CotizacionesService,
    private negociosService: NegociosService,
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
    
    // Recargar SIEMPRE cuando se retorna a esta vista (desde editar u otra ruta)
    // NavigationEnd detecta cuando la navegación se completa
    this.router.events.subscribe((event: any) => {
      if (event.constructor.name === 'NavigationEnd') {
        // Verificar que seguimos en la misma cotización
        const idActual = Number(this.route.snapshot.paramMap.get('id'));
        if (idActual === this.cotizacionId) {
          this.cargarCotizacion();
        }
      }
    });
  }

  cargarCotizacion() {
    this.loading = true;
    this.error = '';
    this.cotizacionesService.obtenerPorId(this.cotizacionId).subscribe({
      next: (response) => {
        if (response.success && response.data) {
          this.cotizacion = response.data;
          // Verificar si hay PDF generado (guardado en localStorage)
          const pdfKey = `pdf_generado_${this.cotizacionId}`;
          this.pdfGenerado = !!localStorage.getItem(pdfKey);
        } else {
          this.error = 'No se pudo cargar la cotización';
        }
        this.loading = false;
      },
      error: () => {
        this.error = 'Error al cargar cotización';
        this.loading = false;
      }
    });
  }

  generarPdf() {
    if (!this.cotizacion || this.cotizacion.estado !== 'APROBADA' || this.pdfGenerando || this.pdfGenerado) {
      return;
    }

    this.pdfGenerando = true;

    this.cotizacionesService.generarPdf(this.cotizacionId).pipe(
      delay(5000) // Simular 5 segundos de carga
    ).subscribe({
      next: (response) => {
        if (response.success) {
          const pdfKey = `pdf_generado_${this.cotizacionId}`;
          localStorage.setItem(pdfKey, 'true');
          this.pdfGenerado = true;
        }
        this.pdfGenerando = false;
      },
      error: () => {
        this.pdfGenerando = false;
      }
    });
  }

  editar() {
    if (!this.cotizacion) return;
    if (this.cotizacion.estado !== 'BORRADOR' && this.cotizacion.estado !== 'ENVIADA') {
      Swal.fire('No permitido', 'Solo se puede editar en BORRADOR o ENVIADA', 'warning');
      return;
    }
    this.router.navigate(['/cotizaciones/editar', this.cotizacionId]);
  }

  cambiarEstado() {
    if (!this.nuevoEstado) return;

    const mensajeEstado = this.getMensajeEstado(this.nuevoEstado);
    Swal.fire({
      title: 'Cambiar estado',
      text: `¿Cambiar estado a ${this.nuevoEstado}? ${mensajeEstado}`,
      icon: 'info',
      showCancelButton: true,
      confirmButtonColor: '#007bff',
      cancelButtonColor: '#6c757d',
      confirmButtonText: 'Cambiar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed && this.nuevoEstado) {
        this.cambiandoEstado = true;
        this.cotizacionesService.cambiarEstado(this.cotizacionId, this.nuevoEstado).subscribe({
          next: (response) => {
            if (response.success) {
              this.cotizacion = response.data;
              this.nuevoEstado = null;
              Swal.fire('Actualizado', 'Estado actualizado exitosamente', 'success').then(() => {
                this.router.navigate(['/cotizaciones']);
              });
            } else {
              this.error = response.message || 'Error al cambiar estado';
              Swal.fire('Error', this.error, 'error');
            }
            this.cambiandoEstado = false;
          },
          error: () => {
            this.error = 'Error al cambiar estado';
            Swal.fire('Error', 'Error al cambiar estado', 'error');
            this.cambiandoEstado = false;
          }
        });
      }
    });
  }

  getMensajeEstado(estado: EstadoCotizacion): string {
    switch (estado) {
      case 'ENVIADA':
        return 'EL CLIENTE HA SIDO NOTIFICADO';
      case 'APROBADA':
        return 'LISTA PARA EJECUTAR NEGOCIO';
      case 'RECHAZADA':
        return 'EL CLIENTE NO ESTA DEACUERDO CON LO ESTIPULADO';
      default:
        return '';
    }
  }

  getEstadoColor(estado: EstadoCotizacion): string {
    switch (estado) {
      case 'BORRADOR':
        return 'badge-secondary';
      case 'ENVIADA':
        return 'badge-info';
      case 'APROBADA':
        return 'badge-success';
      case 'RECHAZADA':
        return 'badge-danger';
      default:
        return 'badge-secondary';
    }
  }

  volver() {
    this.router.navigate(['/cotizaciones']);
  }

  crearNegocio() {
    if (!this.cotizacion) return;
    
    if (this.cotizacion.estado !== 'APROBADA') {
      Swal.fire('No permitido', 'Solo se pueden crear negocios desde cotizaciones APROBADAS', 'warning');
      return;
    }

    Swal.fire({
      title: 'Crear Negocio',
      text: `¿Crear un negocio para la cotización ${this.cotizacion.codigo}?`,
      icon: 'info',
      showCancelButton: true,
      confirmButtonColor: '#28a745',
      cancelButtonColor: '#6c757d',
      confirmButtonText: 'Crear',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.loading = true;
        const negocioRequest = {
          cotizacionId: this.cotizacion!.id,
          descripcion: this.cotizacion!.descripcion,
          observaciones: `Negocio creado desde cotización ${this.cotizacion!.codigo}`
        };

        this.negociosService.crearDesdeAprobada(this.cotizacion!.id, negocioRequest).subscribe({
          next: (response: any) => {
            if (response.success) {
              Swal.fire('Éxito', 'Negocio creado exitosamente', 'success').then(() => {
                // Handle both ApiResponse<Negocio> and direct NegocioResponse
                const negocioId = response.data?.id || response.id;
                if (negocioId) {
                  this.router.navigate(['/negocios/detalle', negocioId]);
                } else {
                  Swal.fire('Error', 'No se pudo obtener el ID del negocio', 'error');
                  this.loading = false;
                }
              });
            } else {
              Swal.fire('Error', response.message || 'Error al crear negocio', 'error');
              this.loading = false;
            }
          },
          error: (error: any) => {
            console.error('Error al crear negocio:', error);
            Swal.fire('Error', error.error?.message || 'Error al crear negocio. Por favor intenta de nuevo.', 'error');
            this.loading = false;
          }
        });
      }
    });
  }
}

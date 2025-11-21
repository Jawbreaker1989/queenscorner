import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, NavigationEnd } from '@angular/router';
import { CotizacionesService } from '../../../services/cotizaciones';
import { NegociosService } from '../../../services/negocios';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CotizacionResponse, EstadoCotizacion } from '../../../models/cotizacion.model';
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
    if (!this.cotizacion || this.cotizacion.estado !== 'APROBADA') {
      Swal.fire('No permitido', 'Solo se puede generar PDF para cotizaciones APROBADAS', 'warning');
      return;
    }

    this.pdfGenerando = true;
    
    // Mostrar modal de loading asincrónico
    Swal.fire({
      title: 'Generando PDF',
      html: `
        <div style="display: flex; flex-direction: column; align-items: center; gap: 20px;">
          <div style="
            width: 60px;
            height: 60px;
            border: 4px solid #f3f3f3;
            border-top: 4px solid #667eea;
            border-radius: 50%;
            animation: spin-pdf 1s linear infinite;
          "></div>
          <p style="margin: 0; color: #666; font-size: 16px;">Por favor espera, tu PDF se está generando...</p>
        </div>
      `,
      allowOutsideClick: false,
      allowEscapeKey: false,
      didOpen: () => {
        Swal.showLoading();
      }
    });

    this.cotizacionesService.generarPdf(this.cotizacionId).subscribe({
      next: (response) => {
        if (response.success) {
          // Marcar como generado en localStorage
          const pdfKey = `pdf_generado_${this.cotizacionId}`;
          localStorage.setItem(pdfKey, 'true');
          this.pdfGenerado = true;

          Swal.fire({
            title: '¡Éxito!',
            html: `
              <div style="display: flex; flex-direction: column; align-items: center; gap: 15px;">
                <div style="font-size: 48px;">✅</div>
                <p style="margin: 0; color: #666; font-size: 16px;">PDF generado exitosamente.<br/>El proceso se está completando en segundo plano.</p>
              </div>
            `,
            icon: 'success',
            confirmButtonColor: '#667eea'
          });
        } else {
          Swal.fire('Error', response.message || 'Error al generar PDF', 'error');
        }
        this.pdfGenerando = false;
      },
      error: () => {
        Swal.fire('Error', 'Error al generar PDF', 'error');
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
        return '📱 Se enviará notificación al cliente por SMS/WhatsApp';
      case 'APROBADA':
        return '✅ Se generará un PDF de aprobación';
      case 'RECHAZADA':
        return '❌ Se rechazará la cotización';
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
          next: (response) => {
            if (response.success) {
              Swal.fire('Éxito', 'Negocio creado exitosamente', 'success').then(() => {
                this.router.navigate(['/negocios/detalle', response.data.id]);
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

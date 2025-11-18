import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { NegociosService } from '../../../services/negocios';
import { CotizacionesService } from '../../../services/cotizaciones';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NegocioRequest, NegocioResponse } from '../../../models/negocio.model';
import { CotizacionResponse } from '../../../models/cotizacion.model';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-crear-negocio',
  templateUrl: './crear-negocio.html',
  styleUrls: ['./crear-negocio.css'],
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class CrearNegocioComponent implements OnInit {
  cotizacionId: number = 0;
  cotizacion: CotizacionResponse | null = null;
  negocio: NegocioRequest = {
    cotizacionId: 0,
    descripcion: '',
    observaciones: '',
    fechaInicio: new Date().toISOString().split('T')[0],
    fechaFinEstimada: new Date(Date.now() + 30 * 24 * 60 * 60 * 1000).toISOString().split('T')[0],
    anticipo: 0
  };

  loading = false;
  cargando = true;
  error = '';

  constructor(
    private negociosService: NegociosService,
    private cotizacionesService: CotizacionesService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    this.cotizacionId = Number(this.route.snapshot.queryParams['cotizacionId']);
    
    if (this.cotizacionId) {
      this.cargarCotizacion();
    } else {
      this.cargando = false;
    }
  }

  cargarCotizacion() {
    this.cotizacionesService.obtenerPorId(this.cotizacionId).subscribe({
      next: (response) => {
        if (response.success && response.data) {
          // Validar que la cotización esté aprobada
          if (response.data.estado !== 'APROBADA') {
            Swal.fire('No permitido', 'Solo se puede crear negocio desde cotización APROBADA', 'warning').then(() => {
              this.router.navigate(['/cotizaciones']);
            });
            return;
          }

          this.cotizacion = response.data;
          this.negocio.cotizacionId = this.cotizacionId;
          
          // Usar descripción de cotización como default
          if (!this.negocio.descripcion) {
            this.negocio.descripcion = `Negocio para: ${response.data.descripcion}`;
          }
        } else {
          this.error = 'No se pudo cargar la cotización';
        }
        this.cargando = false;
      },
      error: () => {
        this.error = 'Error al cargar cotización';
        this.cargando = false;
      }
    });
  }

  guardar() {
    if (!this.validar()) return;

    this.loading = true;
    this.error = '';

    this.negociosService.crearDesdeAprobada(this.cotizacionId, this.negocio).subscribe({
      next: (response) => {
        if (response.success) {
          Swal.fire('Éxito', 'Negocio creado exitosamente desde cotización aprobada', 'success').then(() => {
            this.router.navigate(['/negocios/detalle', response.data.id]);
          });
        } else {
          this.error = response.message || 'Error al crear negocio';
          Swal.fire('Error', this.error, 'error');
          this.loading = false;
        }
      },
      error: () => {
        this.error = 'Error al crear negocio. Por favor intenta de nuevo.';
        Swal.fire('Error', this.error, 'error');
        this.loading = false;
      }
    });
  }

  validar(): boolean {
    if (!this.negocio.cotizacionId) {
      this.error = 'Cotización es requerida';
      return false;
    }
    if (!this.negocio.descripcion.trim()) {
      this.error = 'Descripción requerida';
      return false;
    }
    if (!this.negocio.fechaInicio) {
      this.error = 'Fecha inicio requerida';
      return false;
    }
    if (!this.negocio.fechaFinEstimada) {
      this.error = 'Fecha fin estimada requerida';
      return false;
    }
    const inicio = new Date(this.negocio.fechaInicio);
    const fin = new Date(this.negocio.fechaFinEstimada);
    if (fin <= inicio) {
      this.error = 'Fecha fin debe ser posterior a fecha inicio';
      return false;
    }
    
    // Validar presupuesto asignado si se especificó
    if (this.negocio.presupuestoAsignado && this.cotizacion) {
      const maxPresupuesto = this.cotizacion.total * 0.75;
      if (this.negocio.presupuestoAsignado > maxPresupuesto) {
        this.error = `Presupuesto asignado no puede exceder $${maxPresupuesto.toFixed(2)} (75% del total de $${this.cotizacion.total.toFixed(2)})`;
        return false;
      }
    }
    
    return true;
  }

  cancelar() {
    this.router.navigate(['/negocios']);
  }
}

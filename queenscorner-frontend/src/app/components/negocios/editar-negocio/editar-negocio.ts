import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import Swal from 'sweetalert2';
import { NegociosService } from '../../../services/negocios';
import { NegocioResponse, NegocioRequest } from '../../../models/negocio.model';

@Component({
  selector: 'app-editar-negocio',
  templateUrl: './editar-negocio.html',
  styleUrls: ['./editar-negocio.css'],
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class EditarNegocioComponent implements OnInit {
  negocio: NegocioResponse | null = null;
  negocioForm: NegocioRequest = {
    cotizacionId: 0,
    descripcion: '',
    observaciones: '',
    fechaInicio: '',
    fechaFinEstimada: '',
    presupuestoAsignado: 0,
    responsable: ''
  };
  cargando: boolean = true;
  procesando: boolean = false;

  constructor(
    private negociosService: NegociosService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.cargarNegocio();
  }

  cargarNegocio(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (!id) {
      Swal.fire('Error', 'ID de negocio no válido', 'error');
      this.router.navigate(['/negocios']);
      return;
    }

    this.negociosService.obtenerPorId(Number(id)).subscribe({
      next: (response: any) => {
        this.negocio = response.data;
        this.negocioForm = {
          cotizacionId: this.negocio?.cotizacionId || 0,
          descripcion: this.negocio?.descripcion || '',
          observaciones: this.negocio?.observaciones || '',
          fechaInicio: this.negocio?.fechaInicio || '',
          fechaFinEstimada: this.negocio?.fechaFinEstimada || '',
          presupuestoAsignado: this.negocio?.presupuestoAsignado || 0,
          responsable: this.negocio?.responsable || ''
        };
        this.cargando = false;

        // Validar que el negocio pueda ser editado
        if (this.negocio?.estado !== 'EN_REVISION') {
          Swal.fire('Información', `No se puede editar un negocio en estado ${this.negocio?.estado}`, 'info');
          this.router.navigate(['/negocios/detalle', id]);
        }
      },
      error: (error: any) => {
        console.error('Error al cargar negocio:', error);
        Swal.fire('Error', 'No se pudo cargar el negocio', 'error');
        this.cargando = false;
        this.router.navigate(['/negocios']);
      }
    });
  }

  formatearFecha(fecha?: string): string {
    if (!fecha) return '-';
    try {
      const d = new Date(fecha);
      return d.toLocaleDateString('es-CO');
    } catch {
      return fecha;
    }
  }

  validar(): boolean {
    if (!this.negocioForm.descripcion.trim()) {
      Swal.fire('Error', 'La descripción es requerida', 'error');
      return false;
    }
    if (!this.negocioForm.fechaInicio) {
      Swal.fire('Error', 'Fecha inicio es requerida', 'error');
      return false;
    }
    if (!this.negocioForm.fechaFinEstimada) {
      Swal.fire('Error', 'Fecha fin estimada es requerida', 'error');
      return false;
    }
    const inicio = new Date(this.negocioForm.fechaInicio);
    const fin = new Date(this.negocioForm.fechaFinEstimada);
    if (fin <= inicio) {
      Swal.fire('Error', 'Fecha fin debe ser posterior a fecha inicio', 'error');
      return false;
    }
    return true;
  }

  guardar(): void {
    if (!this.validar() || !this.negocio) {
      return;
    }

    Swal.fire({
      title: '¿Guardar cambios?',
      text: `Se actualizarán los datos del negocio ${this.negocio.codigo}`,
      icon: 'question',
      showCancelButton: true,
      confirmButtonText: 'Sí, guardar',
      cancelButtonText: 'Cancelar'
    }).then(result => {
      if (result.isConfirmed) {
        this.procesando = true;
        this.negociosService.actualizar(this.negocio!.id, this.negocioForm).subscribe({
          next: () => {
            this.procesando = false;
            Swal.fire('Éxito', 'Negocio actualizado correctamente', 'success');
            this.router.navigate(['/negocios/detalle', this.negocio?.id]);
          },
          error: (error: any) => {
            this.procesando = false;
            console.error('Error al actualizar negocio:', error);
            Swal.fire('Error', 'No se pudo actualizar el negocio', 'error');
          }
        });
      }
    });
  }

  cancelar(): void {
    this.router.navigate(['/negocios/detalle', this.negocio?.id]);
  }
}

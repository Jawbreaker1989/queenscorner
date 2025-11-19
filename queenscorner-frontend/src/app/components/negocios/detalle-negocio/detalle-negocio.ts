import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import Swal from 'sweetalert2';
import { NegociosService } from '../../../services/negocios';
import { NegocioResponse } from '../../../models/negocio.model';

@Component({
  selector: 'app-detalle-negocio',
  templateUrl: './detalle-negocio.html',
  styleUrls: ['./detalle-negocio.css'],
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class DetalleNegocioComponent implements OnInit {
  negocio: NegocioResponse | null = null;
  cargando: boolean = true;
  nuevoEstado: string = '';
  mostrarCambioEstado: boolean = false;
  procesando: boolean = false;

  constructor(
    private negociosService: NegociosService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    // Suscribirse a cambios en queryParams para detectar refresco
    this.route.queryParams.subscribe(() => {
      this.cargarNegocio();
    });
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
        this.nuevoEstado = this.negocio?.estado || '';
        this.cargando = false;
      },
      error: (error: any) => {
        console.error('Error al cargar negocio:', error);
        Swal.fire('Error', 'No se pudo cargar el negocio', 'error');
        this.cargando = false;
        this.router.navigate(['/negocios']);
      }
    });
  }

  getEstadoColor(estado: string): string {
    const colores: { [key: string]: string } = {
      'EN_REVISION': '#3498db',
      'CANCELADO': '#e74c3c',
      'FINALIZADO': '#27ae60'
    };
    return colores[estado] || '#95a5a6';
  }

  getEstadoTextColor(estado: string): string {
    return '#fff';
  }

  toggleCambioEstado(): void {
    this.mostrarCambioEstado = !this.mostrarCambioEstado;
    if (!this.mostrarCambioEstado) {
      this.nuevoEstado = this.negocio?.estado || '';
    }
  }

  obtenerTransicionesValidas(): string[] {
    const estado = this.negocio?.estado;
    const transiciones: { [key: string]: string[] } = {
      'EN_REVISION': ['FINALIZADO', 'CANCELADO'],
      'FINALIZADO': [],
      'CANCELADO': []
    };
    return transiciones[estado || ''] || [];
  }

  guardarCambioEstado(): void {
    if (!this.negocio || !this.nuevoEstado) {
      Swal.fire('Error', 'Seleccione un estado válido', 'error');
      return;
    }

    if (this.nuevoEstado === this.negocio.estado) {
      Swal.fire('Información', 'El estado es el mismo', 'info');
      this.mostrarCambioEstado = false;
      return;
    }

    Swal.fire({
      title: '¿Confirmar cambio de estado?',
      text: `¿Desea cambiar el estado de ${this.negocio.codigo} a ${this.nuevoEstado}?`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Sí, cambiar',
      cancelButtonText: 'Cancelar'
    }).then(result => {
      if (result.isConfirmed) {
        this.procesando = true;
        this.negociosService.cambiarEstado(this.negocio!.id, this.nuevoEstado).subscribe({
          next: () => {
            this.procesando = false;
            Swal.fire('Éxito', 'Estado actualizado correctamente', 'success');
            this.mostrarCambioEstado = false;
            this.cargarNegocio();
          },
          error: (error: any) => {
            this.procesando = false;
            console.error('Error al cambiar estado:', error);
            Swal.fire('Error', 'No se pudo cambiar el estado', 'error');
          }
        });
      }
    });
  }

  editar(): void {
    this.router.navigate(['/negocios/editar', this.negocio?.id]);
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

  volver(): void {
    this.router.navigate(['/negocios']);
  }
}

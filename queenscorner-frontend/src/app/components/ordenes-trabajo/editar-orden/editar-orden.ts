import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import Swal from 'sweetalert2';
import { OrdenesTrabajoService } from '../../../services/ordenes-trabajo';
import { OrdenTrabajoResponse, OrdenTrabajoRequest, DetalleOrdenTrabajo } from '../../../models/orden-trabajo.model';

@Component({
  selector: 'app-editar-orden',
  templateUrl: './editar-orden.html',
  styleUrls: ['./editar-orden.css'],
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class EditarOrdenComponent implements OnInit {
  orden: OrdenTrabajoResponse | null = null;
  ordenForm: OrdenTrabajoRequest = {
    negocioId: 0,
    descripcion: '',
    fechaInicio: '',
    fechaEstimadaFin: '',
    detalles: [],
    observaciones: ''
  };
  detalles: DetalleOrdenTrabajo[] = [];
  nuevoDetalle: DetalleOrdenTrabajo = {
    descripcion: '',
    cantidad: 1,
    precioUnitario: 0
  };
  cargando: boolean = true;
  procesando: boolean = false;
  mostrarFormDetalle: boolean = false;
  subtotal: number = 0;
  impuestos: number = 0;
  total: number = 0;

  constructor(
    private ordenesService: OrdenesTrabajoService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.cargarOrden();
  }

  cargarOrden(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (!id) {
      Swal.fire('Error', 'ID de orden no válido', 'error');
      this.router.navigate(['/ordenes-trabajo']);
      return;
    }

    this.ordenesService.obtenerPorId(Number(id)).subscribe({
      next: (response: any) => {
        this.orden = response.data;
        this.ordenForm = {
          negocioId: this.orden?.negocioId || 0,
          descripcion: this.orden?.descripcion || '',
          fechaInicio: this.orden?.fechaInicio || '',
          fechaEstimadaFin: this.orden?.fechaEstimadaFin || '',
          detalles: [],
          observaciones: this.orden?.observaciones || ''
        };
        this.detalles = (this.orden?.detalles || []).map(d => ({
          id: d.id,
          descripcion: d.descripcion,
          cantidad: d.cantidad,
          precioUnitario: d.precioUnitario,
          subtotal: d.subtotal
        }));
        this.calcularTotales();
        this.cargando = false;

        // Validar que la orden pueda ser editada
        if (this.orden?.estado !== 'PENDIENTE' && this.orden?.estado !== 'EN_PROGRESO') {
          Swal.fire('Información', `No se puede editar una orden en estado ${this.orden?.estado}`, 'info');
          this.router.navigate(['/ordenes-trabajo/detalle', id]);
        }
      },
      error: (error: any) => {
        console.error('Error al cargar orden:', error);
        Swal.fire('Error', 'No se pudo cargar la orden', 'error');
        this.cargando = false;
        this.router.navigate(['/ordenes-trabajo']);
      }
    });
  }

  toggleFormDetalle(): void {
    this.mostrarFormDetalle = !this.mostrarFormDetalle;
    if (!this.mostrarFormDetalle) {
      this.nuevoDetalle = {
        descripcion: '',
        cantidad: 1,
        precioUnitario: 0
      };
    }
  }

  agregarDetalle(): void {
    if (!this.nuevoDetalle.descripcion.trim()) {
      Swal.fire('Error', 'La descripción del detalle es requerida', 'error');
      return;
    }

    if (this.nuevoDetalle.cantidad <= 0) {
      Swal.fire('Error', 'La cantidad debe ser mayor a 0', 'error');
      return;
    }

    if (this.nuevoDetalle.precioUnitario < 0) {
      Swal.fire('Error', 'El precio no puede ser negativo', 'error');
      return;
    }

    const detalle: DetalleOrdenTrabajo = {
      id: undefined,
      descripcion: this.nuevoDetalle.descripcion,
      cantidad: this.nuevoDetalle.cantidad,
      precioUnitario: this.nuevoDetalle.precioUnitario,
      subtotal: this.nuevoDetalle.cantidad * this.nuevoDetalle.precioUnitario
    };

    this.detalles.push(detalle);
    this.calcularTotales();
    this.nuevoDetalle = {
      descripcion: '',
      cantidad: 1,
      precioUnitario: 0
    };
    this.mostrarFormDetalle = false;
  }

  eliminarDetalle(index: number): void {
    this.detalles.splice(index, 1);
    this.calcularTotales();
  }

  calcularTotales(): void {
    this.subtotal = this.detalles.reduce((sum, d) => sum + (d.subtotal || 0), 0);
    this.impuestos = this.subtotal * 0.19; // IVA 19%
    this.total = this.subtotal + this.impuestos;
  }

  validar(): boolean {
    if (!this.ordenForm.descripcion.trim()) {
      Swal.fire('Error', 'La descripción es requerida', 'error');
      return false;
    }

    if (!this.ordenForm.fechaInicio) {
      Swal.fire('Error', 'La fecha de inicio es requerida', 'error');
      return false;
    }

    if (this.detalles.length === 0) {
      Swal.fire('Error', 'Debe haber al menos un detalle en la orden', 'error');
      return false;
    }

    return true;
  }

  guardar(): void {
    if (!this.validar() || !this.orden) {
      return;
    }

    this.ordenForm.detalles = this.detalles;

    Swal.fire({
      title: '¿Guardar cambios?',
      text: `Se actualizarán los datos de la orden ${this.orden.codigo}`,
      icon: 'question',
      showCancelButton: true,
      confirmButtonText: 'Sí, guardar',
      cancelButtonText: 'Cancelar'
    }).then(result => {
      if (result.isConfirmed) {
        this.procesando = true;
        this.ordenesService.actualizar(this.orden!.id, this.ordenForm).subscribe({
          next: () => {
            this.procesando = false;
            Swal.fire('Éxito', 'Orden actualizada correctamente', 'success');
            this.router.navigate(['/ordenes-trabajo/detalle', this.orden?.id]);
          },
          error: (error: any) => {
            this.procesando = false;
            console.error('Error al actualizar orden:', error);
            Swal.fire('Error', 'No se pudo actualizar la orden', 'error');
          }
        });
      }
    });
  }

  cancelar(): void {
    this.router.navigate(['/ordenes-trabajo/detalle', this.orden?.id]);
  }
}

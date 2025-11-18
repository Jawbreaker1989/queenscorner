import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import Swal from 'sweetalert2';
import { OrdenesTrabajoService } from '../../../services/ordenes-trabajo';
import { NegociosService } from '../../../services/negocios';
import { NegocioResponse } from '../../../models/negocio.model';
import { DetalleOrdenTrabajo, OrdenTrabajoRequest } from '../../../models/orden-trabajo.model';

@Component({
  selector: 'app-crear-orden',
  templateUrl: './crear-orden.html',
  styleUrls: ['./crear-orden.css'],
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class CrearOrdenComponent implements OnInit {
  negocio: NegocioResponse | null = null;
  ordenForm: OrdenTrabajoRequest = {
    negocioId: 0,
    descripcion: '',
    fechaInicio: new Date().toISOString().split('T')[0],
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
    private negociosService: NegociosService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.cargarNegocio();
  }

  cargarNegocio(): void {
    const negocioId = this.route.snapshot.queryParamMap.get('negocioId');
    if (!negocioId) {
      Swal.fire('Error', 'ID de negocio no válido', 'error');
      this.router.navigate(['/ordenes-trabajo']);
      return;
    }

    this.negociosService.obtenerPorId(Number(negocioId)).subscribe({
      next: (response: any) => {
        this.negocio = response.data;

        // Validar que el negocio está finalizado
        if (this.negocio?.estado !== 'FINALIZADO') {
          Swal.fire('Error', 'Solo se pueden crear órdenes desde negocios finalizados', 'error');
          this.router.navigate(['/negocios']);
          return;
        }

        this.ordenForm.negocioId = Number(negocioId);
        this.ordenForm.descripcion = this.negocio?.descripcion || '';
        this.cargando = false;
      },
      error: (error: any) => {
        console.error('Error al cargar negocio:', error);
        Swal.fire('Error', 'No se pudo cargar el negocio', 'error');
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
      Swal.fire('Error', 'Debe agregar al menos un detalle a la orden', 'error');
      return false;
    }

    return true;
  }

  guardar(): void {
    if (!this.validar()) {
      return;
    }

    this.ordenForm.detalles = this.detalles;

    Swal.fire({
      title: '¿Crear orden de trabajo?',
      text: `Se creará una nueva orden de trabajo asociada al negocio ${this.negocio?.codigo}`,
      icon: 'question',
      showCancelButton: true,
      confirmButtonText: 'Sí, crear',
      cancelButtonText: 'Cancelar'
    }).then(result => {
      if (result.isConfirmed) {
        this.procesando = true;
        this.ordenesService.crearDesdeNegocioFinalizado(
          this.negocio!.id,
          this.ordenForm
        ).subscribe({
          next: (response: any) => {
            this.procesando = false;
            Swal.fire('Éxito', 'Orden de trabajo creada correctamente', 'success');
            this.router.navigate(['/ordenes-trabajo/detalle', response.data.id]);
          },
          error: (error: any) => {
            this.procesando = false;
            console.error('Error al crear orden:', error);
            Swal.fire('Error', 'No se pudo crear la orden de trabajo', 'error');
          }
        });
      }
    });
  }

  cancelar(): void {
    this.router.navigate(['/ordenes-trabajo']);
  }
}

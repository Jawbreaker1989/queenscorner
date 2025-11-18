import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { CotizacionesService } from '../../../services/cotizaciones';
import { Clientes } from '../../../services/clientes';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CotizacionRequest, CotizacionResponse, ItemCotizacion, EstadoCotizacion } from '../../../models/cotizacion.model';
import { ClienteResponse } from '../../../models/cliente.model';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-editar-cotizacion',
  templateUrl: './editar-cotizacion.html',
  styleUrls: ['./cotizacion-form.css'],
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class EditarCotizacionComponent implements OnInit {
  clientes: ClienteResponse[] = [];
  cotizacion: CotizacionRequest = {
    clienteId: 0,
    descripcion: '',
    fechaValidez: '',
    observaciones: '',
    items: []
  };
  cotizacionOriginal: CotizacionResponse | null = null;
  cotizacionId: number = 0;
  nuevoItem: ItemCotizacion = {
    descripcion: '',
    cantidad: 1,
    precioUnitario: 0
  };
  estadoActual: EstadoCotizacion | null = null;
  nuevoEstado: EstadoCotizacion | null = null;

  loading = false;
  cargando = true;
  cargandoClientes = true;
  error = '';
  estados: EstadoCotizacion[] = ['ENVIADA', 'APROBADA', 'RECHAZADA'];

  constructor(
    private cotizacionesService: CotizacionesService,
    private clientesService: Clientes,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    this.cotizacionId = Number(this.route.snapshot.paramMap.get('id'));
    if (!this.cotizacionId) {
      this.router.navigate(['/cotizaciones']);
      return;
    }
    this.cargarDatos();
  }

  cargarDatos() {
    this.cotizacionesService.obtenerPorId(this.cotizacionId).subscribe({
      next: (response) => {
        if (response.success && response.data) {
          this.cotizacionOriginal = response.data;
          
          // Si está APROBADA o RECHAZADA, no permitir editar
          if (response.data.estado === 'APROBADA' || response.data.estado === 'RECHAZADA') {
            Swal.fire('No permitido', 'No se puede editar cotizaciones en estado ' + response.data.estado, 'warning').then(() => {
              this.router.navigate(['/cotizaciones/detalle', this.cotizacionId]);
            });
            return;
          }

          // MAPEAR ITEMS CON SUS IDs
          const itemsConId = (response.data.items || []).map(item => ({
            id: item.id,
            descripcion: item.descripcion,
            cantidad: item.cantidad,
            precioUnitario: item.precioUnitario,
            subtotal: item.subtotal
          }));

          this.estadoActual = response.data.estado;
          this.cotizacion = {
            clienteId: response.data.cliente.id,
            descripcion: response.data.descripcion,
            fechaValidez: response.data.fechaValidez,
            observaciones: response.data.observaciones || '',
            items: itemsConId
          };
        } else {
          this.error = 'No se pudo cargar la cotización';
        }
        this.cargando = false;
        this.cargarClientes();
      },
      error: () => {
        this.error = 'Error al cargar cotización';
        this.cargando = false;
      }
    });
  }

  cargarClientes() {
    this.clientesService.obtenerTodos().subscribe({
      next: (response) => {
        if (response.success) {
          this.clientes = response.data || [];
        }
        this.cargandoClientes = false;
      },
      error: () => {
        this.error = 'Error al cargar clientes';
        this.cargandoClientes = false;
      }
    });
  }

  agregarItem() {
    if (!this.nuevoItem.descripcion.trim()) {
      this.error = 'Descripción del item requerida';
      return;
    }
    if (this.nuevoItem.cantidad <= 0) {
      this.error = 'Cantidad debe ser mayor a 0';
      return;
    }
    if (this.nuevoItem.precioUnitario <= 0) {
      this.error = 'Precio unitario debe ser mayor a 0';
      return;
    }

    // NUEVO ITEM SIN ID (para que el backend lo identifique como nuevo)
    this.cotizacion.items.push({
      descripcion: this.nuevoItem.descripcion,
      cantidad: this.nuevoItem.cantidad,
      precioUnitario: this.nuevoItem.precioUnitario,
      subtotal: this.nuevoItem.cantidad * this.nuevoItem.precioUnitario
      // NO incluir 'id' - undefined indica que es nuevo
    });

    this.nuevoItem = {
      descripcion: '',
      cantidad: 1,
      precioUnitario: 0
    };
    this.error = '';
  }

  eliminarItem(index: number) {
    this.cotizacion.items.splice(index, 1);
  }

  getTotal(): number {
    return this.cotizacion.items.reduce((sum: number, item: ItemCotizacion) => sum + (item.subtotal || 0), 0);
  }

  guardar() {
    if (!this.validar()) return;

    this.loading = true;
    this.error = '';

    // Primero actualizar la cotización
    this.cotizacionesService.actualizar(this.cotizacionId, this.cotizacion).subscribe({
      next: (response) => {
        if (response.success) {
          this.cotizacionOriginal = response.data;
          this.estadoActual = response.data.estado;
          
          // Si se cambió el estado, aplicarlo después
          if (this.nuevoEstado && this.nuevoEstado !== this.estadoActual) {
            this.aplicarCambioEstado();
          } else {
            Swal.fire('Éxito', 'Cotización actualizada exitosamente', 'success').then(() => {
              this.router.navigate(['/cotizaciones']);
            });
            this.loading = false;
            this.nuevoEstado = null;
          }
        } else {
          this.error = response.message || 'Error al actualizar cotización';
          Swal.fire('Error', this.error, 'error');
          this.loading = false;
        }
      },
      error: (error) => {
        this.error = 'Error al actualizar cotización. Por favor intenta de nuevo.';
        Swal.fire('Error', this.error, 'error');
        this.loading = false;
      }
    });
  }

  private aplicarCambioEstado() {
    if (!this.nuevoEstado) {
      this.loading = false;
      return;
    }

    this.cotizacionesService.cambiarEstado(this.cotizacionId, this.nuevoEstado).subscribe({
      next: (response) => {
        if (response.success) {
          this.cotizacionOriginal = response.data;
          this.estadoActual = response.data.estado;
          this.nuevoEstado = null;
          Swal.fire('Éxito', 'Cotización y estado actualizados exitosamente', 'success').then(() => {
            this.router.navigate(['/cotizaciones']);
          });
          this.loading = false;
        } else {
          this.error = response.message || 'Error al cambiar estado';
          Swal.fire('Advertencia', 'Cotización guardada pero no se pudo cambiar estado: ' + this.error, 'warning').then(() => {
            this.router.navigate(['/cotizaciones']);
          });
          this.loading = false;
        }
      },
      error: () => {
        this.error = 'Error al cambiar estado';
        Swal.fire('Advertencia', 'Cotización guardada pero no se pudo cambiar estado', 'warning').then(() => {
          this.router.navigate(['/cotizaciones']);
        });
        this.loading = false;
      }
    });
  }

  validar(): boolean {
    if (!this.cotizacion.clienteId) {
      this.error = 'Selecciona un cliente';
      return false;
    }
    if (!this.cotizacion.descripcion.trim()) {
      this.error = 'Descripción requerida';
      return false;
    }
    if (!this.cotizacion.fechaValidez) {
      this.error = 'Fecha de validez requerida';
      return false;
    }
    if (this.cotizacion.items.length === 0) {
      this.error = 'Agrega al menos un item';
      return false;
    }
    return true;
  }

  cancelar() {
    this.router.navigate(['/cotizaciones']);
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
}

import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { CotizacionesService } from '../../../services/cotizaciones';
import { Clientes } from '../../../services/clientes';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CotizacionRequest, CotizacionResponse, ItemCotizacion } from '../../../models/cotizacion.model';
import { ClienteResponse } from '../../../models/cliente.model';

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

  loading = false;
  cargando = true;
  cargandoClientes = true;
  error = '';

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
          this.cotizacion = {
            clienteId: response.data.cliente.id,
            descripcion: response.data.descripcion,
            fechaValidez: response.data.fechaValidez,
            observaciones: response.data.observaciones || '',
            items: response.data.items || []
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

    this.cotizacion.items.push({
      descripcion: this.nuevoItem.descripcion,
      cantidad: this.nuevoItem.cantidad,
      precioUnitario: this.nuevoItem.precioUnitario,
      subtotal: this.nuevoItem.cantidad * this.nuevoItem.precioUnitario
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
    return this.cotizacion.items.reduce((sum, item) => sum + (item.subtotal || 0), 0);
  }

  guardar() {
    if (!this.validar()) return;

    this.loading = true;
    this.error = '';

    this.cotizacionesService.actualizar(this.cotizacionId, this.cotizacion).subscribe({
      next: (response) => {
        if (response.success) {
          this.router.navigate(['/cotizaciones']);
        } else {
          this.error = response.message || 'Error al actualizar cotización';
          this.loading = false;
        }
      },
      error: (error) => {
        this.error = 'Error al actualizar cotización. Por favor intenta de nuevo.';
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
}

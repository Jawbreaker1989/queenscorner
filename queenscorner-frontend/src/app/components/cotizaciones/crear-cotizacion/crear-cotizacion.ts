import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CotizacionesService } from '../../../services/cotizaciones';
import { Clientes } from '../../../services/clientes';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CotizacionRequest, ItemCotizacion } from '../../../models/cotizacion.model';
import { ClienteResponse } from '../../../models/cliente.model';

@Component({
  selector: 'app-crear-cotizacion',
  templateUrl: './crear-cotizacion.html',
  styleUrls: ['./cotizacion-form.css'],
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class CrearCotizacionComponent implements OnInit {
  clientes: ClienteResponse[] = [];
  cotizacion: CotizacionRequest = {
    clienteId: 0,
    descripcion: '',
    fechaValidez: '',
    observaciones: '',
    items: []
  };
  nuevoItem: ItemCotizacion = {
    descripcion: '',
    cantidad: 1,
    precioUnitario: 0
  };

  loading = false;
  cargandoClientes = true;
  error = '';

  constructor(
    private cotizacionesService: CotizacionesService,
    private clientesService: Clientes,
    private router: Router
  ) {}

  ngOnInit() {
    this.cargarClientes();
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

    this.cotizacionesService.crear(this.cotizacion).subscribe({
      next: (response) => {
        if (response.success) {
          this.router.navigate(['/cotizaciones']);
        } else {
          this.error = response.message || 'Error al crear cotización';
          this.loading = false;
        }
      },
      error: (error) => {
        this.error = 'Error al crear cotización. Por favor intenta de nuevo.';
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

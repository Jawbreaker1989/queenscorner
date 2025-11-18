import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import Swal from 'sweetalert2';
import { OrdenesTrabajoService } from '../../../services/ordenes-trabajo';
import { OrdenTrabajoResponse, DetalleOrdenTrabajo } from '../../../models/orden-trabajo.model';

@Component({
  selector: 'app-detalle-orden',
  templateUrl: './detalle-orden.html',
  styleUrls: ['./detalle-orden.css'],
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class DetalleOrdenComponent implements OnInit {
  orden: OrdenTrabajoResponse | null = null;
  cargando: boolean = true;
  nuevoEstado: string = '';
  mostrarCambioEstado: boolean = false;
  procesando: boolean = false;
  generandoPdf: boolean = false;

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
        this.nuevoEstado = this.orden?.estado || '';
        this.cargando = false;
      },
      error: (error: any) => {
        console.error('Error al cargar orden:', error);
        Swal.fire('Error', 'No se pudo cargar la orden', 'error');
        this.cargando = false;
        this.router.navigate(['/ordenes-trabajo']);
      }
    });
  }

  getEstadoColor(estado: string): string {
    const colores: { [key: string]: string } = {
      'PENDIENTE': '#95a5a6',
      'EN_PROGRESO': '#f39c12',
      'COMPLETADA': '#27ae60',
      'CANCELADA': '#e74c3c'
    };
    return colores[estado] || '#95a5a6';
  }

  getEstadoTextColor(estado: string): string {
    return '#fff';
  }

  toggleCambioEstado(): void {
    this.mostrarCambioEstado = !this.mostrarCambioEstado;
    if (!this.mostrarCambioEstado) {
      this.nuevoEstado = this.orden?.estado || '';
    }
  }

  obtenerTransicionesValidas(): string[] {
    const estado = this.orden?.estado;
    const transiciones: { [key: string]: string[] } = {
      'PENDIENTE': ['EN_PROGRESO', 'CANCELADA'],
      'EN_PROGRESO': ['COMPLETADA', 'CANCELADA'],
      'COMPLETADA': [],
      'CANCELADA': []
    };
    return transiciones[estado || ''] || [];
  }

  guardarCambioEstado(): void {
    if (!this.orden || !this.nuevoEstado) {
      Swal.fire('Error', 'Seleccione un estado válido', 'error');
      return;
    }

    if (this.nuevoEstado === this.orden.estado) {
      Swal.fire('Información', 'El estado es el mismo', 'info');
      this.mostrarCambioEstado = false;
      return;
    }

    Swal.fire({
      title: '¿Confirmar cambio de estado?',
      text: `¿Desea cambiar el estado de ${this.orden.codigo} a ${this.nuevoEstado}?`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Sí, cambiar',
      cancelButtonText: 'Cancelar'
    }).then(result => {
      if (result.isConfirmed) {
        this.procesando = true;
        this.ordenesService.cambiarEstado(this.orden!.id, this.nuevoEstado).subscribe({
          next: () => {
            this.procesando = false;
            Swal.fire('Éxito', 'Estado actualizado correctamente', 'success');
            this.mostrarCambioEstado = false;
            this.cargarOrden();
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

  generarPdf(): void {
    if (!this.orden) {
      Swal.fire('Error', 'Orden no válida', 'error');
      return;
    }

    this.generandoPdf = true;
    const html = `
      <html>
        <body style="font-family: Arial, sans-serif; padding: 20px;">
          <h1>Orden de Trabajo</h1>
          <p><strong>Código:</strong> ${this.orden.codigo}</p>
          <p><strong>Cliente:</strong> ${this.orden.cliente?.nombre}</p>
          <p><strong>Descripción:</strong> ${this.orden.descripcion}</p>
          <p><strong>Estado:</strong> ${this.orden.estado}</p>
          <p><strong>Fecha Inicio:</strong> ${new Date(this.orden.fechaInicio).toLocaleDateString()}</p>
          <hr>
          <h2>Detalles</h2>
          <table border="1" style="width: 100%; border-collapse: collapse;">
            <thead>
              <tr style="background-color: #f0f0f0;">
                <th>Descripción</th>
                <th>Cantidad</th>
                <th>Precio Unitario</th>
                <th>Subtotal</th>
              </tr>
            </thead>
            <tbody>
              ${(this.orden.detalles || []).map(d => `
                <tr>
                  <td>${d.descripcion}</td>
                  <td>${d.cantidad}</td>
                  <td>$${d.precioUnitario?.toFixed(2)}</td>
                  <td>$${(d.subtotal || 0).toFixed(2)}</td>
                </tr>
              `).join('')}
            </tbody>
          </table>
          <hr>
          <p><strong>Subtotal:</strong> $${this.orden.subtotal?.toFixed(2)}</p>
          <p><strong>Impuestos:</strong> $${this.orden.impuestos?.toFixed(2)}</p>
          <p><strong style="font-size: 16px;">Total:</strong> <strong style="font-size: 16px;">$${this.orden.total?.toFixed(2)}</strong></p>
        </body>
      </html>
    `;

    setTimeout(() => {
      const ventana = window.open('', '', 'width=800,height=600');
      if (ventana) {
        ventana.document.write(html);
        ventana.document.close();
        ventana.print();
      }
      this.generandoPdf = false;
    }, 800);
  }

  editar(): void {
    this.router.navigate(['/ordenes-trabajo/editar', this.orden?.id]);
  }

  volver(): void {
    this.router.navigate(['/ordenes-trabajo']);
  }
}

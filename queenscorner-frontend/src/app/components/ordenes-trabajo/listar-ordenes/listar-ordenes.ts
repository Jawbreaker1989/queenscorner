import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import Swal from 'sweetalert2';
import { OrdenesTrabajoService } from '../../../services/ordenes-trabajo';
import { OrdenTrabajoResponse } from '../../../models/orden-trabajo.model';

@Component({
  selector: 'app-listar-ordenes',
  templateUrl: './listar-ordenes.html',
  styleUrls: ['./listar-ordenes.css'],
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class ListarOrdenesComponent implements OnInit {
  ordenes: OrdenTrabajoResponse[] = [];
  ordenesFiltradas: OrdenTrabajoResponse[] = [];
  cargando: boolean = true;
  filtro: string = '';

  constructor(
    private ordenesService: OrdenesTrabajoService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.cargarOrdenes();
  }

  cargarOrdenes(): void {
    this.cargando = true;
    this.ordenesService.obtenerTodas().subscribe({
      next: (response: any) => {
        this.ordenes = response.data;
        this.aplicarFiltro();
        this.cargando = false;
      },
      error: (error: any) => {
        console.error('Error al cargar órdenes:', error);
        Swal.fire('Error', 'No se pudieron cargar las órdenes de trabajo', 'error');
        this.cargando = false;
      }
    });
  }

  aplicarFiltro(): void {
    if (!this.filtro.trim()) {
      this.ordenesFiltradas = [...this.ordenes];
    } else {
      const filtroLower = this.filtro.toLowerCase();
      this.ordenesFiltradas = this.ordenes.filter(orden =>
        orden.codigo.toLowerCase().includes(filtroLower) ||
        orden.cliente?.nombre.toLowerCase().includes(filtroLower) ||
        orden.descripcion?.toLowerCase().includes(filtroLower)
      );
    }
  }

  onFiltroChange(): void {
    this.aplicarFiltro();
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

  crearOrden(): void {
    this.router.navigate(['/ordenes-trabajo/crear']);
  }

  verDetalle(id: number): void {
    this.router.navigate(['/ordenes-trabajo/detalle', id]);
  }

  editar(id: number): void {
    this.router.navigate(['/ordenes-trabajo/editar', id]);
  }

  eliminar(orden: OrdenTrabajoResponse): void {
    Swal.fire({
      title: '¿Eliminar orden?',
      text: `¿Desea eliminar la orden ${orden.codigo}? Esta acción no se puede deshacer.`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    }).then(result => {
      if (result.isConfirmed) {
        this.ordenesService.eliminar(orden.id).subscribe({
          next: () => {
            Swal.fire('Éxito', 'Orden eliminada correctamente', 'success');
            this.cargarOrdenes();
          },
          error: (error: any) => {
            console.error('Error al eliminar orden:', error);
            Swal.fire('Error', 'No se pudo eliminar la orden', 'error');
          }
        });
      }
    });
  }
}

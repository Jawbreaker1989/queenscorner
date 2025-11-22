import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CotizacionesService } from '../../../services/cotizaciones';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CotizacionResponse, EstadoCotizacion } from '../../../models/cotizacion.model';
import { delay } from 'rxjs/operators';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-listar-cotizaciones',
  templateUrl: './listar-cotizaciones.html',
  styleUrl: './listar-cotizaciones.css',
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class ListarCotizacionesComponent implements OnInit {
  cotizaciones: CotizacionResponse[] = [];
  cotizacionesFiltradas: CotizacionResponse[] = [];
  loading = false;
  error = '';
  mensajeExito = '';
  pdfGenerando: { [key: number]: boolean } = {};
  pdfGenerados: { [key: number]: boolean } = {};
  searchTerm: string = '';

  constructor(
    private cotizacionesService: CotizacionesService,
    private router: Router
  ) { }

  ngOnInit() {
    this.cargarCotizaciones();
    // Verificar PDFs generados desde localStorage
    this.verificarPdfsGenerados();
  }

  cargarCotizaciones() {
    this.loading = true;
    this.error = '';
    this.cotizacionesService.obtenerTodas().subscribe({
      next: (response) => {
        if (response.success) {
          this.cotizaciones = response.data || [];
          this.filtrarCotizaciones();
        } else {
          this.error = response.message || 'Error al cargar cotizaciones';
        }
        this.loading = false;
      },
      error: () => {
        this.error = 'Error al cargar cotizaciones';
        this.loading = false;
      }
    });
  }

  filtrarCotizaciones() {
    if (!this.searchTerm.trim()) {
      this.cotizacionesFiltradas = [...this.cotizaciones];
      return;
    }

    const term = this.searchTerm.toLowerCase().trim();
    this.cotizacionesFiltradas = this.cotizaciones.filter(cotizacion =>
      cotizacion.codigo.toLowerCase().includes(term) ||
      cotizacion.cliente.nombre.toLowerCase().includes(term) ||
      cotizacion.estado.toLowerCase().includes(term) ||
      cotizacion.descripcion.toLowerCase().includes(term)
    );
  }

  verificarPdfsGenerados() {
    // Revisar localStorage para cada cotización y marcar si PDF fue generado
    this.cotizaciones.forEach(cot => {
      const pdfKey = `pdf_generado_${cot.id}`;
      if (localStorage.getItem(pdfKey)) {
        this.pdfGenerados[cot.id] = true;
      }
    });
  }

  ver(id: number) {
    this.router.navigate(['/cotizaciones/detalle', id]);
  }

  verDobleClick(id: number) {
    this.ver(id);
  }

  crear() {
    this.router.navigate(['/cotizaciones/crear']);
  }

  editar(id: number) {
    const cot = this.cotizaciones.find(c => c.id === id);
    if (!cot) return;
    if (cot.estado !== 'BORRADOR' && cot.estado !== 'ENVIADA') {
      Swal.fire('No permitido', 'Solo se puede editar en BORRADOR o ENVIADA', 'warning');
      return;
    }
    this.router.navigate(['/cotizaciones/editar', id]);
  }

  generarPdf(id: number) {
    const cot = this.cotizaciones.find(c => c.id === id);
    if (!cot || cot.estado !== 'APROBADA' || this.pdfGenerados[id] || this.pdfGenerando[id]) {
      return;
    }

    this.pdfGenerando[id] = true;
    this.cotizacionesService.generarPdf(id).pipe(
      delay(5000) // Simular 5 segundos de carga
    ).subscribe({
      next: (response) => {
        if (response.success) {
          const pdfKey = `pdf_generado_${id}`;
          localStorage.setItem(pdfKey, 'true');
          this.pdfGenerados[id] = true;
        }
        this.pdfGenerando[id] = false;
      },
      error: () => {
        this.pdfGenerando[id] = false;
      }
    });
  }

  eliminar(id: number, codigo: string) {
    Swal.fire({
      title: '¿Ocultar cotización?',
      text: `¿Deseas ocultar la cotización ${codigo} de la lista?`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#dc3545',
      cancelButtonColor: '#6c757d',
      confirmButtonText: 'Sí, ocultar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        // Simplemente eliminar de la lista visual
        const index = this.cotizaciones.findIndex(c => c.id === id);
        if (index > -1) {
          this.cotizaciones.splice(index, 1);
          this.filtrarCotizaciones();
          this.mensajeExito = `Cotización ${codigo} ocultada exitosamente`;
          setTimeout(() => this.mensajeExito = '', 3000);
        }
      }
    });
  }

  cambiarEstado(id: number) {
    const cot = this.cotizaciones.find(c => c.id === id);
    if (!cot || cot.estado === 'APROBADA' || cot.estado === 'RECHAZADA') {
      Swal.fire('No permitido', 'No se puede cambiar estado desde aquí', 'warning');
      return;
    }
    this.router.navigate(['/cotizaciones/detalle', id]);
  }

  getMensajeEstado(estado: EstadoCotizacion): string {
    switch (estado) {
      case 'BORRADOR':
        return '📝 En preparación';
      case 'ENVIADA':
        return '📱 Enviada al cliente';
      case 'APROBADA':
        return '✅ Aprobada';
      case 'RECHAZADA':
        return '❌ Rechazada';
      default:
        return '';
    }
  }

  getEstadoColor(estado: EstadoCotizacion): string {
    switch (estado) {
      case 'BORRADOR':
        return 'badge-secondary';
      case 'ENVIADA':
        return 'badge-info';
      case 'APROBADA':
        return 'badge-success';
      case 'RECHAZADA':
        return 'badge-danger';
      default:
        return 'badge-secondary';
    }
  }

  volver() {
    this.router.navigate(['/dashboard']);
  }
}

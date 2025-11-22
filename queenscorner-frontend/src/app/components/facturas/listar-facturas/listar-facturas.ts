import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { FacturaService } from '../../../services/facturas';
import { Factura, ApiResponse } from '../../../models/factura.model';
import { delay } from 'rxjs/operators';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-listar-facturas',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './listar-facturas.html',
  styleUrls: ['./listar-facturas.css']
})
export class ListarFacturasComponent implements OnInit {
  facturas: Factura[] = [];
  facturasFiltradas: Factura[] = [];
  loading = true;
  error: string | null = null;
  negocioId: number | null = null;
  negocioNombre: string | null = null;
  pdfGenerando: { [key: number]: boolean } = {};
  pdfGenerados: { [key: number]: boolean } = {};
  searchTerm: string = '';

  constructor(
    private facturaService: FacturaService,
    private router: Router,
    private activatedRoute: ActivatedRoute
  ) { }

  ngOnInit() {
    this.activatedRoute.queryParams.subscribe(params => {
      if (params['negocioId']) {
        this.negocioId = params['negocioId'];
        this.negocioNombre = params['negocioNombre'] || null;
        this.cargarFacturasPorNegocio();
      } else {
        this.cargarFacturas();
      }
    });
    // Verificar PDFs generados desde localStorage
    this.verificarPdfsGenerados();
  }

  cargarFacturas() {
    this.loading = true;
    this.error = null;
    this.facturaService.listarFacturas().subscribe({
      next: (response: any) => {
        // Si la respuesta es un arreglo, úsala; si tiene 'data', úsala; si no, vacío
        if (Array.isArray(response)) {
          this.facturas = response;
        } else if (Array.isArray(response?.data)) {
          this.facturas = response.data;
        } else {
          this.facturas = [];
        }
        this.filtrarFacturas();
        this.verificarPdfsGenerados();
        this.loading = false;
      },
      error: (error: any) => {
        this.error = 'Error al cargar facturas';
        this.loading = false;
        console.error(error);
      }
    });
  }

  cargarFacturasPorNegocio() {
    if (!this.negocioId) return;
    this.loading = true;
    this.error = null;
    this.facturaService.listarPorNegocio(this.negocioId).subscribe({
      next: (response: ApiResponse<Factura[]>) => {
        this.facturas = response.data || [];
        this.filtrarFacturas();
        this.verificarPdfsGenerados();
        this.loading = false;
      },
      error: (error: any) => {
        this.error = 'Error al cargar facturas del negocio';
        this.loading = false;
        console.error(error);
      }
    });
  }

  verificarPdfsGenerados() {
    // Revisar localStorage para cada factura y marcar si PDF fue generado
    this.facturas.forEach(factura => {
      const pdfKey = `pdf_generado_factura_${factura.id}`;
      if (localStorage.getItem(pdfKey)) {
        this.pdfGenerados[factura.id] = true;
      }
    });
  }

  filtrarFacturas() {
    if (!this.searchTerm.trim()) {
      this.facturasFiltradas = [...this.facturas];
      return;
    }

    const term = this.searchTerm.toLowerCase().trim();
    this.facturasFiltradas = this.facturas.filter(factura =>
      (factura.numeroFactura || '').toLowerCase().includes(term) ||
      (factura.nombreCliente || '').toLowerCase().includes(term) ||
      (factura.nombreNegocio || '').toLowerCase().includes(term) ||
      (factura.negocio?.cliente?.nombre || '').toLowerCase().includes(term) ||
      (factura.estado || '').toLowerCase().includes(term)
    );
  }

  getEstadoColor(estado: string) {
    const colores: Record<string, string> = {
      'EN_REVISION': 'badge-warning',
      'ENVIADA': 'badge-info',
      'PAGADA': 'badge-success',
      'ANULADA': 'badge-danger'
    };
    return colores[estado] || 'badge-secondary';
  }

  getEstadoLabel(estado: string): string {
    const etiquetas: Record<string, string> = {
      'ENVIADA': 'Emitida'
    };
    return etiquetas[estado] || estado;
  }

  verDetalle(id: number) {
    this.router.navigate(['/facturas/detalle', id]);
  }

  emitirFactura(factura: Factura) {
    if (factura.estado !== 'EN_REVISION') {
      Swal.fire({
        title: 'No permitido',
        text: 'Solo se pueden enviar facturas en estado EN_REVISION',
        icon: 'warning',
        confirmButtonText: 'Entendido'
      });
      return;
    }
    
    Swal.fire({
      title: '¿Emitir factura?',
      text: `¿Deseas emitir la factura ${factura.numeroFactura}?`,
      icon: 'question',
      showCancelButton: true,
      confirmButtonText: 'Sí, emitir',
      cancelButtonText: 'Cancelar',
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33'
    }).then((result) => {
      if (result.isConfirmed) {
        this.facturaService.emitirFactura(factura.id).subscribe({
          next: () => {
            Swal.fire({
              title: '¡Éxito!',
              text: 'Factura emitida correctamente',
              icon: 'success',
              confirmButtonText: 'Continuar'
            });
            this.cargarFacturas();
          },
          error: (error: any) => {
            console.error('Error al emitir factura', error);
            Swal.fire({
              title: 'Error',
              text: 'Error al emitir factura: ' + (error?.error?.message || 'Error desconocido'),
              icon: 'error',
              confirmButtonText: 'Entendido'
            });
          }
        });
      }
    });
  }

  anularFactura(id: number, numeroFactura: string) {
    Swal.fire({
      title: '¿Ocultar factura?',
      text: `¿Deseas ocultar la factura ${numeroFactura} de la lista?`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Sí, ocultar',
      cancelButtonText: 'Cancelar',
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6'
    }).then((result) => {
      if (result.isConfirmed) {
        // Simplemente eliminar de la lista visual
        const index = this.facturas.findIndex(f => f.id === id);
        if (index > -1) {
          this.facturas.splice(index, 1);
          this.filtrarFacturas();
        }
      }
    });
  }

  generarPdf(factura: Factura) {
    if (this.pdfGenerados[factura.id] || this.pdfGenerando[factura.id]) {
      return;
    }

    this.pdfGenerando[factura.id] = true;

    this.facturaService.generarPdf(factura.id).pipe(
      delay(5000) // Simular 5 segundos de carga
    ).subscribe({
      next: () => {
        // Marcar como generado en localStorage
        const pdfKey = `pdf_generado_factura_${factura.id}`;
        localStorage.setItem(pdfKey, 'true');
        this.pdfGenerados[factura.id] = true;
        this.pdfGenerando[factura.id] = false;
        this.negocioId ? this.cargarFacturasPorNegocio() : this.cargarFacturas();
      },
      error: (error: any) => {
        console.error('Error al generar PDF', error);
        this.pdfGenerando[factura.id] = false;
      }
    });
  }

  volver() {
    if (this.negocioId) {
      this.router.navigate(['/negocios/detalle', this.negocioId]);
    } else {
      this.router.navigate(['/dashboard']);
    }
  }
}

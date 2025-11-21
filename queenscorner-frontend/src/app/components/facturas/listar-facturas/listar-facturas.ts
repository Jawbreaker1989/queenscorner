import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { FacturaService } from '../../../services/facturas';
import { Factura, ApiResponse } from '../../../models/factura.model';

@Component({
  selector: 'app-listar-facturas',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './listar-facturas.html',
  styleUrls: ['./listar-facturas.css']
})
export class ListarFacturasComponent implements OnInit {
  facturas: Factura[] = [];
  loading = true;
  error: string | null = null;
  negocioId: number | null = null;
  negocioNombre: string | null = null;
  pdfGenerando: { [key: number]: boolean } = {};
  pdfGenerados: { [key: number]: boolean } = {};

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
      alert('Solo se pueden enviar facturas en estado EN_REVISION');
      return;
    }
    
    if (confirm(`¿Enviar factura ${factura.numeroFactura}?`)) {
      this.facturaService.emitirFactura(factura.id).subscribe({
        next: () => {
          alert('Factura enviada correctamente');
          this.cargarFacturas();
        },
        error: (error: any) => {
          console.error('Error al enviar factura', error);
          alert('Error al enviar factura: ' + (error?.error?.message || 'Error desconocido'));
        }
      });
    }
  }

  anularFactura(id: number, numeroFactura: string) {
    if (confirm(`¿Anular factura ${numeroFactura}?`)) {
      this.facturaService.anularFactura(id).subscribe({
        next: () => {
          alert('Factura anulada correctamente');
          this.negocioId ? this.cargarFacturasPorNegocio() : this.cargarFacturas();
        },
        error: (error: any) => {
          console.error('Error al anular factura', error);
          alert('Error al anular factura: ' + (error?.error?.message || 'Error desconocido'));
        }
      });
    }
  }

  generarPdf(factura: Factura) {
    if (this.pdfGenerados[factura.id]) {
      alert('El PDF para esta factura ya fue generado');
      return;
    }

    if (confirm(`¿Generar PDF para la factura ${factura.numeroFactura}?`)) {
      this.pdfGenerando[factura.id] = true;
      this.facturaService.generarPdf(factura.id).subscribe({
        next: () => {
          // Marcar como generado en localStorage
          const pdfKey = `pdf_generado_factura_${factura.id}`;
          localStorage.setItem(pdfKey, 'true');
          this.pdfGenerados[factura.id] = true;

          alert('PDF generado correctamente');
          this.negocioId ? this.cargarFacturasPorNegocio() : this.cargarFacturas();
        },
        error: (error: any) => {
          console.error('Error al generar PDF', error);
          alert('Error al generar PDF: ' + (error?.error?.message || 'Error desconocido'));
          this.pdfGenerando[factura.id] = false;
        }
      });
    }
  }

  volver() {
    if (this.negocioId) {
      this.router.navigate(['/negocios/detalle', this.negocioId]);
    } else {
      this.router.navigate(['/dashboard']);
    }
  }
}

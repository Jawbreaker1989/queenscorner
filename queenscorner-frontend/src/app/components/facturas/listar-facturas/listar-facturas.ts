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
        this.loading = false;
      },
      error: (error: any) => {
        this.error = 'Error al cargar facturas del negocio';
        this.loading = false;
        console.error(error);
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

  descargarPdf(factura: Factura) {
    if (!factura.rutaPdf) {
      alert('PDF aún no disponible');
      return;
    }
    this.facturaService.descargarPdf(factura.id).subscribe({
      next: (blob: Blob) => {
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = `Factura_${factura.numeroFactura}.pdf`;
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        window.URL.revokeObjectURL(url);
      },
      error: (error: any) => {
        console.error('Error descargando PDF', error);
        alert('Error al descargar PDF');
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

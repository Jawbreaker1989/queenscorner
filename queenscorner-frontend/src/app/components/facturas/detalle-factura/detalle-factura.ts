import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { FacturaService } from '../../../services/facturas';
import { Factura, ApiResponse } from '../../../models/factura.model';

@Component({
  selector: 'app-detalle-factura',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './detalle-factura.html',
  styleUrls: ['./detalle-factura.css']
})
export class DetalleFacturaComponent implements OnInit {
  factura: Factura | null = null;
  loading = true;
  error: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private facturaService: FacturaService
  ) { }

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.cargarFactura(parseInt(id));
    }
  }

  cargarFactura(id: number) {
    this.loading = true;
    this.error = null;
    this.facturaService.obtenerFactura(id).subscribe({
      next: (response: ApiResponse<Factura>) => {
        this.factura = response.data;
        this.loading = false;
      },
      error: (error: any) => {
        this.error = 'Error al cargar factura: ' + (error?.error?.message || 'Error desconocido');
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

  descargarPdf() {
    if (!this.factura) return;
    
    if (!this.factura.rutaPdf) {
      alert('PDF aún no disponible');
      return;
    }

    this.facturaService.descargarPdf(this.factura.id).subscribe({
      next: (blob: Blob) => {
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = `Factura_${this.factura!.numeroFactura}.pdf`;
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

  emitirFactura() {
    if (!this.factura) return;
    
    if (this.factura.estado !== 'EN_REVISION') {
      alert('Solo se pueden enviar facturas en estado EN_REVISION');
      return;
    }

    if (confirm(`¿Enviar factura ${this.factura.numeroFactura}?`)) {
      this.facturaService.emitirFactura(this.factura.id).subscribe({
        next: (response: ApiResponse<Factura>) => {
          this.factura = response.data;
          alert('Factura enviada correctamente');
        },
        error: (error: any) => {
          console.error('Error al enviar factura', error);
          alert('Error al enviar factura: ' + (error?.error?.message || 'Error desconocido'));
        }
      });
    }
  }

  volver() {
    if (this.factura?.negocioId) {
      this.router.navigate(['/facturas'], { queryParams: { negocioId: this.factura.negocioId } });
    } else {
      this.router.navigate(['/facturas']);
    }
  }
}

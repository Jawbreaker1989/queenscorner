import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { FacturaService } from '../../../services/facturas';
import { Factura, ApiResponse } from '../../../models/factura.model';
import Swal from 'sweetalert2';

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
  generandoPdf = false;

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
      next: (response: any) => {
        // Si la respuesta es un objeto con 'data', úsala; si no, usa la respuesta directa
        if (response && typeof response === 'object' && response.data) {
          this.factura = response.data;
        } else {
          this.factura = response;
        }
        this.loading = false;
        console.log('Factura cargada:', this.factura);
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

  generarPdf() {
    if (!this.factura) return;
    
    if (this.factura.estado !== 'EN_REVISION') {
      Swal.fire('Error', 'Solo se pueden generar PDFs para facturas en estado EN_REVISION', 'error');
      return;
    }

    this.generandoPdf = true;
    
    this.facturaService.generarPdf(this.factura.id).subscribe({
      next: (response: ApiResponse<Factura>) => {
        this.generandoPdf = false;
        this.factura = response.data;
        Swal.fire('Éxito', 'PDF generado correctamente', 'success');
        // Recargar la factura para obtener la ruta del PDF actualizada
        this.cargarFactura(this.factura.id);
      },
      error: (error: any) => {
        this.generandoPdf = false;
        const mensaje = error?.error?.message || 'Error desconocido';
        console.error('Error generando PDF:', error);
        Swal.fire('Error', `Error al generar PDF: ${mensaje}`, 'error');
      }
    });
  }

  descargarPdf() {
    if (!this.factura) return;
    
    if (!this.factura.rutaPdf && !this.factura.pathPdf) {
      Swal.fire('Información', 'PDF aún no disponible. Genera el PDF primero.', 'info');
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
        Swal.fire('Error', 'Error al descargar PDF', 'error');
      }
    });
  }

  emitirFactura() {
    if (!this.factura) return;
    
    if (this.factura.estado !== 'EN_REVISION') {
      Swal.fire('Error', 'Solo se pueden enviar facturas en estado EN_REVISION', 'error');
      return;
    }

    Swal.fire({
      title: '¿Emitir factura?',
      text: `¿Desea emitir la factura ${this.factura.numeroFactura}?`,
      icon: 'question',
      showCancelButton: true,
      confirmButtonText: 'Sí, emitir',
      cancelButtonText: 'Cancelar'
    }).then(result => {
      if (result.isConfirmed) {
        this.facturaService.emitirFactura(this.factura!.id).subscribe({
          next: (response: ApiResponse<Factura>) => {
            this.factura = response.data;
            Swal.fire('Éxito', 'Factura emitida correctamente', 'success');
          },
          error: (error: any) => {
            console.error('Error al emitir factura', error);
            Swal.fire('Error', 'Error al emitir factura: ' + (error?.error?.message || 'Error desconocido'), 'error');
          }
        });
      }
    });
  }

  volver() {
    if (this.factura?.negocioId) {
      this.router.navigate(['/facturas'], { queryParams: { negocioId: this.factura.negocioId } });
    } else {
      this.router.navigate(['/facturas']);
    }
  }
}

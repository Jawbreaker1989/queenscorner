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
      next: (response: any) => {
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

  getEstadoLabel(estado: string): string {
    const etiquetas: Record<string, string> = {
      'ENVIADA': 'Emitida'
    };
    return etiquetas[estado] || estado;
  }

  iraDashboard() {
    this.router.navigate(['/dashboard']);
  }

  volver() {
    this.router.navigate(['/facturas']);
  }
}

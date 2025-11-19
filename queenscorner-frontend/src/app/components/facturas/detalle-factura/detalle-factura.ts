import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { FacturaService } from '../../services/facturas';
import { Factura } from '../../models/factura.model';

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
    private facturaService: FacturaService
  ) { }

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.cargarFactura(parseInt(id));
    }
  }

  cargarFactura(id: number) {
    this.facturaService.obtenerFactura(id).subscribe({
      next: (response) => {
        this.factura = response.data;
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Error al cargar factura';
        this.loading = false;
        console.error(error);
      }
    });
  }

  descargarPdf() {
    if (this.factura?.rutaPdf) {
      window.open(this.factura.rutaPdf, '_blank');
    }
  }

  emitirFactura() {
    if (this.factura) {
      this.facturaService.emitirFactura(this.factura.id).subscribe({
        next: (response) => {
          this.factura = response.data;
        },
        error: (error) => {
          console.error('Error al emitir factura', error);
        }
      });
    }
  }

  cambiarEstado(estado: string) {
    if (this.factura) {
      this.facturaService.cambiarEstado(this.factura.id, estado).subscribe({
        next: (response) => {
          this.factura = response.data;
        },
        error: (error) => {
          console.error('Error al cambiar estado', error);
        }
      });
    }
  }
}

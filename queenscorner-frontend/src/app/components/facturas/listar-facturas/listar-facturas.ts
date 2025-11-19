import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FacturaService } from '../../services/facturas';
import { Factura } from '../../models/factura.model';

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

  constructor(private facturaService: FacturaService) { }

  ngOnInit() {
    this.cargarFacturas();
  }

  cargarFacturas() {
    this.loading = true;
    this.facturaService.listarFacturas().subscribe({
      next: (response) => {
        this.facturas = response.data;
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Error al cargar facturas';
        this.loading = false;
        console.error(error);
      }
    });
  }

  anularFactura(id: number) {
    if (confirm('¿Desea anular esta factura?')) {
      this.facturaService.anularFactura(id).subscribe({
        next: () => {
          this.cargarFacturas();
        },
        error: (error) => {
          console.error('Error al anular factura', error);
        }
      });
    }
  }

  descargarPdf(rutaPdf: string) {
    if (rutaPdf) {
      window.open(rutaPdf, '_blank');
    }
  }
}

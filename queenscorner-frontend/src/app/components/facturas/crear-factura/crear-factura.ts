import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { FacturaService } from '../../../services/facturas';
import { NegociosService } from '../../../services/negocios';
import { CotizacionesService } from '../../../services/cotizaciones';
import { Factura, CrearFacturaRequest, LineaFacturaRequest, EstadoFactura } from '../../../models/factura.model';
import { NegocioResponse, ApiResponse } from '../../../models/negocio.model';
import { CotizacionResponse } from '../../../models/cotizacion.model';

@Component({
  selector: 'app-crear-factura',
  templateUrl: './crear-factura.html',
  styleUrls: ['./crear-factura.css'],
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule]
})
export class CrearFacturaComponent implements OnInit {
  form: FormGroup;
  loading = false;
  error: string | null = null;
  success = false;

  negocios: NegocioResponse[] = [];
  negocioSeleccionado: NegocioResponse | null = null;
  cotizacion: CotizacionResponse | null = null;
  lineas: LineaFacturaRequest[] = [];
  
  paso: number = 1; // 1: Datos básicos (NO editable), 2: Agregar items

  constructor(
    private fb: FormBuilder,
    private facturaService: FacturaService,
    private negociosService: NegociosService,
    private cotizacionesService: CotizacionesService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private cdr: ChangeDetectorRef
  ) {
    this.form = this.fb.group({
      negocioId: [null],
      cotizacionId: [null]
    });
  }

  ngOnInit() {
    this.cargarNegocios();
    
    // Check if negocioId is passed as query param
    this.activatedRoute.queryParams.subscribe(params => {
      if (params['negocioId']) {
        const negocioId = parseInt(params['negocioId'], 10);
        
        // Esperar a que se carguen los negocios, luego seleccionar
        const checkAndSelect = () => {
          if (this.negocios.length > 0) {
            this.form.patchValue({ negocioId: negocioId });
            this.onNegocioChange(negocioId);
            
            // Deshabilitar campos de negocio una vez cargado
            this.form.get('negocioId')?.disable();
            this.paso = 2; // Ir al paso de agregar items
            this.cdr.markForCheck();
          } else {
            // Reintentar en 200ms si los negocios no se han cargado
            setTimeout(checkAndSelect, 200);
          }
        };
        
        checkAndSelect();
      }
    });
  }

  cargarNegocios() {
    this.negociosService.obtenerTodos().subscribe({
      next: (response: ApiResponse<NegocioResponse[]>) => {
        this.negocios = response.data || [];
        console.log('Negocios cargados:', this.negocios);
        // Log cada negocio para debugging
        this.negocios.forEach(neg => {
          console.log(`Negocio ${neg.codigo}:`, {
            id: neg.id,
            codigo: neg.codigo,
            cliente: neg.cliente,
            cotizacionId: neg.cotizacionId,
            cotizacion: neg.cotizacion
          });
        });
      },
      error: (err: any) => {
        this.error = 'Error al cargar negocios';
        console.error(err);
      }
    });
  }

  onNegocioChange(negocioId: any) {
    const id = negocioId ? (typeof negocioId === 'string' ? parseInt(negocioId, 10) : negocioId) : null;
    if (!id) {
      this.cotizacion = null;
      this.lineas = [];
      this.negocioSeleccionado = null;
      this.cdr.markForCheck();
      return;
    }

    // Get negocio's data and cotización
    const negocio = this.negocios.find(n => n.id === id);
    if (!negocio) {
      return;
    }
    
    this.negocioSeleccionado = negocio;
    
    // Log para debugging
    console.log('Negocio seleccionado:', this.negocioSeleccionado);
    console.log('Cliente del negocio:', this.negocioSeleccionado.cliente);
    console.log('Cliente nombre:', this.negocioSeleccionado.cliente?.nombre);
    
    // Usar la cotización que viene en el negocio (ahora incluida en la respuesta)
    if (negocio.cotizacion) {
      this.cotizacion = negocio.cotizacion;
      this.form.patchValue({ cotizacionId: this.cotizacion?.id });

      // Auto-populate line items from cotización
      if (this.cotizacion && this.cotizacion.items && this.cotizacion.items.length > 0) {
        this.lineas = this.cotizacion.items.map((item: any) => ({
          itemCotizacionId: item.id,
          descripcion: item.descripcion,
          cantidad: item.cantidad,
          valorUnitario: item.precioUnitario
        }));
      } else {
        // Si no hay items, inicializar con array vacío y permitir agregar manualmente
        this.lineas = [];
      }
    } else if (negocio.cotizacionId) {
      // Fallback: Si no viene la cotización completa, hacer la llamada separada
      this.cotizacionesService.obtenerPorId(negocio.cotizacionId).subscribe({
        next: (response: any) => {
          this.cotizacion = response.data;
          this.form.patchValue({ cotizacionId: this.cotizacion?.id });

          // Auto-populate line items from cotización
          if (this.cotizacion && this.cotizacion.items && this.cotizacion.items.length > 0) {
            this.lineas = this.cotizacion.items.map((item: any) => ({
              itemCotizacionId: item.id,
              descripcion: item.descripcion,
              cantidad: item.cantidad,
              valorUnitario: item.precioUnitario
            }));
          } else {
            this.lineas = [];
          }
          this.cdr.markForCheck();
        },
        error: (err: any) => {
          this.error = 'Error al cargar cotización';
          console.error(err);
        }
      });
    } else {
      // Si el negocio no tiene cotización, inicializar vacío
      this.cotizacion = null;
      this.lineas = [];
    }
    
    // Forzar detección de cambios
    this.cdr.markForCheck();
  }

  agregarLinea() {
    this.lineas.push({
      itemCotizacionId: 0,
      descripcion: '',
      cantidad: 1,
      valorUnitario: 0
    });
  }

  eliminarLinea(index: number) {
    this.lineas.splice(index, 1);
  }

  calcularValorLinea(linea: LineaFacturaRequest): number {
    return (linea.cantidad || 0) * (linea.valorUnitario || 0);
  }

  calcularSubtotal(): number {
    return this.lineas.reduce((sum, linea) => sum + this.calcularValorLinea(linea), 0);
  }

  calcularIva(): number {
    return this.calcularSubtotal() * 0.19;
  }

  calcularTotal(): number {
    return this.calcularSubtotal() + this.calcularIva();
  }

  crearFactura() {
    // Validar datos esenciales
    if (!this.negocioSeleccionado) {
      this.error = 'Debe seleccionar un negocio';
      return;
    }
    if (this.lineas.length === 0) {
      this.error = 'Debe agregar al menos una línea de factura';
      return;
    }

    this.loading = true;
    this.error = null;

    const request: CrearFacturaRequest = {
      negocioId: this.negocioSeleccionado.id,
      cotizacionId: this.cotizacion?.id || this.negocioSeleccionado.cotizacionId,
      lineas: this.lineas
    };

    this.facturaService.crearFactura(request).subscribe({
      next: (response: any) => {
        this.loading = false;
        this.success = true;
        // Handle both ApiResponse<Factura> and direct FacturaResponse
        const facturaId = response.data?.id || response.id;
        if (facturaId) {
          setTimeout(() => {
            this.router.navigate(['/facturas/detalle', facturaId]);
          }, 1500);
        } else {
          this.error = 'Error: No se pudo obtener el ID de la factura';
          console.error('Invalid response:', response);
        }
      },
      error: (err: any) => {
        this.loading = false;
        this.error = err.error?.message || 'Error al emitir factura';
        console.error(err);
      }
    });
  }

  cancelar() {
    this.router.navigate(['/facturas']);
  }
}


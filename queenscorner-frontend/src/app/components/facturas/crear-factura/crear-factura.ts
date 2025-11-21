import { Component, OnInit } from '@angular/core';
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
  
  paso: number = 1; // 1: Datos básicos (NO editable), 2: Agregar items, 3: Confirmación

  medioPagoOptions = [
    { value: 'TRANSFERENCIA', label: 'Transferencia Bancaria' },
    { value: 'EFECTIVO', label: 'Efectivo' },
    { value: 'CHEQUE', label: 'Cheque' },
    { value: 'TARJETA', label: 'Tarjeta de Crédito' },
    { value: 'OTRO', label: 'Otro' }
  ];

  constructor(
    private fb: FormBuilder,
    private facturaService: FacturaService,
    private negociosService: NegociosService,
    private cotizacionesService: CotizacionesService,
    private router: Router,
    private activatedRoute: ActivatedRoute
  ) {
    // negocioId NO requiere validador porque se deshabilita después de seleccionar
    // Validación de fechaVencimiento y medioPago se hace en crearFactura(), no en el form
    this.form = this.fb.group({
      negocioId: [null],
      cotizacionId: [null],
      fechaVencimiento: [''],
      medioPago: [''],
      referenciaPago: [''],
      notas: [''],
      condicionesPago: ['']
    });
  }

  ngOnInit() {
    this.cargarNegocios();
    
    // Check if negocioId is passed as query param
    this.activatedRoute.queryParams.subscribe(params => {
      if (params['negocioId']) {
        const negocioId = parseInt(params['negocioId'], 10);
        setTimeout(() => {
          this.form.patchValue({ negocioId: negocioId });
          this.onNegocioChange(negocioId);
          
          // Deshabilitar campos de negocio una vez cargado
          this.form.get('negocioId')?.disable();
          this.paso = 2; // Ir al paso de agregar items
        }, 500);
      }
    });
  }

  cargarNegocios() {
    this.negociosService.obtenerTodos().subscribe({
      next: (response: ApiResponse<NegocioResponse[]>) => {
        this.negocios = response.data || [];
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
      return;
    }

    // Get negocio's data and cotización
    const negocio = this.negocios.find(n => n.id === id);
    if (!negocio) {
      return;
    }
    
    this.negocioSeleccionado = negocio;
    
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
    // Validar campos requeridos antes de crear
    const fechaVencimiento = this.form.get('fechaVencimiento')?.value;
    const medioPago = this.form.get('medioPago')?.value;
    
    if (!fechaVencimiento || fechaVencimiento.trim() === '') {
      this.error = 'La fecha de vencimiento es requerida';
      return;
    }
    if (!medioPago || medioPago.trim() === '') {
      this.error = 'Debe seleccionar un medio de pago';
      return;
    }
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
      fechaVencimiento: fechaVencimiento,
      medioPago: medioPago,
      referenciaPago: this.form.get('referenciaPago')?.value || '',
      notas: this.form.get('notas')?.value || '',
      condicionesPago: this.form.get('condicionesPago')?.value || '',
      lineas: this.lineas
    };

    this.facturaService.crearFactura(request).subscribe({
      next: (response: any) => {
        this.loading = false;
        this.success = true;
        setTimeout(() => {
          this.router.navigate(['/facturas/detalle', response.data.id]);
        }, 1500);
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

  getMedioPagoLabel(value: string): string {
    return this.medioPagoOptions.find(m => m.value === value)?.label || value;
  }
}


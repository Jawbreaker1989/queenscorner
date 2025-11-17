import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Clientes } from '../../../services/clientes';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ClienteRequest, ClienteResponse } from '../../../models/cliente.model';

@Component({
  selector: 'app-editar-cliente',
  templateUrl: './editar-cliente.html',
  styleUrls: ['./cliente-form.css'],
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class EditarClienteComponent implements OnInit {
  cliente: ClienteRequest = {
    nombre: '',
    email: '',
    telefono: '',
    direccion: '',
    ciudad: ''
  };
  clienteId: number = 0;
  loading = false;
  cargando = true;
  error = '';

  constructor(
    private clientesService: Clientes,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    this.clienteId = Number(this.route.snapshot.paramMap.get('id'));
    if (!this.clienteId) {
      this.router.navigate(['/clientes']);
      return;
    }
    this.cargarCliente();
  }

  cargarCliente() {
    this.cargando = true;
    this.error = '';

    this.clientesService.obtenerPorId(this.clienteId).subscribe({
      next: (response) => {
        if (response.success && response.data) {
          this.cliente = {
            nombre: response.data.nombre,
            email: response.data.email,
            telefono: response.data.telefono,
            direccion: response.data.direccion,
            ciudad: response.data.ciudad
          };
        } else {
          this.error = 'No se pudo cargar el cliente';
        }
        this.cargando = false;
      },
      error: (error) => {
        this.error = 'Error al cargar cliente. Por favor intenta de nuevo.';
        this.cargando = false;
      }
    });
  }

  guardar() {
    if (!this.validar()) return;

    this.loading = true;
    this.error = '';

    this.clientesService.actualizar(this.clienteId, this.cliente).subscribe({
      next: (response) => {
        if (response.success) {
          this.router.navigate(['/clientes']);
        } else {
          this.error = response.message || 'Error al actualizar cliente';
          this.loading = false;
        }
      },
      error: (error) => {
        this.error = 'Error al actualizar cliente. Por favor intenta de nuevo.';
        this.loading = false;
      }
    });
  }

  validar(): boolean {
    if (!this.cliente.nombre.trim()) {
      this.error = 'El nombre es requerido';
      return false;
    }
    if (!this.cliente.email.trim() || !this.esEmailValido(this.cliente.email)) {
      this.error = 'Email inválido';
      return false;
    }
    if (!this.cliente.telefono.trim()) {
      this.error = 'El teléfono es requerido';
      return false;
    }
    if (!this.cliente.direccion.trim()) {
      this.error = 'La dirección es requerida';
      return false;
    }
    if (!this.cliente.ciudad.trim()) {
      this.error = 'La ciudad es requerida';
      return false;
    }
    return true;
  }

  esEmailValido(email: string): boolean {
    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return regex.test(email);
  }

  cancelar() {
    this.router.navigate(['/clientes']);
  }
}

import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Clientes } from '../../../services/clientes';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ClienteRequest, ClienteResponse } from '../../../models/cliente.model';

@Component({
  selector: 'app-crear-cliente',
  templateUrl: './crear-cliente.html',
  styleUrls: ['./cliente-form.css'],
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class CrearClienteComponent implements OnInit {
  cliente: ClienteRequest = {
    nombre: '',
    email: '',
    telefono: '',
    direccion: '',
    ciudad: ''
  };
  loading = false;
  error = '';

  constructor(
    private clientesService: Clientes,
    private router: Router
  ) {}

  ngOnInit() {}

  guardar() {
    if (!this.validar()) return;

    this.loading = true;
    this.error = '';

    this.clientesService.crear(this.cliente).subscribe({
      next: (response) => {
        if (response.success) {
          this.router.navigate(['/clientes']);
        } else {
          this.error = response.message || 'Error al crear cliente';
          this.loading = false;
        }
      },
      error: (error) => {
        this.error = 'Error al crear cliente. Por favor intenta de nuevo.';
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

import { Component, OnInit } from '@angular/core';
import { Clientes } from '../../../services/clientes';

@Component({
  selector: 'app-listar-clientes',
  templateUrl: './listar-clientes.html',
  styleUrls: ['./listar-clientes.css']
})
export class ListarClientesComponent implements OnInit {
  clientes: any[] = [];

  constructor(private clientesService: Clientes) {}

  ngOnInit() {
    this.cargarClientes();
  }

  cargarClientes() {
    this.clientesService.getClientes().subscribe({
      next: (response: any) => this.clientes = response.data,
      error: (error) => console.error('Error cargando clientes', error)
    });
  }
}
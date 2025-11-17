import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login';
import { DashboardComponent } from './components/dashboard/dashboard';
import { ListarClientesComponent } from './components/clientes/listar-clientes/listar-clientes';
import { CrearClienteComponent } from './components/clientes/crear-cliente/crear-cliente';
import { EditarClienteComponent } from './components/clientes/editar-cliente/editar-cliente';
import { DetalleClienteComponent } from './components/clientes/detalle-cliente/detalle-cliente';
import { AuthGuard } from './guards/auth-guard';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard] },
  {
    path: 'clientes',
    canActivate: [AuthGuard],
    children: [
      { path: '', component: ListarClientesComponent },
      { path: 'crear', component: CrearClienteComponent },
      { path: 'editar/:id', component: EditarClienteComponent },
      { path: 'detalle/:id', component: DetalleClienteComponent }
    ]
  },
  { path: '**', redirectTo: 'login' }
];
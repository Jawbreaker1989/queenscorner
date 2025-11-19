import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login';
import { DashboardComponent } from './components/dashboard/dashboard';
import { ListarClientesComponent } from './components/clientes/listar-clientes/listar-clientes';
import { CrearClienteComponent } from './components/clientes/crear-cliente/crear-cliente';
import { EditarClienteComponent } from './components/clientes/editar-cliente/editar-cliente';
import { DetalleClienteComponent } from './components/clientes/detalle-cliente/detalle-cliente';
import { ListarCotizacionesComponent } from './components/cotizaciones/listar-cotizaciones/listar-cotizaciones';
import { CrearCotizacionComponent } from './components/cotizaciones/crear-cotizacion/crear-cotizacion';
import { EditarCotizacionComponent } from './components/cotizaciones/editar-cotizacion/editar-cotizacion';
import { DetalleCotizacionComponent } from './components/cotizaciones/detalle-cotizacion/detalle-cotizacion';
import { ListarNegociosComponent } from './components/negocios/listar-negocios/listar-negocios';
import { EditarNegocioComponent } from './components/negocios/editar-negocio/editar-negocio';
import { DetalleNegocioComponent } from './components/negocios/detalle-negocio/detalle-negocio';
import { ListarFacturasComponent } from './components/facturas/listar-facturas/listar-facturas';
import { CrearFacturaComponent } from './components/facturas/crear-factura/crear-factura';
import { DetalleFacturaComponent } from './components/facturas/detalle-factura/detalle-factura';
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
  {
    path: 'cotizaciones',
    canActivate: [AuthGuard],
    children: [
      { path: '', component: ListarCotizacionesComponent },
      { path: 'crear', component: CrearCotizacionComponent },
      { path: 'editar/:id', component: EditarCotizacionComponent },
      { path: 'detalle/:id', component: DetalleCotizacionComponent }
    ]
  },
  {
    path: 'negocios',
    canActivate: [AuthGuard],
    children: [
      { path: '', component: ListarNegociosComponent },
      { path: 'editar/:id', component: EditarNegocioComponent },
      { path: 'detalle/:id', component: DetalleNegocioComponent }
    ]
  },
  {
    path: 'facturas',
    canActivate: [AuthGuard],
    children: [
      { path: '', component: ListarFacturasComponent },
      { path: 'crear', component: CrearFacturaComponent },
      { path: 'detalle/:id', component: DetalleFacturaComponent }
    ]
  },
  { path: '**', redirectTo: 'login' }
];
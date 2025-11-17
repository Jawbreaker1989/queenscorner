import { Component } from '@angular/core';
import { Auth } from '../../services/auth';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.html',
  styleUrls: ['./dashboard.css']
})
export class DashboardComponent {
  
  constructor(private auth: Auth) {}

  logout() {
    this.auth.logout();
    window.location.reload();
  }
}
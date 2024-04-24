import { Injectable } from '@angular/core';
import { Environment } from '../environment/environment';
import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Observable } from 'rxjs/internal/Observable';
import { ResetPasswordModel } from '../model/reset-password.model';
import { TokenService } from './token.service';
import { LoginModel } from '../model/login.model';

@Injectable({
  providedIn: 'root'
})
export class ReportService {
  private apiUrl = `${Environment.apiBaseUrl}/api/report`;
  private jsonUrl = 'assets/data.json';
  private apiConfig = { headers: this.createHeader() }

  constructor(private http: HttpClient) { }

  reportCategory(): Observable<any> {
    return this.http.get(`${this.apiUrl}/count-by-category`);
  }

  reportBrand(): Observable<any> {
    return this.http.get(`${this.apiUrl}/count-by-brand`);
  }

  private createHeader() {
    return new HttpHeaders({ 'Content-Type': 'application/json' });
  }
}

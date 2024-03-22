import { Injectable } from '@angular/core';
import { Environment } from '../environment/environment';
import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { RegisterModel } from '../model/register.model';
import { Observable } from 'rxjs/internal/Observable';
import { ResetPasswordModel } from '../model/reset-password.model';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {
  private apiUrl = `${Environment.apiBaseUrl}`;
  private apiConfig = { headers: this.createHeader() }

  constructor(private http: HttpClient) { }

  register(register: RegisterModel) {
    return this.http.post(`${this.apiUrl}/register`, register, this.apiConfig);
  }

  findByEmail(email: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/customer/${email}`);
  }

  private createHeader() {
    return new HttpHeaders({ 'Content-Type': 'application/json' });
  }

}

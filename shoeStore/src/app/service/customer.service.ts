import { Injectable } from '@angular/core';
import { Environment } from '../environment/environment';
import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { RegisterModel } from '../model/register.model';
import { Observable } from 'rxjs/internal/Observable';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {
  private apiUrl = `${Environment.apiBaseUrl}`;
  private apiConfig = { headers: this.createHeader() }

  constructor(private http: HttpClient) { }

  register(register: RegisterModel) {
    return this.http.post(`${this.apiUrl}/register`, register, this.apiConfig);
  };

  verificationEmailByLink(code: string): Observable<any>  {
    const params = new HttpParams().set('code', code);
    return this.http.get(`${this.apiUrl}/verify`, { params });
  }

  verificationEmailByCode(email: string, code: string): Observable<any>  {
    const requestBody = { code, email };
    console.log(requestBody);

    return this.http.post(`${this.apiUrl}/verification-email-by-code`, requestBody, this.apiConfig);
  }

  sendVerificationEmailByCode(email: string): Observable<any>  {
    const params = new HttpParams().set('email', email);
    return this.http.get(`${this.apiUrl}/send-verification-email-by-code`, { params});
  }

  private createHeader() {
    return new HttpHeaders({ 'Content-Type': 'application/json' });
  }

}

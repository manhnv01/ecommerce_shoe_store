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
  private api = `${Environment.apiBaseUrl}`;
  private apiConfig = { headers: this.createHeader() }

  constructor(private http: HttpClient) { }

  findAll(page: number, size: number, sortDir: string, sortBy: string, search: string, status: string): Observable<any> {
    const params = new HttpParams()
      .set('search', search)
      .set('size', size.toString())
      .set('page', page.toString())
      .set('sort-direction', sortDir)
      .set('sort-by', sortBy)
      .set('status', status)

    return this.http.get(this.api, { params });
  }

  register(register: RegisterModel) {
    return this.http.post(`${this.api}/register`, register, this.apiConfig);
  }

  findByEmail(email: string): Observable<any> {
    return this.http.get(`${this.api}/customer/${email}`);
  }

  updateProfile(customer: any): Observable<any> {
    return this.http.put(`${this.api}/customer`, customer, this.apiConfig);
  }

  getTotals(): Observable<any> {
    return this.http.get<any>(this.api + `/totals`);
  }

  private createHeader() {
    return new HttpHeaders({ 'Content-Type': 'application/json' });
  }

}

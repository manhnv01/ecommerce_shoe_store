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
export class ReturnService {
  private apiUrl = `${Environment.apiBaseUrl}/api/return-product`;
  private jsonUrl = 'assets/data.json';
  private apiConfig = { headers: this.createHeader() }

  constructor(private http: HttpClient, private tokenService: TokenService) { }

  private createHeader() {
    return new HttpHeaders({ 'Content-Type': 'application/json' });
  }

  getTotals(): Observable<any> {
    return this.http.get<any>(this.apiUrl + `/totals`);
  }

  findAll(page: number, size: number, sortDir: string, sortBy: string, search: string, status: string): Observable<any> {
    const params = new HttpParams()
      .set('search', search)
      .set('size', size.toString())
      .set('page', page.toString())
      .set('sort-direction', sortDir)
      .set('sort-by', sortBy)
      .set('status', status);

    return this.http.get(this.apiUrl, { params });
  }

  saveReturn(returnProduct: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, returnProduct, this.apiConfig);
  }
}

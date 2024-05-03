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

  getTop5BestSeller(month: number, year: number): Observable<any> {
    let queryParams = new HttpParams()
    .set('month', month.toString())
    .set('year', year.toString());

    return this.http.get(`${this.apiUrl}/top-5-best-seller`, { params: queryParams });
  }

  getProductInterest() {
    return this.http.get(`${this.apiUrl}/product-interest`);
  }

  getRevenueByYear(year: number): Observable<any> {
    let queryParams = new HttpParams()
    .set('year', year.toString());
    return this.http.get(`${this.apiUrl}/revenue-by-year/`, { params: queryParams });
  }

  getCostByYear(year: number): Observable<any> {
    let queryParams = new HttpParams()
    .set('year', year.toString());
    return this.http.get(`${this.apiUrl}/cost-by-year/`, { params: queryParams });
  }

  getCostReturnByYear(year: number): Observable<any> {
    let queryParams = new HttpParams()
    .set('year', year.toString());
    return this.http.get(`${this.apiUrl}/cost-return-by-year/`, { params: queryParams });
  }

  exportInvoicePdf(orderId: number): Observable<any> {
    return this.http.get(`${Environment.apiBaseUrl}/api/export/pdf/invoice/${orderId}`, { responseType: 'blob' });
  }

  ///////////////////////////////////////////
  orderReport(year: number) {
    return this.http.get(`${this.apiUrl}/order?year=${year}`);
  }

  receiptReport(year: number) {
    return this.http.get(`${this.apiUrl}/receipt?year=${year}`);
  }

  exportInventoryReport() {
    return this.http.get(`${this.apiUrl}/inventory/export`, {
      responseType: 'blob'
    });
  }

  exportOrderReport(year: number) {
    return this.http.get(`${this.apiUrl}/order/export?year=${year}`, {
      responseType: 'blob'
    });
  }

  exportReceiptReport(year: number) {
    return this.http.get(`${this.apiUrl}/receipt/export?year=${year}`, {
      responseType: 'blob'
    });
  }

  private createHeader() {
    return new HttpHeaders({ 'Content-Type': 'application/json' });
  }
}

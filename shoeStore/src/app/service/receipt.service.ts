import { Injectable } from '@angular/core';
import { Environment } from '../environment/environment';
import { ReceiptModel } from '../model/receipt.model';
import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Observable } from 'rxjs/internal/Observable';

@Injectable({
  providedIn: 'root'
})
export class ReceiptService {
  private api = `${Environment.apiBaseUrl}/admin/receipt`;
  private apiConfig = { headers: this.createHeader() }

  constructor(private http: HttpClient) { }

  findAll(page: number, size: number, sortDir: string, sortBy: string, search: string): Observable<any> {
    const params = new HttpParams()
      .set('search', search)
      .set('size', size.toString())
      .set('page', page.toString())
      .set('sort-direction', sortDir)
      .set('sort-by', sortBy);

    return this.http.get(this.api, { params });
  }

  findById(id: number): Observable<ReceiptModel> {
    return this.http.get<ReceiptModel>(`${this.api}/${id}`);
  }

  getTotals(): Observable<any> {
    return this.http.get<any>(this.api + `/totals`);
  }

  create(receipt: ReceiptModel) {
    return this.http.post(this.api, receipt, this.apiConfig);
  };

  private createHeader() {
    return new HttpHeaders({ 'Content-Type': 'application/json' });
  }

}

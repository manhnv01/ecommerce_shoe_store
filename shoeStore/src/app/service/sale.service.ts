import { Injectable } from '@angular/core';
import { Environment } from '../environment/environment';
import { SaleModel } from '../model/sale.model';
import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Observable } from 'rxjs/internal/Observable';

@Injectable({
  providedIn: 'root'
})
export class SaleService {
  private api = `${Environment.apiBaseUrl}/admin/sale`;
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

  findById(id: number): Observable<SaleModel> {
    return this.http.get<SaleModel>(`${this.api}/${id}`);
  }

  validateProductInSale(sale: SaleModel): Observable<SaleModel> {
    return this.http.post<SaleModel>(`${this.api}/validate-product-in-sale`, sale, this.apiConfig);
  }

  getTotals(): Observable<any> {
    return this.http.get<any>(this.api + `/totals`);
  }

  save(sale: SaleModel) {
    if (sale.id != null) {

      return this.http.put(this.api, sale, this.apiConfig);
    }
    return this.http.post(this.api, sale, this.apiConfig);
  };

  delete(id: number) {
    return this.http.delete(`${this.api}/${id}`,);
  }

  deleteOne(id: number) {
    const options = {
      headers: this.apiConfig.headers,
      body: id
    };

    return this.http.delete(`${this.api}/${id}`, options);
  }

  deletes(ids: number[]) {
    const url = `${this.api}/${ids.join(',')}`;
    const options = {
      headers: this.apiConfig.headers,
      body: ids
    };

    return this.http.delete(url, options);
  }

  private createHeader() {
    return new HttpHeaders({ 'Content-Type': 'application/json' });
  }

}

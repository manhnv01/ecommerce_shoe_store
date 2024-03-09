import { Injectable } from '@angular/core';
import { Environment } from '../environment/environment';
import { SupplierModel } from '../model/supplier.model';
import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Observable } from 'rxjs/internal/Observable';
import { tap } from 'rxjs/internal/operators/tap';
import { ActivatedRoute, Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class SupplierService {
  private api = `${Environment.apiBaseUrl}/api/supplier`;
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

  findById(id: number): Observable<SupplierModel> {
    return this.http.get<SupplierModel>(`${this.api}/${id}`);
  }

  getTotals(): Observable<any> {
    return this.http.get<any>(this.api + `/totals`);
  }

  save(supplier: SupplierModel) {
    if (supplier.id != null) {
      return this.http.put(this.api, supplier, this.apiConfig);
    }
    return this.http.post(this.api, supplier, this.apiConfig);
  };

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

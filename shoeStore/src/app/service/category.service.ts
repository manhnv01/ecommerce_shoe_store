import { Injectable } from '@angular/core';
import { Environment } from '../environment/environment';
import { CategoryModel } from '../model/category.model';
import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Observable } from 'rxjs/internal/Observable';
import { tap } from 'rxjs/internal/operators/tap';
import { ActivatedRoute, Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {
  private api = `${Environment.apiBaseUrl}/api/category`;
  private apiConfig = { headers: this.createHeader() }

  constructor(private http: HttpClient) { }

  findAll(page: number, size: number, sortDir: string, sortBy: string, search: string, enabled: string): Observable<any> {
    const params = new HttpParams()
      .set('search', search)
      .set('size', size.toString())
      .set('page', page.toString())
      .set('sort-direction', sortDir)
      .set('sort-by', sortBy)
      .set('enabled', enabled);

    return this.http.get(this.api, { params });
  }

  
  findAllOption(page: number, size: number, sortDir: string, sortBy: string): Observable<any> {
    const params = new HttpParams()
      .set('size', size.toString())
      .set('page', page.toString())
      .set('sort-direction', sortDir)
      .set('sort-by', sortBy);

    return this.http.get(this.api, { params });
  }

  getAll(): Observable<CategoryModel[]> {
    return this.http.get<CategoryModel[]>(`${this.api}/get-all`);
  }

  findById(id: number): Observable<CategoryModel> {
    return this.http.get<CategoryModel>(`${this.api}/${id}`);
  }

  getCategoryTotals(): Observable<any> {
    return this.http.get<any>(this.api + `/totals`);
  }

  save(category: CategoryModel) {
    if (category.id != null) {
      return this.http.put(this.api, category, this.apiConfig);
    }
    return this.http.post(this.api, category, this.apiConfig);
  };

  updatesStatus(ids: number[], enabled: boolean) {
    const url = `${this.api}/${ids.join(',')}`;
    const options = {
      headers: this.apiConfig.headers,
      params: { enabled: enabled.toString() } // Thêm tham số enabled vào phần params
    };

    return this.http.put(url, null, options);
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

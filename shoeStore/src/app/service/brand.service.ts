import { Injectable } from '@angular/core';
import { Environment } from '../environment/environment';
import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Observable } from 'rxjs/internal/Observable';
import { BrandModel } from '../model/brand.model';

@Injectable({
  providedIn: 'root'
})
export class BrandService {
  private api = `${Environment.apiBaseUrl}/admin/brand`;
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

  findById(id: number): Observable<BrandModel> {
    return this.http.get<BrandModel>(`${this.api}/${id}`);
  }

  getTotals(): Observable<any> {
    return this.http.get<any>(this.api + `/totals`);
  }

  create(brand: BrandModel, thumbnailFile: File) {
    const formData = new FormData();
    formData.append('name', brand.name);
    formData.append('file', thumbnailFile);
    formData.append('slug', brand.slug);
    formData.append('enabled', brand.enabled.toString());
    return this.http.post(this.api, formData);
  }

  update(brand: BrandModel, thumbnailFile: File) {
    const formData = new FormData();
    formData.append('id', brand.id.toString());
    formData.append('name', brand.name);
    formData.append('file', thumbnailFile);
    formData.append('slug', brand.slug);
    formData.append('enabled', brand.enabled.toString());
    return this.http.put(this.api, formData);
  }

  changeSwitch(id: number) {
    return this.http.put(`${this.api}/switch/${id}`, null, this.apiConfig);
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
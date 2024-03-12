import { Injectable } from '@angular/core';
import { Environment } from '../environment/environment';
import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Observable } from 'rxjs/internal/Observable';
import { EmployeeModel } from '../model/employee.model';

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {
  private api = `${Environment.apiBaseUrl}/api/employee`;
  private apiConfig = { headers: this.createHeader() }

  constructor(private http: HttpClient) { }

  private createHeader() {
    return new HttpHeaders({ 'Content-Type': 'application/json' });
  }

  findAll(page: number, size: number, sortDir: string, sortBy: string, search: string): Observable<any> {
    const params = new HttpParams()
      .set('search', search)
      .set('size', size.toString())
      .set('page', page.toString())
      .set('sort-direction', sortDir)
      .set('sort-by', sortBy);

    return this.http.get(this.api, { params });
  }

  create(employee: EmployeeModel, avatar: File) {
    const formData = new FormData();
    formData.append('name', employee.name);
    formData.append('file', avatar);
    formData.append('status', employee.status);
    formData.append('phone', employee.phone);
    formData.append('email', employee.email);
    formData.append('birthday', employee.birthday.toString());
    formData.append('gender', employee.gender);
    console.log(employee);
    return this.http.post(this.api, formData);
  }

  update(employee: EmployeeModel, avatar: File) {
    const formData = new FormData();
    formData.append('id', employee.id.toString());
    formData.append('name', employee.name);
    formData.append('file', avatar);
    formData.append('status', employee.status);
    formData.append('phone', employee.phone);
    formData.append('email', employee.email);
    formData.append('birthday', employee.birthday.toString());
    formData.append('gender', employee.gender);
    return this.http.put(this.api, formData);
  }


  findById(id: number): Observable<EmployeeModel> {
    return this.http.get<EmployeeModel>(`${this.api}/${id}`);
  }

  getTotals(): Observable<any> {
    return this.http.get<any>(this.api + `/totals`);
  }

  delete(id: number) {
    return this.http.delete(`${this.api}/${id}`,);
  }

}


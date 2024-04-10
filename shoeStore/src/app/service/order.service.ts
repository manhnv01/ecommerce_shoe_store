import { Injectable } from '@angular/core';
import { Environment } from '../environment/environment';
import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { OrderModel } from '../model/order.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private api = `${Environment.apiBaseUrl}/api/order`;
  private apiConfig = {headers: {'Content-Type': 'application/json'}};

  constructor(private http: HttpClient) {
  }

  findAll( pageSize: number, pageNumber: number, sortDir: string, sortBy: string, filter: string, search: string) {
    let queryParams = new HttpParams();
    queryParams = queryParams.append("search", search);
    queryParams = queryParams.append("filter", filter);
    queryParams = queryParams.append("page-size", pageSize);
    queryParams = queryParams.append("page-number", pageNumber);
    queryParams = queryParams.append("sort-direction", sortDir);
    queryParams = queryParams.append("sort-by", sortBy);
    return this.http.get(`${this.api}`, {params: queryParams});
  }

  findAllByCustomer(email: string, pageSize: number, pageNumber: number, sortDir: string, sortBy: string, filter: string) {
    let queryParams = new HttpParams();
    queryParams = queryParams.append("email", email);
    queryParams = queryParams.append("filter", filter);
    queryParams = queryParams.append("page-size", pageSize);
    queryParams = queryParams.append("page-number", pageNumber);
    queryParams = queryParams.append("sort-direction", sortDir);
    queryParams = queryParams.append("sort-by", sortBy);
    return this.http.get(`${this.api}/customer`, {params: queryParams});
  }

  getTotalsByUserLogin(): Observable<any> {
    return this.http.get<any>(`${this.api}/customer/totals`);
  }

  getTotalsForAdmin(): Observable<any> {
    return this.http.get<any>(`${this.api}/totals`);
  }

  findById(id: number) {
    return this.http.get(`${this.api}/${id}`);
  }

  findByIdWithClient(id: number) {
    return this.http.get(`${this.api}/${id}`);
  }

  create(orderModel: OrderModel) {
    return this.http.post(this.api, orderModel, this.apiConfig);
  }

  updateOrderStatus(id: number, orderStatus: number, cancelReason: string) {
    const formData = new FormData();
    formData.append('id', id.toString());
    formData.append('orderStatus', orderStatus.toString());
    formData.append('cancelReason', cancelReason);
    return this.http.put(this.api, formData);

    // return this.http.put(`${this.apiOrderAdminUrl}/${id}/${orderStatus}`, null, this.apiConfigUrl);
  }

  updateOrderStatusClient(id: number, orderStatus: number, cancelReason: string) {
    const formData = new FormData();
    formData.append('id', id.toString());
    formData.append('orderStatus', orderStatus.toString());
    formData.append('cancelReason', cancelReason);
    return this.http.put(this.api, formData);
  }

  payment(amount: number, orderId: number) {
    const formData = new FormData();
    formData.append('amount', amount.toString());
    formData.append('orderInfo', orderId.toString());
    return this.http.post(`${this.api}/create-payment`, formData);
  }

  getById(id: number) {
    return this.http.get(`${this.api}/${id}`);
  }

  private createHeader() {
    return new HttpHeaders({ 'Content-Type': 'application/json' });
  }

}

import { Injectable } from '@angular/core';
import { Environment } from '../environment/environment';
import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { RegisterModel } from '../model/register.model';
import { Observable } from 'rxjs/internal/Observable';
import { BehaviorSubject } from 'rxjs';
import { OrderModel } from '../model/order.model';

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private apiOrderAdminUrl = `${Environment.apiBaseUrl}/admin/orders`;
  private apiOrderUrl = `${Environment.apiBaseUrl}/orders`;
  private apiPaymentUrl = `${Environment.apiBaseUrl}/payment/vnpay`;
  private apiConfigUrl = {headers: {'Content-Type': 'application/json'}};

  constructor(private http: HttpClient) {
  }

  findAll(fullname: string, pageSize: number, pageNumber: number, sortDir: string, sortBy: string) {
    let queryParams = new HttpParams();
    queryParams = queryParams.append("fullname", fullname);
    queryParams = queryParams.append("page-size", pageSize);
    queryParams = queryParams.append("page-number", pageNumber);
    queryParams = queryParams.append("sort-direction", sortDir);
    queryParams = queryParams.append("sort-by", sortBy);
    return this.http.get(this.apiOrderAdminUrl, {params: queryParams});
  }

  findAllByCustomer(orderId: number, pageSize: number, pageNumber: number, sortDir: string, sortBy: string) {
    let queryParams = new HttpParams();
    queryParams = queryParams.append("id", orderId.toString());
    queryParams = queryParams.append("page-size", pageSize);
    queryParams = queryParams.append("page-number", pageNumber);
    queryParams = queryParams.append("sort-direction", sortDir);
    queryParams = queryParams.append("sort-by", sortBy);
    return this.http.get(this.apiOrderUrl, {params: queryParams});
  }

  findById(id: number) {
    return this.http.get(`${this.apiOrderAdminUrl}/${id}`);
  }

  findByIdWithClient(id: number) {
    return this.http.get(`${this.apiOrderUrl}/${id}`);
  }

  create(orderModel: OrderModel) {
    const formData = new FormData();
    formData.append('fullname', orderModel.fullname);
    formData.append('address', orderModel.address);
    formData.append('phone', orderModel.phone);
    formData.append('note', orderModel.note);
    formData.append('paymentMethod', orderModel.paymentMethod.toString());
    formData.append('paymentStatus', orderModel.paymentStatus.toString());
    formData.append('orderStatus', orderModel.orderStatus.toString());
    for (let i = 0; i < orderModel.orderDetails.length; i++) {
      formData.append(`orderDetails[${i}].productDetailsId`, orderModel.orderDetails[i].productDetailsId.toString());
      formData.append(`orderDetails[${i}].quantity`, orderModel.orderDetails[i].quantity.toString());
      formData.append(`orderDetails[${i}].price`, orderModel.orderDetails[i].price.toString());
    }

    return this.http.post(this.apiOrderUrl, formData);
  }

  updateOrderStatus(id: number, orderStatus: number, cancelReason: string) {
    const formData = new FormData();
    formData.append('id', id.toString());
    formData.append('orderStatus', orderStatus.toString());
    formData.append('cancelReason', cancelReason);
    return this.http.put(this.apiOrderAdminUrl, formData);

    // return this.http.put(`${this.apiOrderAdminUrl}/${id}/${orderStatus}`, null, this.apiConfigUrl);
  }

  updateOrderStatusClient(id: number, orderStatus: number, cancelReason: string) {
    const formData = new FormData();
    formData.append('id', id.toString());
    formData.append('orderStatus', orderStatus.toString());
    formData.append('cancelReason', cancelReason);
    return this.http.put(this.apiOrderUrl, formData);
  }

  payment(amount: number, orderId: number) {
    const formData = new FormData();
    formData.append('amount', amount.toString());
    formData.append('orderInfo', orderId.toString());
    return this.http.post(`${this.apiPaymentUrl}/create-payment`, formData);
  }

  private createHeader() {
    return new HttpHeaders({ 'Content-Type': 'application/json' });
  }

}

import { Injectable } from '@angular/core';
import { Environment } from '../environment/environment';
import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { RegisterModel } from '../model/register.model';
import { Observable } from 'rxjs/internal/Observable';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CartService {
  private apiUrl = `${Environment.apiBaseUrl}/api/cart`;
  private apiConfig = { headers: this.createHeader() }

  private cartItemCountSubject = new BehaviorSubject<number>(0);
  cartItemCount$ = this.cartItemCountSubject.asObservable();

  constructor(private http: HttpClient) { }

  setCartItemCount(count: number): void {
    this.cartItemCountSubject.next(count);
  }

  addToCart(cart: any) {
    return this.http.post(`${this.apiUrl}`, cart, this.apiConfig);
  }

  updateQuantity(cart: any) {
    return this.http.put(`${this.apiUrl}`, cart, this.apiConfig);
  }

  getCartByAccountEmail(email: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/${email}`);
  }

  deleteCartDetailsById(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }

  private createHeader() {
    return new HttpHeaders({ 'Content-Type': 'application/json' });
  }

}

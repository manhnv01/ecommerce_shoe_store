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
export class AccountService {
  private apiUrl = `${Environment.apiBaseUrl}`;
  private jsonUrl = 'assets/data.json';
  private apiConfig = { headers: this.createHeader() }

  constructor(private http: HttpClient, private tokenService: TokenService) { }

  login(loginModel: LoginModel) {
    return this.http.post(`${this.apiUrl}/login`, loginModel, this.apiConfig);
  }

  logout() {
    this.tokenService.removeToken();
    window.location.href = '/';
  }

  verificationEmailByLink(code: string): Observable<any>  {
    const params = new HttpParams().set('code', code);
    return this.http.get(`${this.apiUrl}/verify`, { params });
  }

  verificationEmailByCode(email: string, code: string): Observable<any>  {
    const requestBody = { code, email };
    console.log(requestBody);

    return this.http.post(`${this.apiUrl}/verification-email-by-code`, requestBody, this.apiConfig);
  }

  reSendVerificationEmailByCode(email: string): Observable<any>  {
    const params = new HttpParams().set('email', email);
    return this.http.get(`${this.apiUrl}/send-verification-email-by-code`, { params});
  }

  private createHeader() {
    return new HttpHeaders({ 'Content-Type': 'application/json' });
  }

  resetPassword(resetPassword: ResetPasswordModel): Observable<any> {
    return this.http.put(`${this.apiUrl}/reset-password`, resetPassword, this.apiConfig);
  }

  reSendForgotPassword(email: string): Observable<any> {
    const params = new HttpParams().set('email', email);
    return this.http.get(`${this.apiUrl}/send-verification-forgot-password-by-code`, { params });
  }

  getJsonDataAddress(): Observable<any> {
    return this.http.get<any>(this.jsonUrl);
  }

  changePassword(changePassword: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/change-password`, changePassword, this.apiConfig);
  }
}

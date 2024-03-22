import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root'
})
export class TokenService {
  private readonly TOKEN_KEY = 'token';

  constructor(
    private jwtHelper: JwtHelperService
    ) {}

  private userRoles: string[] = [];

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  setToken(token: string): void {
    // debugger
    localStorage.setItem(this.TOKEN_KEY, token);
  }

  removeToken(): void {
    localStorage.removeItem(this.TOKEN_KEY);
  }

  getUserRoles(): any {
    // Giải mã token để lấy thông tin về người dùng, bao gồm danh sách các quyền (roles)
    const token = localStorage.getItem(this.TOKEN_KEY);
    const decodedToken = token ? this.jwtHelper.decodeToken(token) : null;
    return decodedToken.roles || [];
  }

  getUserName(): string {
    const token = localStorage.getItem(this.TOKEN_KEY);
    const decodedToken = token ? this.jwtHelper.decodeToken(token) : null;
    return decodedToken.sub;
  }

  isTokenExpired(): boolean {
    const token = localStorage.getItem(this.TOKEN_KEY);
    const decodedToken = token ? this.jwtHelper.decodeToken(token) : null;

    // Thực hiện kiểm tra thời gian hết hạn của token như trong ví dụ trước đó
    const expirationTimeInSeconds = decodedToken.exp;
    const currentTimeInSeconds = Math.floor(Date.now() / 1000);
    
    return expirationTimeInSeconds < currentTimeInSeconds;
  }

  isLogin(): boolean {
    let authToken = localStorage.getItem('token');
    return authToken !== null;
  }

  messageError(messageError: string): string {
    return messageError;
  }
}

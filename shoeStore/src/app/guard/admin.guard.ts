import {ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Injectable} from "@angular/core";
import {TokenService} from "../service/token.service";
import {ToastrService} from "ngx-toastr";

@Injectable({
  providedIn: 'root'
})
export class AdminGuard {
  private readonly TOKEN_KEY = 'token';
  constructor(private tokenService: TokenService, private router: Router, private toastr: ToastrService) {
  }

  canActivate: CanActivateFn = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | UrlTree => {
    // debugger
    const requiredRole = ['ROLE_ADMIN', 'ROLE_EMPLOYEE']; // Quyền truy cập yêu cầu

    if (this.tokenService.isLogin() == false){
      return this.router.createUrlTree(['/login']);
    }
    const roles = this.tokenService.getUserRoles(); // Lấy danh sách các quyền từ AuthService
    // console.log("role:" + roles);
    // debugger
    if (roles == null || roles.length == 0 || this.tokenService.isTokenExpired()) {
      // nếu token hết hạn, chuyển hướng đến trang đăng nhập
      this.toastr.error("Phiên làm việc hết hạn, vui lòng đăng nhập lại");
      return this.router.createUrlTree(['/login']);
    } else if (roles.some((role: string) => requiredRole.includes(role))) {
      // Nếu người dùng có ít nhất một quyền nằm trong danh sách quyền yêu cầu
      return true; // Người dùng có quyền truy cập
    } else {
      // Người dùng không có quyền truy cập, chuyển hướng đến trang access-denied
      return this.router.createUrlTree(['/forbidden']);
    }
  };
}
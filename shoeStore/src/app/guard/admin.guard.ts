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
    const requiredRole = ['ROLE_ADMIN', 'ROLE_EMPLOYEE']; // Quyền truy cập yêu cầu

    if (this.tokenService.isLogin() == false){
      this.toastr.info("Vui lòng đăng nhập để tiếp tục");
      localStorage.setItem('redirectUrl', state.url);
      return this.router.createUrlTree(['/login']);
    }
    const roles = this.tokenService.getUserRoles(); // Lấy danh sách các quyền
    if (roles == null || roles.length == 0 || this.tokenService.isTokenExpired()) {
      // nếu token hết hạn, chuyển hướng đến trang đăng nhập
      localStorage.setItem('redirectUrl', state.url);
      this.toastr.error("Phiên làm việc hết hạn, vui lòng đăng nhập lại");
      this.toastr.info(state.url);
      return this.router.createUrlTree(['/login']);
    } else if (roles.some((role: string) => requiredRole.includes(role))) {
      return true; // Người dùng có quyền truy cập
    } else {
      // Người dùng không có quyền truy cập, chuyển hướng đến trang access-denied
      return this.router.createUrlTree(['/forbidden']);
    }
  };
}
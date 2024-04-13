import {ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Injectable} from "@angular/core";
import {TokenService} from "../service/token.service";
import {ToastrService} from "ngx-toastr";

@Injectable({
  providedIn: 'root'
})
export class UserGuard {
  constructor(private tokenService: TokenService, private router: Router, private toastr: ToastrService) {
  }

  canActivate: CanActivateFn = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | UrlTree => {
    if (!this.tokenService.isLogin()) {
      localStorage.setItem('redirectUrl', state.url);
      // nếu token hết hạn hoặc chưa đăng nhập, chuyển hướng đến trang đăng nhập
      this.toastr.info("Vui lòng đăng nhập để tiếp tục");
      return this.router.createUrlTree(['/login']);
    } else if (this.tokenService.isTokenExpired()) {
      localStorage.setItem('redirectUrl', state.url);
      // nếu token hết hạn, chuyển hướng đến trang đăng nhập
      this.toastr.error("Phiên làm việc hết hạn, vui lòng đăng nhập lại");
      return this.router.createUrlTree(['/login']);
    }
    return true;
  };
}
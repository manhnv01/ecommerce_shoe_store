import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { AccountService } from 'src/app/service/account.service';
import { CategoryService } from 'src/app/service/category.service';
import { CategoryModel } from 'src/app/model/category.model';
import { TokenService } from 'src/app/service/token.service';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CartService } from 'src/app/service/cart.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { CustomerService } from 'src/app/service/customer.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  @ViewChild('oldPassword') oldPassword!: ElementRef;
  @ViewChild('newPassword') newPassword!: ElementRef;
  @ViewChild('btnCloseModal') btnCloseModal!: ElementRef;
  show: boolean = true;

  profile: any;

  isAdmin: boolean = false;
  
  isLogin: boolean = false;
  isTokenExpired: boolean = true;

  totalProductInCart: number = 0;

  categories: CategoryModel[] = [];

  private findAllSubscription: Subscription | undefined;

  email: string = '';

  changePasswordForm: FormGroup = new FormGroup({
    id: new FormControl(null),
    oldPassword: new FormControl('', [Validators.required]),
    newPassword: new FormControl('', [Validators.required, Validators.minLength(6), Validators.maxLength(30)]),
  });

  constructor(
    private accountService: AccountService,
    private tokenService: TokenService,
    private categoryService: CategoryService,
    private customerService: CustomerService,
    private toastr: ToastrService,
    private cartService: CartService,
    private title: Title
  ) { }

  ngOnInit() {
    this.title.setTitle('Trang chủ');

    if (this.tokenService.getToken() !== null) {
      this.isLogin = true;
      this.isTokenExpired = this.tokenService.isTokenExpired();

      if (!this.isTokenExpired) {
        this.getProfile();
        this.email = this.tokenService.getUserName();
      }

      if (!this.tokenService.getUserRoles().includes('ROLE_USER')){
        this.isAdmin = true;
      }

      if (!this.isTokenExpired && this.tokenService.getUserRoles().includes('ROLE_USER')) {
        this.getCartByAccountEmail();
      }
    }

    this.cartService.cartItemCount$.subscribe(count => {
      this.totalProductInCart = count;
    });
  }

  ngOnDestroy(): void {
    if (this.findAllSubscription) {
      this.findAllSubscription.unsubscribe();
    }
  }

  getCategories() {
    this.categoryService.getAll().subscribe({
      next: (response) => {
        console.log(response);
        this.categories = response;
      }
    });
  }

  getCartByAccountEmail() {
    this.findAllSubscription = this.cartService.getCartByAccountEmail(this.tokenService.getUserName()).subscribe({
      next: (response) => {
        this.totalProductInCart = response.totalProduct;
      },
      error: (error) => {
        console.log(error);
      }
    })
  }
  
  getProfile() {
    this.customerService.findByEmail(this.tokenService.getUserName()).subscribe({
      next: (response) => {
        console.log(response);
        this.profile = response;
      },
      error: (error) => {
        console.log(error);
      }
    });
  }


  onChangePassword() {
    this.changePasswordForm.get('id')?.setValue(this.profile.account.id);
    console.log(this.changePasswordForm.value);
    this.accountService.changePassword(this.changePasswordForm.value).subscribe({
      next: (response) => {
        console.log(response);
        this.toastr.success('Đổi mật khẩu thành công');
        this.resetText();
        this.btnCloseModal.nativeElement.click();
      },
      error: (error) => {
        console.log(error);
        if (error.status === 400 && error.error === 'INVALID_PASSWORD') {
          this.toastr.error('Mật khẩu cũ không đúng');
        } else if (error.status === 400 && error.error === 'ACCOUNT_NOT_FOUND') {
          this.toastr.error('Tài khoản không tồn tại');
        } else
          this.toastr.error('Đổi mật khẩu thất bại');
      }
    });
  }

  togglePassword(){
    this.show = !this.show;
    if(this.show){
      this.oldPassword.nativeElement.setAttribute('type', 'password');
      this.newPassword.nativeElement.setAttribute('type', 'password');
    }
    else {
      this.oldPassword.nativeElement.setAttribute('type', 'text');
      this.newPassword.nativeElement.setAttribute('type', 'text');
    }
  }

  resetText() {
    this.changePasswordForm.reset();
  }

  logout(): void {
    this.accountService.logout();
  }
}

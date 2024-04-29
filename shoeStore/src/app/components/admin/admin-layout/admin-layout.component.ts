import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { AccountService } from 'src/app/service/account.service';
import { TokenService } from 'src/app/service/token.service';
import { ToastrService } from 'ngx-toastr';
import { EmployeeService } from 'src/app/service/employee.service';
import { Environment } from 'src/app/environment/environment';
import { ActivatedRoute, Router } from '@angular/router';
import { FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-admin-layout',
  templateUrl: './admin-layout.component.html',
  styleUrls: ['./admin-layout.component.css']
})
export class AdminLayoutComponent implements OnInit {

  @ViewChild('oldPassword') oldPassword!: ElementRef;
  @ViewChild('newPassword') newPassword!: ElementRef;
  @ViewChild('btnCloseModal') btnCloseModal!: ElementRef;
  show: boolean = true;
  
  protected readonly Environment = Environment;
  
  baseUrl: string = `${Environment.apiBaseUrl}`;

  isLogin: boolean = false;
  isTokenExpired: boolean = true;

  employee: any;

  email: string = '';
  requiredRole = ['ROLE_ADMIN', 'ROLE_EMPLOYEE']; // Quyền truy cập yêu cầu
  roles: string[] = [];
  role: string = '';

  constructor(
    private accountService: AccountService,
    private employeeService: EmployeeService,
    private tokenService: TokenService,
    private title: Title,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private toastr: ToastrService
  ) { }

  changePasswordForm: FormGroup = new FormGroup({
    id: new FormControl(null),
    oldPassword: new FormControl('', [Validators.required]),
    newPassword: new FormControl('', [Validators.required, Validators.minLength(6), Validators.maxLength(30)]),
  });
  
  ngOnInit() {
    this.title.setTitle('Admin Shoes Station');
    if (this.tokenService.getToken() !== null) {
      this.isLogin = true;
      this.isTokenExpired = this.tokenService.isTokenExpired();
    }

    if (this.tokenService.getToken() !== null) {
      this.isLogin = true;
      this.isTokenExpired = this.tokenService.isTokenExpired();

      if (!this.isTokenExpired) {
        this.email = this.tokenService.getUserName();
        this.roles = this.tokenService.getUserRoles();
      }

      if (this.isLogin && !this.isTokenExpired && this.roles.some((role: string) => this.requiredRole.includes(role))) {
        this.getUserInfoByEmail();
        if (this.roles.includes('ROLE_ADMIN')) {
          this.role = 'Quản trị viên';
        } else {
          this.role = 'Nhân viên';
        }
      }
    }
  }

  getUserInfoByEmail() {
    this.employeeService.findByEmail(this.email).subscribe({
      next: (response) => {
        this.employee = response;
      },
      error: (err) => {
        console.log(err);
      }
    });
  }

  detail(id: number){
    window.location.href = `/admin/profile/${id}`;
  }

  logout(): void {
    this.accountService.logout();
  }

  onChangePassword() {
    this.changePasswordForm.get('id')?.setValue(this.employee.account.id);
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

}

import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AccountService } from 'src/app/service/account.service';
import { TokenService } from 'src/app/service/token.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  // email không tồn tại
  EMAIL_NOT_FOUND = '';

  @ViewChild('btnSubmit') btnSubmit!: ElementRef;
  @ViewChild('btnSubmitLoading') btnSubmitLoading!: ElementRef;
  @ViewChild('btnCloseModal') btnCloseModal!: ElementRef;

  loginForm: FormGroup = new FormGroup(
    {
      email: new FormControl('', [Validators.required]),
      password: new FormControl('', [Validators.required])
    }
  );

  verifyForm: FormGroup = new FormGroup(
    {
      email: new FormControl('', [Validators.required]),
    }
  );

  constructor(
    private router: Router,
    private title: Title,
    private toastr: ToastrService,
    private accountService: AccountService,
    private tokenService: TokenService) {
  }

  ngOnInit(): void {
    this.title.setTitle("Đăng nhập");
  }

  login(): void {
    this.accountService.login(this.loginForm.value).subscribe({
      next: (response: any) => {
        this.toastr.success('Đăng nhập thành công');
        this.tokenService.setToken(response.token);
        const roles = this.tokenService.getUserRoles();
        const requiredRole = ['ROLE_ADMIN', 'ROLE_EMPLOYEE'];
        if (roles.some((role: string) => requiredRole.includes(role))) {
          window.location.href = "/admin";
        } else {
          window.location.href = "/";
        }
      },
      error: (error: any) => {
        console.log(error);
        if (error.status === 400 && error.error.message === 'ACCOUNT_NOT_FOUND') {
          this.toastr.error('Tài khoản không tồn tại');
        } else if (error.status === 403 && error.error.message === 'INVALID_PASSWORD') {
          this.toastr.error('Mật khẩu không đúng');
        } else if (error.status === 403 && error.error.message === 'ACCOUNT_IS_LOCKED') {
          this.toastr.error('Tài khoản đã bị khóa');
        } else
          this.toastr.error('Đăng nhập thất bại. Vui lòng thử lại sau');
      }
    });
  }

  sendVerify() {
    this.btnSubmit.nativeElement.classList.add('d-none');
    this.btnSubmitLoading.nativeElement.classList.remove('d-none');
    this.accountService.reSendVerificationEmailByCode(this.verifyForm.get('email')?.value).subscribe({
      next: (response) => {
        this.btnCloseModal.nativeElement.click();
        console.log(response);
        localStorage.setItem('email', this.verifyForm.value.email);
        this.toastr.success('Gửi mã xác minh thành công', 'Thông báo');
        this.router.navigate(['/verify']);
      },
      error: (error) => {
        console.log(error);
        if (error.status === 400 && error.error === 'ACCOUNT_NOT_FOUND') {
          this.toastr.error('Tài khoản không tồn tại');
        } else
          this.toastr.error('Gửi mã xác minh thất bại', 'Thông báo');
        this.btnSubmit.nativeElement.classList.remove('d-none');
        this.btnSubmitLoading.nativeElement.classList.add('d-none');
      }
    });
  }
}

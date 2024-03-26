import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { CustomerService } from 'src/app/service/customer.service';
import { ToastrService } from 'ngx-toastr';
import { AccountService } from 'src/app/service/account.service';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent implements OnInit {

  @ViewChild('btnSubmit') btnSubmit!: ElementRef;
  @ViewChild('btnSubmitLoading') btnSubmitLoading!: ElementRef;
  @ViewChild('reSend') reSend!: ElementRef;
  @ViewChild('reSendLoading') reSendLoading!: ElementRef;
  

  resetPasswordForm: FormGroup = new FormGroup({
    code: new FormControl('', [Validators.required, Validators.minLength(6), Validators.maxLength(6), Validators.pattern('^[0-9]*$')]),
    newPassword: new FormControl('', [Validators.required, Validators.minLength(6), Validators.maxLength(30)]),
    email: new FormControl(localStorage.getItem('email') || '')
  });

  constructor(
    private accountService: AccountService,
    private toastr: ToastrService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private title: Title
  ) {
    this.title.setTitle('Đặt lại mật khẩu');
  }

  ngOnInit() {
  }

  onSubmit(): void{
    if (this.resetPasswordForm.invalid) {
      return;
    }
    this.resetPassword();
  }

  reSendForgotPassword(): void {
    this.reSend.nativeElement.classList.add('d-none');
    this.reSendLoading.nativeElement.classList.remove('d-none');
    this.accountService.reSendForgotPassword(localStorage.getItem('email') || '').subscribe({
      next: (response) => {
        console.log(response);
        this.toastr.success('Gửi mã xác minh thành công');
        this.reSend.nativeElement.classList.remove('d-none');
        this.reSendLoading.nativeElement.classList.add('d-none');
      },
      error: (error) => {
        console.log(error);
        this.toastr.error('Gửi mã xác minh thất bại');
        this.reSend.nativeElement.classList.remove('d-none');
        this.reSendLoading.nativeElement.classList.add('d-none');
      }
    });
  }

  resetPassword(): void{
    this.btnSubmit.nativeElement.classList.add('d-none');
    this.btnSubmitLoading.nativeElement.classList.remove('d-none');
    this.accountService.resetPassword(this.resetPasswordForm.value).subscribe({
      next: (response) => {
        this.toastr.success('Đặt lại mật khẩu thành công', 'Thông báo');
        this.router.navigate(['/login']);
      },
      error: (error) => {
        console.log(error);
        this.btnSubmit.nativeElement.classList.remove('d-none');
        this.btnSubmitLoading.nativeElement.classList.add('d-none');
        if (error.status === 400 && error.error === 'THE_VERIFICATION_CODE_HAS_EXPIRED') {
          this.toastr.error('Mã xác minh đã hết hạn.', 'Thông báo');
        } 
        else if (error.status === 400 && error.error === 'INVALID_VERIFICATION_CODE') {
          this.toastr.error('Mã xác minh không chính xác.', 'Thông báo');
        }
        else if (error.status === 400 && error.error === 'ACCOUNT_NOT_FOUND') {
          this.toastr.error('Tài khoản này không tồn tại.', 'Thông báo');
        }
        else {
          this.toastr.error('Lỗi không xác định.', 'Thông báo');
        }
      }
    });
  }
}

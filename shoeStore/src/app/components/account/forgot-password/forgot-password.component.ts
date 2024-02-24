import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AccountService } from 'src/app/service/account.service';
import { CustomerService } from 'src/app/service/customer.service';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent implements OnInit {

  @ViewChild('btnSubmit') btnSubmit!: ElementRef;
  @ViewChild('btnSubmitLoading') btnSubmitLoading!: ElementRef;
  
  constructor(
    private accountService: AccountService,
    private toastr: ToastrService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private title: Title
  ) {
    this.title.setTitle('Quên mật khẩu');
  }

  forgotPasswordForm: FormGroup = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email])
  });

  ngOnInit() {
  }

  onSubmit(): void {
    if (this.forgotPasswordForm.invalid) {
      return;
    }
    this.forgotPassword();
  }

  forgotPassword(): void {
    this.btnSubmit.nativeElement.classList.add('d-none');
    this.btnSubmitLoading.nativeElement.classList.remove('d-none');
    this.accountService.forgotPassword(this.forgotPasswordForm.get('email')?.value).subscribe({
      next: (response) => {
        this.toastr.success('Vui lòng kiểm tra email để đặt lại mật khẩu.');
        localStorage.setItem('email', this.forgotPasswordForm.get('email')?.value);
        this.router.navigate(['/reset-password']);
      },
      error: (error) => {
        this.btnSubmit.nativeElement.classList.remove('d-none');
        this.btnSubmitLoading.nativeElement.classList.add('d-none');
        console.log(error);
        if (error.status === 400 && error.error === 'DOES_NOT_EXIST') {
          this.toastr.error('Không tìm thấy tài khoản có Email này.', 'Thông báo');
        } 
        else {
          this.toastr.error('Lỗi không xác định.', 'Thông báo');
        }
      }
    });
  }
}

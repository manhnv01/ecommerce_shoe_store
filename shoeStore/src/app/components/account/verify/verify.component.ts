import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Toast, ToastrService } from 'ngx-toastr';
import { CodeInputComponent } from 'angular-code-input';
import { Title } from '@angular/platform-browser';
import { AccountService } from 'src/app/service/account.service';

@Component({
  selector: 'app-verify',
  templateUrl: './verify.component.html',
  styleUrls: ['./verify.component.css']
})
export class VerifyComponent implements OnInit {
  @ViewChild('codeInput') codeInput !: CodeInputComponent;
  @ViewChild('reSend') reSend!: ElementRef;
  @ViewChild('reSendLoading') reSendLoading!: ElementRef;

  email: string = '';
  ERROR_VERIFICATION_CODE: string = '';

  constructor(
    private accountService: AccountService,
    private toastr: ToastrService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private title: Title
  ) {
    this.title.setTitle('Xác minh tài khoản');
  }

  ngOnInit() {
    this.email = localStorage.getItem('email') || '';
  }

  onCodeChanged(code: string) { }

  onCodeCompleted(code: string) {
    this.verify(this.email, code);
    this.codeInput.reset();
  }

  verify(email: string, code: string): void {
    this.accountService.verificationEmailByCode(email, code).subscribe({
      next: (response) => {
        console.log(response);
        this.toastr.success('Xác minh thành công');
        this.router.navigate(['/login']);
      },
      error: (error) => {
        console.log(error);
        if (error.status === 400 && error.error === 'THE_VERIFICATION_CODE_HAS_EXPIRED') {
          this.ERROR_VERIFICATION_CODE = 'Mã xác minh đã hết hạn.';
        }
        else if (error.status === 400 && error.error === 'INVALID_VERIFICATION_CODE') {
          this.ERROR_VERIFICATION_CODE = 'Mã xác minh không chính xác.';
        }
        else if (error.status === 400 && error.error === 'ACCOUNT_ALREADY_VERIFIED') {
          this.ERROR_VERIFICATION_CODE = 'Tài khoản đã được xác minh.';
        }
        else if (error.status === 400 && error.error === 'DOES_NOT_EXIST') {
          this.ERROR_VERIFICATION_CODE = 'Tài khoản này không tồn tại.';
        }
        else {
          this.ERROR_VERIFICATION_CODE = 'Lỗi không xác định.';
        }
      }
    });
  }

  reSendVerificationEmailByCode(): void {
    this.reSend.nativeElement.classList.add('d-none');
    this.reSendLoading.nativeElement.classList.remove('d-none');
    this.accountService.reSendVerificationEmailByCode(this.email).subscribe({
      next: (response) => {
        console.log(response);
        this.toastr.success('Gửi mã xác minh thành công', 'Thông báo');
        this.reSend.nativeElement.classList.remove('d-none');
        this.reSendLoading.nativeElement.classList.add('d-none');
      },
      error: (error) => {
        console.log(error);
        this.toastr.error('Gửi mã xác minh thất bại', 'Thông báo');
        this.reSend.nativeElement.classList.remove('d-none');
        this.reSendLoading.nativeElement.classList.add('d-none');
      }
    });
  }
}

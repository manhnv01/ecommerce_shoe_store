import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Toast, ToastrService } from 'ngx-toastr';
import { CustomerService } from 'src/app/service/customer.service';
import { CodeInputComponent } from 'angular-code-input';

@Component({
  selector: 'app-verify',
  templateUrl: './verify.component.html',
  styleUrls: ['./verify.component.css']
})
export class VerifyComponent implements OnInit {
  @ViewChild ( 'codeInput' ) codeInput ! : CodeInputComponent ; 

  email: string = '';
  ERROR_VERIFICATION_CODE: string = '';

  constructor(private customerService: CustomerService,
    private activatedRoute: ActivatedRoute,
    private toastr: ToastrService,
    private router: Router) { }

  ngOnInit() {
    // const code2 = this.activatedRoute.snapshot.queryParams['code'];
    // this.activatedRoute.queryParams.subscribe((params) => {
    //   const { 'code': code = code2 } = params;

    //   this.verify(code);
    // });
    this.activatedRoute.queryParams.subscribe(params => {
      this.email = params['email'];
    });
  }

  onCodeChanged(code: string) {}

  onCodeCompleted(code: string) {
    this.verify(this.email, code);
    this.codeInput.reset();
  }

  verify(email: string, code: string): void {
    this.customerService.verificationEmailByCode(email, code).subscribe({
      next: (response) => {
        console.log(response);
        this.toastr.success('xác minh thành công');
        this.router.navigate(['/login']);
      },
      error: (error) => {
        console.log(error);
        this.toastr.error('xác minh thất bại');
        if (error.status === 400 && error.error === 'THE_VERIFICATION_CODE_HAS_EXPIRED') {
          this.ERROR_VERIFICATION_CODE = 'Mã xác minh đã hết hạn.';
        } 
        else if (error.status === 400 && error.error === 'INVALID_VERIFICATION_CODE') {
          this.ERROR_VERIFICATION_CODE = 'Mã xác minh không chính xác.';
        }
        else {
          
        }
      }
    });
  }

  sendVerificationEmailByCode(): void {
    this.customerService.sendVerificationEmailByCode(this.email).subscribe({
      next: (response) => {
        console.log(response);
        this.toastr.success('Gửi mã xác minh thành công');
      },
      error: (error) => {
        console.log(error);
        this.toastr.error('Gửi mã xác minh thất bại');
      }
    });
  }
}

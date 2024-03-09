import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, ValidationErrors, Validators } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { CustomerService } from 'src/app/service/customer.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  @ViewChild('btnSubmit') btnSubmit!: ElementRef;
  @ViewChild('btnSubmitLoading') btnSubmitLoading!: ElementRef;

  duplicateEmail: string = '';

  registerForm: FormGroup = new FormGroup({
    name: new FormControl('', [Validators.required, Validators.minLength(2), Validators.maxLength(30)]),
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required, Validators.minLength(6)]),
    confirmPassword: new FormControl('', [Validators.required]),
  },
    {
      validators: this.confirmPasswordValidator
    });

  constructor(
    private customerService: CustomerService,
    private toastr: ToastrService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private title: Title
  ) {
    this.title.setTitle('Đăng ký');
  }

  ngOnInit() {
  }

  onSubmit(): void {
    if (this.registerForm.invalid) {
      return;
    }
    this.register();
  }

  register(): void {
    this.btnSubmit.nativeElement.classList.add('d-none');
    this.btnSubmitLoading.nativeElement.classList.remove('d-none');
    this.customerService.register(this.registerForm.value).subscribe({
      next: (response) => {
        this.toastr.success('Đăng ký thành công');
        localStorage.setItem('email', this.registerForm.value.email);
        this.router.navigate(['/verify']);
      },
      error: (error) => {
        console.log(error);
        this.btnSubmit.nativeElement.classList.remove('d-none');
        this.btnSubmitLoading.nativeElement.classList.add('d-none');
          if (error.status === 400 && error.error === 'DUPLICATE_EMAIL')
            this.duplicateEmail = 'Email này đã được sử dụng.';
          else {
            this.toastr.error('Lỗi không xác định.', 'Thông báo');
          }
      }
    });
  }

  confirmPasswordValidator(control: AbstractControl): ValidationErrors | null {
    const password = control.get('password')?.value;
    const confirmPassword = control.get('confirmPassword')?.value;

    if (password !== confirmPassword) {
      return { passwordMismatch: true };
    }

    return null;
  }
}

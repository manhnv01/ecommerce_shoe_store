import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { CustomerService } from 'src/app/service/customer.service'

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  registerForm: FormGroup = new FormGroup({
    name: new FormControl('', [Validators.required, Validators.minLength(2), Validators.maxLength(50)]),
    email: new FormControl('', [Validators.required, Validators.minLength(2), Validators.maxLength(50), Validators.email]),
    password: new FormControl('', [Validators.required, Validators.minLength(6)]),
    confirmPassword: new FormControl('', [Validators.required, Validators.minLength(6)]),
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

  onSubmit(): void{
    if(this.registerForm.invalid){
      return;
    }
    this.register();
    }

    register(): void {
      this.customerService.register(this.registerForm.value).subscribe({
        next: (response) => {
          this.toastr.success('Đăng ký thành công');
          this.router.navigate(['/login']);
        },
        error: (err) => {
          console.log(err.error.message);
        }
      });
    }
}

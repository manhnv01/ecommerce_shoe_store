import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
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

  loginForm: FormGroup = new FormGroup(
    {
      email: new FormControl(''),
      password: new FormControl('')
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
        this.tokenService.setToken(response.token);
        const roles = this.tokenService.getUserRoles();
        const requiredRole = ['ROLE_ADMIN', 'ROLE_STAFF'];
        if (roles.some((role: string) => requiredRole.includes(role))) {
          window.location.href = "/admin";
        } else {
          window.location.href = "/";
        }
        this.toastr.success('Đăng nhập thành công', 'Thông báo');
      },
      error: (error: any) => {
        this.toastr.error(error.error, 'Thông báo');
      }
    });
  }
}

import { Component, OnInit } from '@angular/core';
import { DomSanitizer, Title } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';
import { Environment } from 'src/app/environment/environment';
import { EmployeeService } from 'src/app/service/employee.service';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { TokenService } from 'src/app/service/token.service';

@Component({
  selector: 'app-detail-employee',
  templateUrl: './detail-employee.component.html',
  styleUrls: ['./detail-employee.component.css']
})
export class DetailEmployeeComponent implements OnInit {
  protected readonly Environment = Environment;

  baseUrl: string = `${Environment.apiBaseUrl}`;
  titleString = '';
  employee: any;

  employeeId: number = 0;

  isProfile: boolean = false;

  isAdmin: boolean = false;

  totalQuantity: number = 0;

  constructor(private employeeService: EmployeeService, private title: Title, private activatedRoute: ActivatedRoute,
    private toastr: ToastrService, private router: Router, private tokenService: TokenService,) {
  }

  ngOnInit(): void {
    if (this.tokenService.getToken() !== null
      && this.tokenService.isTokenExpired() === false
      && this.tokenService.getUserRoles().includes('ROLE_ADMIN')) {
      this.isAdmin = true;
    }

    this.getEmployeeByEmail(this.tokenService.getUserName());
  }

  update(id: number) {
    if (id === 1111111111111) {
      this.toastr.warning('Không được phép', 'Thông báo');
      return;
    }

    this.router.navigate(['admin/employee/save', id]);
  }

  getEmployeeById(id: number) {
    this.employeeService.findById(id).subscribe({
      next: (data: any) => {
        this.employee = data;
        this.titleString = this.employee.name;
        this.title.setTitle(this.titleString);
      },
      error: (error: any) => {
        console.log(error);
        if (error.status === 400 && error.error === 'EMPLOYEE_NOT_FOUND') {
          this.toastr.error('Không tìm thấy nhân viên này');
          this.router.navigateByUrl('/admin/customers');
        }
      }
    });
  }

  // Lấy thông tin nhân viên theo email
  getEmployeeByEmail(email: string) {
    this.employeeService.findByEmail(email).subscribe({
      next: (response: any) => {
        const id = this.activatedRoute.snapshot.params["id"]
        this.employeeId = response.id;
        if (response.id == id) {
          this.isProfile = true;
        }

        if (this.isAdmin || this.employeeId == id)
          this.getEmployeeById(id);
        else
          // chuyển hướng sang trang không được phép
          this.router.navigateByUrl('/forbidden');
        console.log(this.employeeId);
      },
      error: (error: any) => {
        console.log(error);
      }
    });
  }
}


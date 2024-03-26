import { Component, OnInit } from '@angular/core';
import { DomSanitizer, Title } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';
import { Environment } from 'src/app/environment/environment';
import { EmployeeService } from 'src/app/service/employee.service';

@Component({
  selector: 'app-detail-employee',
  templateUrl: './detail-employee.component.html',
  styleUrls: ['./detail-employee.component.css']
})
export class DetailEmployeeComponent implements OnInit {
  protected readonly Environment = Environment;
  titleString = '';
  employee: any;

  totalQuantity: number = 0;

  constructor(private employeeService: EmployeeService, private title: Title, private activatedRoute: ActivatedRoute,
              private sanitizer: DomSanitizer) {
  }

  ngOnInit(): void {
    this.getEmployeeById(this.activatedRoute.snapshot.params["id"]);
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
      }
    });
  }
}


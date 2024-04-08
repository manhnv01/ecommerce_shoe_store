import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Title } from '@angular/platform-browser';
import { Environment } from 'src/app/environment/environment';
import { CustomerService } from 'src/app/service/customer.service';
import { ToastrService } from 'ngx-toastr';
import { TokenService } from 'src/app/service/token.service';

@Component({
  selector: 'app-detail-customer',
  templateUrl: './detail-customer.component.html',
  styleUrls: ['./detail-customer.component.css']
})
export class DetailCustomerComponent implements OnInit {
  protected readonly Environment = Environment;
  
  baseUrl: string = `${Environment.apiBaseUrl}`;
  titleString = '';
  customer: any;

  constructor(private customerService: CustomerService, private title: Title, private activatedRoute: ActivatedRoute,
              private toastr: ToastrService, private router: Router, private tokenService: TokenService,) {
  }

  ngOnInit(): void {
    this.getCustomerById(this.activatedRoute.snapshot.params["id"]);
  }

  getCustomerById(id: number) {
    this.customerService.findById(id).subscribe({
      next: (data: any) => {
        this.customer = data;
        this.titleString = this.customer.name;
        this.title.setTitle(this.titleString);
      },
      error: (error: any) => {
        console.log(error);
      }
    });
  }
}

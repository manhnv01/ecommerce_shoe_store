import { Component, OnInit } from '@angular/core';
import { AccountService } from 'src/app/service/account.service';
import { CustomerService } from 'src/app/service/customer.service';
import { Title } from '@angular/platform-browser';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  cities: any;
  districts: any;
  wards: any;

  constructor(
    private accountService: AccountService,
    private title: Title,
    private customerService: CustomerService,
    private toastr: ToastrService,
  ) { }

  ngOnInit() {
    this.title.setTitle('Cá nhân');
    this.getJsonDataAddress();
  }

  getJsonDataAddress() {
    this.accountService.getJsonDataAddress().subscribe({
      next: (response) => {
        console.log(response);
      },
      error: (error) => {
        console.log(error);
      }
    });
  }
}

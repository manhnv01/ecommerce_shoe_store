import { Component, OnInit } from '@angular/core';
import { Environment } from 'src/app/environment/environment';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { OrderService } from 'src/app/service/order.service';
import { OrderModel } from 'src/app/model/order.model';

@Component({
  selector: 'app-user-order-detail',
  templateUrl: './user-order-detail.component.html',
  styleUrls: ['./user-order-detail.component.css']
})
export class UserOrderDetailComponent implements OnInit {

  order: any;
  baseUrl: string = `${Environment.apiBaseUrl}`;
  constructor(private title: Title, private toastr: ToastrService,
    private router: Router, private activatedRoute: ActivatedRoute,
    private orderService: OrderService) {
  }

  ngOnInit(): void {
    this.title.setTitle('Chi tiết đơn hàng');
    this.findById();
  }

  findById() {
    this.orderService.findByIdWithClient(this.activatedRoute.snapshot.params["id"]).subscribe({
      next: (data: any) => {
        this.order = data;
        console.log("data", data);
        console.log(this.order);
        this.order.totalMoney = 0;
        this.order.orderDetails.forEach((orderDetails: any) => {
          orderDetails.totalMoney = orderDetails.quantity * orderDetails.price;
          this.order.totalMoney += orderDetails.totalMoney;
        });
      }
      ,
      error: (error: any) => {
        if (error.status == 404) {
          this.router.navigateByUrl('/don-hang').then();
          this.toastr.error(error.error);
        } else {
          this.toastr.error('Lỗi thực hiện, vui lòng thử lại sau');
        }
      }
    })
  }

}

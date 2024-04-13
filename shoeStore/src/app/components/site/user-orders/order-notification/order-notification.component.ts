import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { OrderService } from 'src/app/service/order.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-order-notification',
  templateUrl: './order-notification.component.html',
  styleUrls: ['./order-notification.component.css']
})
export class OrderNotificationComponent implements OnInit {

  orderId: number = 0;

  isSuccessful: boolean = true;

  titleNotification: string = 'Đặt hàng thành công';
  descriptionNotification: string = 'Cảm ơn bạn đã lựa chọn mua hàng tại Shoes Station.';

  constructor(
    private orderService: OrderService,
    private title: Title,
    private router: Router,
    private toastr: ToastrService,
    private activatedRoute: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.title.setTitle('Đặt hàng thành công');
    this.orderId = this.activatedRoute.snapshot.params['id'];
    this.findOrderById();
  }

  // orderStatus:0 Trạng thái đơn hàng
  // paymentMethod:1 Phương thức thanh toán
  // paymentStatus:true Trạng thái thanh toán

  findOrderById() {
    this.orderService.findByIdWithClient(this.activatedRoute.snapshot.params["id"]).subscribe({
      next: (data: any) => {
        if (data.paymentMethod === 1 && data.paymentStatus === true) {
          this.isSuccessful = true;
          this.titleNotification = 'Thanh toán thành công';
        } else if (data.paymentMethod === 1 && data.paymentStatus === false) {
          this.isSuccessful = false;
          this.titleNotification = 'Thanh toán thất bại';
          this.descriptionNotification = 'Thanh toán thất bại, vui lòng thử lại sau.';
        }
      }
      ,
      error: (error: any) => {
        if (error.status == 404) {
          console.log(error.error);
        } else {
          this.toastr.error('Lỗi thực hiện, vui lòng thử lại sau');
        }
      }
    })
  }
}

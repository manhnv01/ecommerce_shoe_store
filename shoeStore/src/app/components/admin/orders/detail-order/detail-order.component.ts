import { Component, OnInit } from '@angular/core';
import { Environment } from 'src/app/environment/environment';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { OrderService } from 'src/app/service/order.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-detail-order',
  templateUrl: './detail-order.component.html',
  styleUrls: ['./detail-order.component.css']
})
export class DetailOrderComponent implements OnInit {

  order: any;
  totalMoney: number = 0; // Tổng tiền hàng
  totalPrincipal: number = 0; // Tổng tiền hàng gốc
  totalPrice: number = 0; // Tổng tiền hàng sau khi giảm giá
  baseUrl: string = `${Environment.apiBaseUrl}`;
  constructor(private title: Title, private toastr: ToastrService,
    private router: Router, private activatedRoute: ActivatedRoute,
    private orderService: OrderService) {
  }

  updateOrderStatusForm: FormGroup = new FormGroup({
    id: new FormControl(null, [Validators.required]),
    orderStatus: new FormControl(null, [Validators.required]),
    cancelReason: new FormControl(''),
  }
);

  ngOnInit(): void {
    this.title.setTitle('Chi tiết đơn hàng');
    this.findById();
    this.startClock();
  }

  findById() {
    this.orderService.findByIdWithClient(this.activatedRoute.snapshot.params["id"]).subscribe({
      next: (data: any) => {
        this.order = data;
        this.updateOrderStatusForm.patchValue(data);
        //console.log(this.order);
      }
      ,
      error: (error: any) => {
        if (error.status == 400 && error.error == 'ORDER_NOT_FOUND') {
          this.toastr.error('Đơn hàng không tồn tại');
        } else {
          this.toastr.error('Lỗi thực hiện, vui lòng thử lại sau');
        }
      }
    })
  }

  resetForm() {
    this.updateOrderStatusForm.reset();
    this.updateOrderStatusForm.patchValue(this.order);
  }

  updateOrderStatus() {
    console.log(this.updateOrderStatusForm.value);
    const orderStatus = this.updateOrderStatusForm.get('orderStatus')?.value;
    const cancelReason = this.updateOrderStatusForm.get('cancelReason')?.value;

    console.log(orderStatus);

    // this.orderService.updateOrderStatus(this.order.id, orderStatus, cancelReason).subscribe({
    //   next: (data: any) => {
    //     this.toastr.success('Cập nhật trạng thái đơn hàng thành công');
    //     this.findById();
    //   },
    //   error: (error: any) => {
    //     this.toastr.error('Cập nhật trạng thái đơn hàng thất bại');
    //   }
    // })
  }


  startClock(): void {
    setInterval(() => {
      this.getCurrentTime();
    }, 1000); // Gọi hàm time() mỗi giây (1000 milliseconds)
  }

  getCurrentTime(): string {
    const today = new Date();
    const weekday = ["Chủ Nhật", "Thứ Hai", "Thứ Ba", "Thứ Tư", "Thứ Năm", "Thứ Sáu", "Thứ Bảy"];
    const day = weekday[today.getDay()];
    let dd: any = today.getDate();
    let mm: any = today.getMonth() + 1;
    const yyyy = today.getFullYear();
    let h: any = today.getHours();
    let m: any = today.getMinutes();
    let s: any = today.getSeconds();
    m = this.checkTime(m);
    s = this.checkTime(s);
    let nowTime = h + " giờ " + m + " phút " + s + " giây";
    if (dd < 10) {
      dd = '0' + dd;
    }
    if (mm < 10) {
      mm = '0' + mm;
    }
    const currentDate = day + ', ' + dd + '/' + mm + '/' + yyyy;
    return currentDate + ' | ' + nowTime;
  }

  private checkTime(i: number): string {
    if (i < 10) {
      return "0" + i;
    }
    return i.toString();
  }

}

import { Component, OnInit, ViewChild } from '@angular/core';
import { Environment } from 'src/app/environment/environment';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { OrderService } from 'src/app/service/order.service';
import { OrderModel } from 'src/app/model/order.model';
import { FormGroup } from '@angular/forms';
import { FormControl } from '@angular/forms';
import { Validators } from '@angular/forms';
import { ElementRef } from '@angular/core';

@Component({
  selector: 'app-user-order-detail',
  templateUrl: './user-order-detail.component.html',
  styleUrls: ['./user-order-detail.component.css']
})
export class UserOrderDetailComponent implements OnInit {

  @ViewChild('btnCloseModal') btnCloseModal!: ElementRef;

  order: any;
  totalMoney: number = 0; // Tổng tiền hàng
  totalPrincipal: number = 0; // Tổng tiền hàng gốc
  totalPrice: number = 0; // Tổng tiền hàng sau khi giảm giá
  baseUrl: string = `${Environment.apiBaseUrl}`;
  constructor(private title: Title, private toastr: ToastrService,
    private router: Router, private activatedRoute: ActivatedRoute,
    private orderService: OrderService) {
  }

  cancelOrderForm: FormGroup = new FormGroup({
    id: new FormControl(null),
    orderStatus: new FormControl(4),
    cancelReason: new FormControl(null, [Validators.required])
  });

  ngOnInit(): void {
    this.title.setTitle('Chi tiết đơn hàng');
    this.findById();
  }

  findById() {
    this.orderService.findByIdWithClient(this.activatedRoute.snapshot.params["id"]).subscribe({
      next: (data: any) => {
        this.order = data;
        console.log(this.order);
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
    this.cancelOrderForm.reset();
  }

  cancelOrder() {
    this.cancelOrderForm.controls['id'].setValue(this.order.id);
    console.log(this.cancelOrderForm.value);
    this.orderService.updateOrderStatus(this.cancelOrderForm.value).subscribe({
      next: (data: any) => {
        this.toastr.success('Hủy đơn hàng thành công');
        this.btnCloseModal.nativeElement.click();
        this.findById();
      },
      error: (error: any) => {
        if (error.status == 400 && error.error == 'ORDER_NOT_FOUND') {
          this.toastr.error('Đơn hàng không tồn tại.');
        } else {
          this.toastr.error('Lỗi không xác định.');
        }
      }
    });
  }

  repayment(){
    this.orderService.payment(this.order?.totalMoney + this.order?.total_fee - this.order?.totalDiscount , this.order?.id).subscribe({
      next: (data: any) => {
        window.location.href = data.redirectUrl;
      },
      error: (error: any) => {
        console.log(error);
        this.toastr.error('Lỗi không xác định.');
      }
    });
  }
}

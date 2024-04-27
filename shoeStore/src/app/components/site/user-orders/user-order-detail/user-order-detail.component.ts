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
  @ViewChild('textAreaOrtherReason') textAreaOrtherReason!: ElementRef;

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
    cancelReason: new FormControl(null, [Validators.required, Validators.maxLength(100)]),
    typeReason: new FormControl(null)
  });

  ngOnInit(): void {
    this.title.setTitle('Chi tiết đơn hàng');
    this.findById();
  }

  onRadioChangeReason(event: any) {
    this.cancelOrderForm.controls['typeReason'].setValue(event.target.value);
    if (event.target.value == 'otherReason') {
      this.textAreaOrtherReason.nativeElement.classList.remove('d-none');
      this.textAreaOrtherReason.nativeElement.focus();
      // nếu chọn textArea có giá trị thì set giá trị cho cancelReason là giá trị của textArea
      if (this.textAreaOrtherReason.nativeElement.value) {
        this.cancelOrderForm.controls['cancelReason'].setValue(this.textAreaOrtherReason.nativeElement.value);
      }
      else {
        this.cancelOrderForm.controls['cancelReason'].setValue('Lý do khác');
      }
    }
    else {
      this.textAreaOrtherReason.nativeElement.classList.add('d-none');
      this.cancelOrderForm.controls['cancelReason'].setValue(event.target.value);
    }
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

  inputOtherReason(event: any) {
    if (event.target.value) {
      this.cancelOrderForm.controls['cancelReason'].setValue(event.target.value);
    }
    else {
      this.cancelOrderForm.controls['cancelReason'].setValue('Lý do khác');
    }
  }

  resetForm() {
    this.cancelOrderForm.reset();
  }

  cancelOrder() {
    this.cancelOrderForm.controls['id'].setValue(this.order.id);
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

  repayment() {
    this.orderService.payment(this.order?.totalMoney + this.order?.total_fee - this.order?.totalDiscount, this.order?.id).subscribe({
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

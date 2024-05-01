import { Component, OnInit } from '@angular/core';
import { Environment } from 'src/app/environment/environment';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { OrderService } from 'src/app/service/order.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ElementRef, ViewChild } from '@angular/core';
import { ReportService } from 'src/app/service/report.service';

@Component({
  selector: 'app-detail-order',
  templateUrl: './detail-order.component.html',
  styleUrls: ['./detail-order.component.css']
})
export class DetailOrderComponent implements OnInit {
  @ViewChild('btnCloseModal') btnCloseModal!: ElementRef;

  isCancel: boolean = false;

  order: any;
  totalMoney: number = 0; // Tổng tiền hàng
  totalPrincipal: number = 0; // Tổng tiền hàng gốc
  totalPrice: number = 0; // Tổng tiền hàng sau khi giảm giá
  baseUrl: string = `${Environment.apiBaseUrl}`;
  constructor(private title: Title, private toastr: ToastrService,
    private router: Router, private activatedRoute: ActivatedRoute,
    private reportService: ReportService,
    private orderService: OrderService) {
  }

  updateOrderStatusForm: FormGroup = new FormGroup({
    id: new FormControl(null),
    orderStatus: new FormControl(null, [Validators.required]),
    cancelReason: new FormControl(''),
  }
  );

  ngOnInit(): void {
    this.title.setTitle('Chi tiết đơn hàng');
    this.findById();
    this.startClock();

    this.updateOrderStatusForm.get('orderStatus')?.valueChanges.subscribe((orderStatus: number) => {
      if (orderStatus == 4) {
        this.updateOrderStatusForm.get('cancelReason')?.setValidators([Validators.required]);
        this.isCancel = true;
      } else {
        this.updateOrderStatusForm.get('cancelReason')?.clearValidators();
        this.isCancel = false;
      }
      this.updateOrderStatusForm.get('cancelReason')?.updateValueAndValidity();
    });
  }

  findById() {
    this.orderService.findByIdWithClient(this.activatedRoute.snapshot.params["id"]).subscribe({
      next: (data: any) => {
        this.order = data;
      }
      ,
      error: (error: any) => {
        if (error.status == 400 && error.error == 'ORDER_NOT_FOUND') {
          this.toastr.error('Đơn hàng không tồn tại', 'Thông báo');
        } else {
          this.toastr.error('Lỗi thực hiện, vui lòng thử lại sau', 'Thông báo');
        }
      }
    })
  }

  resetForm() {
    this.updateOrderStatusForm.reset();
    this.updateOrderStatusForm.patchValue(this.order);
  }

  updateOrderStatus() {
    this.updateOrderStatusForm.patchValue({ id: this.order.id });
    this.orderService.updateOrderStatus(this.updateOrderStatusForm.value).subscribe({
      next: (data: any) => {
        this.toastr.success('Cập nhật trạng thái đơn hàng thành công','Thông báo');
        this.findById();
        this.btnCloseModal.nativeElement.click();
      },
      error: (error: any) => {
        console.log(error);
        if (error.status == 400 && error.error == 'ORDER_NOT_FOUND') {
          this.toastr.error('Đơn hàng không tồn tại', 'Thông báo');
        } else if (error.status == 400 && error.error == 'ORDER_COMPLETED_CANNOT_UPDATE') {
          this.toastr.error('Đơn hàng đã hoàn thành không thể cập nhật trạng thái', 'Thông báo');
        } else if (error.status == 400 && error.error == 'ORDER_CANCELLED_CANNOT_UPDATE') {
          this.toastr.error('Đơn hàng đã hủy không thể cập nhật trạng thái', 'Thông báo');
        } else if (error.status == 400 && error.error == 'ORDER_RETURNED_CANNOT_UPDATE') {
          this.toastr.error('Đơn hàng đã trả hàng không thể cập nhật trạng thái', 'Thông báo');
        }
        else {
          this.toastr.error('Lỗi không xác định', 'Thông báo');
        }
      }
    })
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

  goEmployeeDetail(id: number) {
    this.router.navigate(['/admin/employee', id]);
  }

   /////////////////////// Xem online /////////////////////////

   exportInvoicePdf() {
    this.reportService.exportInvoicePdf(this.order?.id).subscribe({
      next: (data: any) => {
        const blob = new Blob([data], { type: 'application/pdf' });
        const url = window.URL.createObjectURL(blob);
        window.open(url);
      },
      error: (error: any) => {
        console.log(error);
        this.toastr.error('Lỗi không xác định');
      }
    });
  }
}

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { ReceiptModel } from 'src/app/model/receipt.model';
import { PaginationModel } from 'src/app/model/pagination.model';
import { ReceiptService } from 'src/app/service/receipt.service';
import { Title } from '@angular/platform-browser';
import { ToastrService } from 'ngx-toastr';
import { ReportService } from 'src/app/service/report.service';


@Component({
  selector: 'app-report-order',
  templateUrl: './report-order.component.html',
  styleUrls: ['./report-order.component.css']
})
export class ReportOrderComponent implements OnInit {
  yearNow: number = new Date().getFullYear();

  chooseYear: number = new Date().getFullYear();
  dataDetail: any[] = [];
  years: number[] = [];

  detailYear: number = 0;
  detailMonth: number = 0;

  dataReport: any[] = [];

  constructor(
    private receiptService: ReceiptService,
    private toastr: ToastrService,
    private activatedRoute: ActivatedRoute,
    private reportService: ReportService,
    private router: Router,
    private title: Title
  ) {
    this.title.setTitle('Báo cáo bán hàng');
  }

  ngOnInit(): void {
    this.createYears();
    this.getOrderReport();
  }

  clearAllParams(): void {
    const navigationExtras: NavigationExtras = {
      queryParams: {},
    };
    this.router.navigate([], navigationExtras);
  }

  onYearSelect(event: any) {
    this.chooseYear = event.target.value;
    this.getOrderReport();
  }

  createYears() {
    // Tạo 5 năm trước
    for (let i = 5; i > 0; i--) {
      this.years.push(this.yearNow - i);
    }

    // Thêm năm hiện tại
    this.years.push(this.yearNow);

    // Tạo 5 năm sau
    for (let i = 1; i <= 5; i++) {
      this.years.push(this.yearNow + i);
    }
  }

  getDetail(month: number, year: number) {
    // lấy danh sách sản phẩm có tháng và năm trong dataReport
    let data = this.dataReport.filter((item) => {
      if (item.month == month && item.year == year) {
        this.detailYear = year;
        this.detailMonth = month;
        return this.dataDetail = item.products;
      }
    });
  }

  getOrderReport() {
    this.reportService.orderReport(this.chooseYear).subscribe({
      next: (data: any) => {
        console.log(data);
        this.dataReport = data;
      },
      error: (error: any) => {
        console.log(error);
        this.toastr.error('Lỗi tải dữ liệu, vui lòng thử lại sau', 'Thông báo');
      }
    });
  }

  exportOrderReport() {
    this.reportService.exportOrderReport(this.chooseYear).subscribe({
      next: (response: any) => {
        let date = new Date();
        let formatMonth = date.getMonth().toString().length == 1 ? '0' + (date.getMonth() + 1) : date.getMonth();
        let currentDateString = date.getDate() + '-' + formatMonth + '-' + date.getFullYear();
        const filename = 'export-order_' + currentDateString + '.xlsx';
        const url = window.URL.createObjectURL(response);
        const a = document.createElement('a');
        document.body.appendChild(a);
        a.href = url;
        a.download = filename;
        a.click();
        window.URL.revokeObjectURL(url);
      }
    });
  }
}

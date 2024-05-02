import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router, NavigationExtras } from '@angular/router';
import { PaginationModel } from 'src/app/model/pagination.model';
import { ReceiptModel } from 'src/app/model/receipt.model';
import { ReceiptService } from 'src/app/service/receipt.service';
import { Subscription } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { ReportService } from 'src/app/service/report.service';

@Component({
  selector: 'app-report-receipt',
  templateUrl: './report-receipt.component.html',
  styleUrls: ['./report-receipt.component.css']
})
export class ReportReceiptComponent implements OnInit {
  yearNow: number = new Date().getFullYear();

  chooseYear: number = new Date().getFullYear();
  years: number[] = [];

  dataReport: any[] = [];

  constructor(
    private receiptService: ReceiptService,
    private toastr: ToastrService,
    private activatedRoute: ActivatedRoute,
    private reportService: ReportService,
    private router: Router,
    private title: Title
  ) {
    this.title.setTitle('Báo cáo nhập hàng');
  }

  ngOnInit(): void {
    this.createYears();
    this.getReceiptReport();
  }

  clearAllParams(): void {
    const navigationExtras: NavigationExtras = {
      queryParams: {},
    };
    this.router.navigate([], navigationExtras);
  }

  onYearSelect(event: any) {
    this.chooseYear = event.target.value;
    this.getReceiptReport();
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

  getReceiptReport() {
    this.reportService.receiptReport(this.chooseYear).subscribe({
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

  exportReceiptReport() {
    this.reportService.exportReceiptReport(this.chooseYear).subscribe({
      next: (response: any) => {
        let date = new Date();
        let formatMonth = date.getMonth().toString().length == 1 ? '0' + (date.getMonth() + 1) : date.getMonth();
        let currentDateString = date.getDate() + '-' + formatMonth + '-' + date.getFullYear();
        const filename = 'export-receipt_' + currentDateString + '.xlsx';
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
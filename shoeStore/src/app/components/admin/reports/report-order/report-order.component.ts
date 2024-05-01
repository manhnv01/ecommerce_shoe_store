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
  paginationModel: PaginationModel;
  receipt: any;
  search: string = '';
  receipts: ReceiptModel[] = [];
  count: number = 0;
  total: number = 0;
  years: number[] = [];
  yearNow: number = new Date().getFullYear();

  chooseYear: number = new Date().getFullYear();

  selectOrderYear: number = new Date().getFullYear();
  selectReceiptYear: number = new Date().getFullYear();

  private findAllSubscription: Subscription | undefined;

  constructor(
    private receiptService: ReceiptService,
    private toastr: ToastrService,
    private activatedRoute: ActivatedRoute,
    private reportService: ReportService,
    private router: Router,
    private title: Title
  ) {
    this.title.setTitle('Báo cáo bán hàng');
    this.paginationModel = new PaginationModel({});
  }

  ngOnInit(): void {
    this.createYears();
    this.getTotals();
    this.activatedRoute.queryParams.subscribe((params) => {
      const { search = '', size = 5, page = 1, 'sort-direction': sortDir = 'ASC', 'sort-by': sortBy = 'id' } = params;

      this.findAll(+page, +size, sortDir, sortBy, search);
    });
    this.search = this.activatedRoute.snapshot.queryParams['search'] || '';
  }

  ngOnDestroy(): void {
    if (this.findAllSubscription) {
      this.findAllSubscription.unsubscribe();
    }
  }

  getTotals() {
    this.receiptService.getTotals().subscribe({
      next: (response: any) => {
        console.log(response);
        this.total = response.total;
      },
      error: (error: any) => {
        console.log(error);
      }
    });
  }

  findAll(page: number = 1, pageSize: number = this.paginationModel.pageSize, sortDir: string = 'ASC', sortBy: string = 'id', search: string = this.search): void {
    this.findAllSubscription = this.receiptService.findAll(page, pageSize, sortDir, sortBy, search).subscribe({
      next: (response: any) => {
        this.paginationModel = new PaginationModel({
          content: response.content,
          totalPages: response.totalPages,
          totalElements: response.totalElements,
          pageNumber: response.number + 1,
          pageSize: response.size,
          startNumberItem: response.numberOfElements > 0 ? (response.number) * response.size + 1 : 0,
          endNumberItem: (response.number) * response.size + response.numberOfElements,
          pageLast: response.last,
          pageFirst: response.first,
        });
        this.paginationModel.calculatePageNumbers();
        this.getTotals();
        console.log(response.content);
      },
      error: (error: any) => {
        console.log(error);
      }
    });
  }

  changePageSize(pageSize: number): void {
    this.router.navigate([], { queryParams: { size: pageSize, page: 1 }, queryParamsHandling: 'merge' }).then(() => { });
    this.receipt = [];
  }
  changePageNumber(pageNumber: number): void {
    if (pageNumber === this.paginationModel.pageNumber) return;
    this.receipt = [];
    this.router.navigate([], { queryParams: { page: pageNumber }, queryParamsHandling: 'merge' }).then(() => { });
  }
  searchItem(): void {
    this.router.navigate([], { queryParams: { search: this.search, page: 1 }, queryParamsHandling: 'merge' }).then(() => { });
  }
  changeSort(sortBy: string): void {
    let sortDir = 'ASC';
    if (this.activatedRoute.snapshot.queryParams['sort-direction'] === sortDir) {
      sortDir = sortDir === 'ASC' ? 'DESC' : 'ASC';
    }
    this.router.navigate([], { queryParams: { 'sort-direction': sortDir, 'sort-by': sortBy }, queryParamsHandling: 'merge' }).then(() => { });
  }

  iconClass(sortBy: string): number {
    const sortBy2 = this.activatedRoute.snapshot.queryParams['sort-by'];
    const sortDir = this.activatedRoute.snapshot.queryParams['sort-direction'];
    if (sortDir === 'ASC' && sortBy2 === sortBy) return 1;
    else if (sortDir === 'DESC' && sortBy2 === sortBy) return 2;
    else return 0;
  }

  handleSuccess(): void {
    this.receipt = [];
    const sortDir = this.activatedRoute.snapshot.queryParams['sort-direction'];
    const sortBy = this.activatedRoute.snapshot.queryParams['sort-by'];
    this.findAll(this.paginationModel.pageNumber, this.paginationModel.pageSize, sortDir, sortBy, this.search);
  }
  clearAllParams(): void {
    const navigationExtras: NavigationExtras = {
      queryParams: {},
    };
    this.router.navigate([], navigationExtras);
    this.handleSuccess();
    this.search = '';
  }


  //////////////////////////////////////////////////////
  onYearSelect(event: any) {
    this.chooseYear = event.target.value;
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

  exportReceiptReport() {
    this.reportService.exportReceiptReport(this.selectReceiptYear).subscribe({
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

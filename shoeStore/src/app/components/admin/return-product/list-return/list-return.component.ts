import { Component, OnInit } from '@angular/core';
import { NavigationExtras } from '@angular/router';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { ReceiptModel } from 'src/app/model/receipt.model';
import { ReceiptService } from 'src/app/service/receipt.service';
import { PaginationModel } from 'src/app/model/pagination.model';
import { Title } from '@angular/platform-browser';
import { ReturnService } from 'src/app/service/return.service';

@Component({
  selector: 'app-list-return',
  templateUrl: './list-return.component.html',
  styleUrls: ['./list-return.component.css']
})
export class ListReturnComponent implements OnInit {
  paginationModel: PaginationModel;
  receipt: any;
  search: string = '';
  receipts: ReceiptModel[] = [];
  count: number = 0;
  total: number = 0;

  status: string = '';

  totals: any;

  private findAllSubscription: Subscription | undefined;

  constructor(
    private returnService: ReturnService,
    private toastr: ToastrService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private title: Title
  ) {
    this.title.setTitle('Quản lý phiếu đổi trả');
    this.paginationModel = new PaginationModel({});
  }

  ngOnInit(): void {
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

  findByStatus(status: string): void {
    this.status = status;
    this.handleSuccess();
  }

  getTotals() {
    this.returnService.getTotals().subscribe({
      next: (response: any) => {
        console.log(response);
        this.totals = response;
      },
      error: (error: any) => {
        console.log(error);
      }
    });
  }

  findAll(page: number = 1, pageSize: number = this.paginationModel.pageSize, sortDir: string = 'ASC', sortBy: string = 'id', search: string = this.search): void {
    this.findAllSubscription = this.returnService.findAll(page, pageSize, sortDir, sortBy, search, this.status).subscribe({
      next: (response: any) => {
        console.log(response.content);
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
}
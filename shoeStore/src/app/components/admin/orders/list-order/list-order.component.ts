import { Component, OnInit } from '@angular/core';
import { AccountService } from 'src/app/service/account.service';
import { CustomerService } from 'src/app/service/customer.service';
import { Title } from '@angular/platform-browser';
import { ToastrService } from 'ngx-toastr';
import { TokenService } from 'src/app/service/token.service';
import { Environment } from 'src/app/environment/environment';
import { PaginationModel } from 'src/app/model/pagination.model';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import { OrderService } from 'src/app/service/order.service';

@Component({
  selector: 'app-list-order',
  templateUrl: './list-order.component.html',
  styleUrls: ['./list-order.component.css']
})
export class ListOrderComponent implements OnInit {
  totals: any;
  orderStatus: string = '';
  search: string = '';
  paginationModel: PaginationModel;
  orders: any[] = [];
  baseUrl: string = `${Environment.apiBaseUrl}`;

  constructor(
    private accountService: AccountService,
    private title: Title,
    private tokenService: TokenService,
    private orderService: OrderService,
    private customerService: CustomerService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private toastr: ToastrService,
  ) { 
    this.paginationModel = new PaginationModel({});
  }

  ngOnInit() {
    this.title.setTitle('Danh sách đơn hàng');
    this.getTotals();
    this.activatedRoute.queryParams.subscribe((params) => {
      const { search = '', size = 10, page = 1, 'sort-direction': sortDir = 'ASC', 'sort-by': sortBy = 'id', 'order-status': orderStatus = '' } = params;
      this.orderStatus = orderStatus;
      this.search = search;
      this.findAll(+page, +size, sortDir, sortBy);
    });
  }

  findAll(page: number, size: number, sortDir: string, sortBy: string): void {
    this.orderService.findAll(size, page, sortDir, sortBy, this.orderStatus, this.search).subscribe({
      next: (response: any) => {
        this.paginationModel = new PaginationModel({
          content: response.content,
          totalPages: response.totalPages,
          totalElements: response.totalElements,
          last: response.last,
          size: response.size,
          number: response.number,
          first: response.first,
          numberOfElements: response.numberOfElements,
          empty: response.empty
        });
      },
      error: (err: any) => {
        console.log(err);
      }
    });
  }

  findByStatus(filter: string): void {
    this.orderStatus = filter;
    this.findAll(1, 10, 'DESC', 'id');
  }

  getTotals() {
    this.orderService.getTotalsForAdmin().subscribe({
      next: (response: any) => {
        this.totals = response;
      },
      error: (error: any) => {
        console.log(error);
      }
    });
  }

  changePageSize(pageSize: number): void {
    this.router.navigate([], { queryParams: { size: pageSize, page: 1 }, queryParamsHandling: 'merge' }).then(() => { });
  }
  changePageNumber(pageNumber: number): void {
    if (pageNumber === this.paginationModel.pageNumber) return;
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

  clearAllParams(): void {
    //const queryParams = { ...this.router.routerState.snapshot.root.queryParams };
    // delete queryParams['yourParamName'];

    const navigationExtras: NavigationExtras = {
      queryParams: {},
      //queryParamsHandling: 'merge',
    };
    this.router.navigate([], navigationExtras);
    // this.enabled = '';
    // this.handleSuccess();
    // this.search = '';
  }

  // isSelected(category: CategoryModel): boolean {
  //   return this.categories.findIndex(c => c.id === category.id) !== -1;
  // }


  // onCheckboxChange(category: CategoryModel) {
  //   const index = this.categories.findIndex(c => c.id === category.id);

  //   if (index === -1) {
  //     this.categories.push(category);
  //   } else {
  //     this.categories.splice(index, 1);
  //   }
  // }

  // chuyển trang create
  createOrder(): void {
    this.router.navigate(['admin/order/create']);
  }
}

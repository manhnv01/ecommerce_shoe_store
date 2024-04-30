import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { AccountService } from 'src/app/service/account.service';
import { CustomerService } from 'src/app/service/customer.service';
import { TokenService } from 'src/app/service/token.service';
import { Title } from '@angular/platform-browser';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { OrderService } from 'src/app/service/order.service';
import { Router, ActivatedRoute, NavigationExtras } from '@angular/router';
import { Environment } from 'src/app/environment/environment';
import { PaginationModel } from 'src/app/model/pagination.model';
import { ReturnService } from 'src/app/service/return.service';

@Component({
  selector: 'app-user-order',
  templateUrl: './user-order.component.html',
  styleUrls: ['./user-order.component.css']
})
export class UserOrderComponent implements OnInit {

  @ViewChild('btnCloseModal') btnCloseModal!: ElementRef;
  totals: any;
  totalsReturn: any;
  orderStatus: string = '';
  paginationModel: PaginationModel;
  paginationModelReturn: PaginationModel;
  search: string = '';
  isLogin: boolean = false;
  isTokenExpired: boolean = true;
  email: string = '';
  profile: any;
  baseUrl: string = `${Environment.apiBaseUrl}`;

  constructor(
    private accountService: AccountService,
    private title: Title,
    private tokenService: TokenService,
    private orderService: OrderService,
    private customerService: CustomerService,
    private router: Router,
    private returnService: ReturnService,
    private activatedRoute: ActivatedRoute,
    private toastr: ToastrService,
  ) {
    this.paginationModel = new PaginationModel({});
    this.paginationModelReturn = new PaginationModel({});
  }

  ngOnInit() {
    this.title.setTitle('Đơn hàng của tôi');

    if (this.tokenService.getToken() !== null) {
      this.isLogin = true;
      this.isTokenExpired = this.tokenService.isTokenExpired();

      if (!this.isTokenExpired) {
        this.email = this.tokenService.getUserName();
      }

      if (this.isLogin && !this.isTokenExpired && this.tokenService.getUserRoles().includes('ROLE_USER')) {
        this.getProfile();
        this.getTotals();
        this.getReturnTotals();

        this.activatedRoute.queryParams.subscribe((params) => {
          const { search = '', size = 5, page = 1, 'sort-direction': sortDir = 'ASC', 'sort-by': sortBy = 'id' } = params;

          this.findAllByCustomer(+size, +page, sortDir, sortBy);
          this.findAllReturnByCustomer(+size, +page, sortDir, sortBy);
        });
      }
    }
  }

  getProfile() {
    this.customerService.findByEmail(this.tokenService.getUserName()).subscribe({
      next: (response) => {
        this.profile = response;
      },
      error: (error) => {
        console.log(error);
      }
    });
  }

  findByStatus(filter: string): void {
    this.orderStatus = filter;
    this.findAllByCustomer(this.paginationModel.pageSize, this.paginationModel.pageNumber, 'DESC', 'id');
  }

  getTotals() {
    this.orderService.getTotalsByUserLogin().subscribe({
      next: (response: any) => {
        this.totals = response;
      },
      error: (error: any) => {
        console.log(error);
      }
    });
  }

  // Lấy tất cả đơn hàng của khách hàng
  findAllByCustomer(pageSize: number, pageNumber: number, sortDir: string, sortBy: string) {
    this.orderService.findAllByCustomer(this.email, pageSize, pageNumber, sortDir, sortBy, this.orderStatus).subscribe({
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
        this.getReturnTotals();
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
    const navigationExtras: NavigationExtras = {
      queryParams: {},
      //queryParamsHandling: 'merge',
    };
    this.router.navigate([], navigationExtras);
  }


  //////////////////////////////////// Đổi trả /////////////////////////////////

  getReturnTotals() {
    this.returnService.getTotalsByUserLogin().subscribe({
      next: (response: any) => {
        this.totalsReturn = response;
      },
      error: (error: any) => {
        console.log(error);
      }
    });
  }

  // Lấy tất cả phiếu đổi trả của khách hàng
  findAllReturnByCustomer(pageSize: number, pageNumber: number, sortDir: string, sortBy: string) {
    this.returnService.findAllByCustomer(this.email, pageSize, pageNumber, sortDir, sortBy).subscribe({
      next: (response: any) => {
        this.paginationModelReturn = new PaginationModel({
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
        this.paginationModelReturn.calculatePageNumbers();
        this.getTotals();
        this.getReturnTotals();
      },
      error: (error: any) => {
        console.log(error);
      }
    });
  }
}

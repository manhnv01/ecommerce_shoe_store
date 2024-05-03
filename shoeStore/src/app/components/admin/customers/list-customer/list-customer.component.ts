import { Component, OnInit } from '@angular/core';
import { Environment } from 'src/app/environment/environment';
import { PaginationModel } from 'src/app/model/pagination.model';
import { ToastrService } from 'ngx-toastr';
import { ActivatedRoute, Router, NavigationExtras } from '@angular/router';
import { Subscription } from 'rxjs';
import { Title } from '@angular/platform-browser';
import { CustomerService } from 'src/app/service/customer.service';
import { AccountService } from 'src/app/service/account.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-list-customer',
  templateUrl: './list-customer.component.html',
  styleUrls: ['./list-customer.component.css']
})
export class ListCustomerComponent implements OnInit {
  paginationModel: PaginationModel;
  product: any;
  search: string = '';
  status: string = '';
  count: number = 0;

  baseUrl: string = `${Environment.apiBaseUrl}`;

  total: number = 0;
  totalEnabled: number = 0;
  totalDisabled: number = 0;

  private findAllSubscription: Subscription | undefined;

  constructor(
    private customerService: CustomerService,
    private toastr: ToastrService,
    private activatedRoute: ActivatedRoute,
    private accountService: AccountService,
    private router: Router,
    private title: Title
  ) {
    this.title.setTitle('Quản lý khách hàng');
    this.paginationModel = new PaginationModel({});
  }

  ngOnInit(): void {
    this.getTotals();
    this.activatedRoute.queryParams.subscribe((params) => {
      const { search = '', size = 5, page = 1, 'sort-direction': sortDir = 'ASC', 'sort-by': sortBy = 'id' } = params;

      this.findAll(+page, +size, sortDir, sortBy, search);
    });
  }

  // khóa tài khoản
  lockAccount(id: number): void {
    
    if (id === null || id === undefined || id === 0){
      this.toastr.warning('Không tìm thấy tài khoản của người dùng này', 'Thông báo');
      return;
    }

    this.accountService.lockAccount(id).subscribe({
      next: (response: any) => {
        this.toastr.success('Khóa tài khoản thành công', 'Thông báo');
        this.handleSuccess();
      },
      error: (error: any) => {
        console.log(error);
        if (error.error === 'ACCOUNT_NOT_FOUND' && error.status === 400) {
          this.toastr.error('Không tìm thấy tài khoản', 'Thông báo');
        }
        else {
          this.toastr.error('Lỗi không xác định', 'Thông báo');
        }
      }
    });
  }

  // mở khóa tài khoản
  unlockAccount(id: number): void {

    if (id === null || id === undefined || id === 0){
      this.toastr.warning('Không tìm thấy tài khoản của người dùng này', 'Thông báo');
      return;
    }

    this.accountService.unlockAccount(id).subscribe({
      next: (response: any) => {
        this.toastr.success('Mở khóa tài khoản thành công', 'Thông báo');
        this.handleSuccess();
      },
      error: (error: any) => {
        console.log(error);
        if (error.error === 'ACCOUNT_NOT_FOUND' && error.status === 400) {
          this.toastr.error('Không tìm thấy tài khoản', 'Thông báo');
        }
        else {
          this.toastr.error('Lỗi không xác định', 'Thông báo');
        }
      }
    });
  }

  confirmLockAccount(id: number): void {
    Swal.fire({
      title: 'Bạn có chắc chắn muốn khóa tài khoản khách hàng này?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Đồng ý',
      cancelButtonText: 'Hủy',
      customClass: {
        confirmButton: 'btn btn-sm btn-danger',
        cancelButton: 'btn btn-sm btn-dark'
      },
    }).then((result) => {
      if (result.isConfirmed) {
        this.lockAccount(id);
      }
    });
  }

  ngOnDestroy(): void {
    if (this.findAllSubscription) {
      this.findAllSubscription.unsubscribe();
    }
  }

  detail(id: number){
    this.router.navigate(['admin/customer/', id]);
  }

  getTotals() {
    this.customerService.getTotals().subscribe({
      next: (response: any) => {
        console.log(response);
        this.total = response.total;
        this.totalEnabled = response.totalEnabled;
        this.totalDisabled = response.totalDisabled;
      },
      error: (error: any) => {
        console.log(error);
      }
    });
  }

  findAll(page: number = 1, pageSize: number = this.paginationModel.pageSize, sortDir: string = 'ASC', sortBy: string = 'id', search: string = this.search, status: string = this.status): void {
    this.findAllSubscription = this.customerService.findAll(page, pageSize, sortDir, sortBy, search, status).subscribe({
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
        console.log(this.paginationModel);  
        this.paginationModel.calculatePageNumbers();
        this.getTotals()
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

  findByEnabled(status: string): void {
    this.status = status;
    this.handleSuccess();
  }

  handleSuccess(): void {
    const sortDir = this.activatedRoute.snapshot.queryParams['sort-direction'];
    const sortBy = this.activatedRoute.snapshot.queryParams['sort-by'];
    this.findAll(this.paginationModel.pageNumber, this.paginationModel.pageSize, sortDir, sortBy, this.search);
  }
  clearAllParams(): void {
    const navigationExtras: NavigationExtras = {
      queryParams: {},
    };
    this.router.navigate([], navigationExtras);
    this, this.handleSuccess();
  }
}

import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { AccountService } from 'src/app/service/account.service';
import { CustomerService } from 'src/app/service/customer.service';
import { Title } from '@angular/platform-browser';
import { ToastrService } from 'ngx-toastr';
import { TokenService } from 'src/app/service/token.service';
import { Environment } from 'src/app/environment/environment';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { OrderService } from 'src/app/service/order.service';
import { PaginationModel } from 'src/app/model/pagination.model';
import { ActivatedRoute, NavigationExtras, Route, Router } from '@angular/router';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  @ViewChild('btnCloseModal') btnCloseModal!: ElementRef;
  show: boolean = true;

  totals: any;

  orderStatus: string = '';

  paginationModel: PaginationModel;
  cities: any;
  districts: any;
  wards: any;

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
    private activatedRoute: ActivatedRoute,
    private toastr: ToastrService,
  ) { 
    this.paginationModel = new PaginationModel({});
  }

  profileForm: FormGroup = new FormGroup({
    id: new FormControl(null),
    name: new FormControl('', [Validators.required, Validators.minLength(2), Validators.maxLength(30)]),
    phone: new FormControl('', [Validators.required, Validators.pattern('^(0)[0-9]{9}$')]),
    city: new FormControl(null, [Validators.required]),
    district: new FormControl(null, [Validators.required]),
    ward: new FormControl(null, [Validators.required]),
    addressDetail: new FormControl('', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]),
  });

  ngOnInit() {
    this.title.setTitle('Cá nhân');
    this.getJsonDataAddress();

    if (this.tokenService.getToken() !== null) {
      this.isLogin = true;
      this.isTokenExpired = this.tokenService.isTokenExpired();

      if (!this.isTokenExpired) {
        this.email = this.tokenService.getUserName();
      }

      if (this.isLogin && !this.isTokenExpired && this.tokenService.getUserRoles().includes('ROLE_USER')) {
        this.getProfile();
        this.getTotals();

        this.activatedRoute.queryParams.subscribe((params) => {
          const { search = '', size = 5, page = 1, 'sort-direction': sortDir = 'ASC', 'sort-by': sortBy = 'id' } = params;
      
          this.findAllByCustomer(+size, +page, sortDir, sortBy);
        });
      }
    }
  }

  resetText() {
    this.profileForm.reset();
  }

  onSubmit() {
    this.profileForm.get('id')?.setValue(this.profile.id);
    this.customerService.updateProfile(this.profileForm.value).subscribe({
      next: (response) => {
        this.toastr.success('Cập nhật thông tin thành công');
        this.getProfile();
        this.btnCloseModal.nativeElement.click();
      },
      error: (error) => {
        console.log(error);
        if (error.status === 400 && error.error === 'DUPLICATE_PHONE') {
          this.toastr.error('Số điện thoại đã được sử dụng');
        }
        else
          this.toastr.error('Cập nhật thông tin thất bại');
      }
    });
  }

  openModal() {
    this.profileForm.patchValue(this.profile);
  }

  getJsonDataAddress() {
    this.accountService.getJsonDataAddress().subscribe({
      next: (response) => {
        this.cities = response;
      },
      error: (error) => {
        console.log(error);
      }
    });
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

  getDistrictsControl(): FormControl {
    const cityControl = this.profileForm.get('city') as FormControl;
    cityControl.valueChanges.pipe().subscribe((id: any) => {
      this.cities?.forEach((city: any) => {
        if (city.name === id) {
          this.districts = city.districts;
          this.profileForm.get('district')?.setValue(this.districts[0]?.name); // Đảm bảo mảng districts không rỗng trước khi gán giá trị
        }
      });
    });
    return cityControl;
  }

  getWardsControl(): FormControl {
    const districtControl = this.profileForm.get('district') as FormControl;
    districtControl.valueChanges.pipe().subscribe((name: any) => {
      this.districts?.forEach((district: any) => {
        if (district.name === name) {
          this.wards = district.wards;
          this.profileForm.get('ward')?.setValue(this.wards[0]?.name); // Đảm bảo mảng wards không rỗng trước khi gán giá trị
        }
      });
    });
    return districtControl;
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
}

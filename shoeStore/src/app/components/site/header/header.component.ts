import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { AccountService } from 'src/app/service/account.service';
import { CategoryService } from 'src/app/service/category.service';
import { CategoryModel } from 'src/app/model/category.model';
import { TokenService } from 'src/app/service/token.service';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CartService } from 'src/app/service/cart.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { CustomerService } from 'src/app/service/customer.service';
import { ProductService } from 'src/app/service/product.service';
import { Router, ActivatedRoute, NavigationExtras } from '@angular/router';
import { Environment } from 'src/app/environment/environment';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  @ViewChild('oldPassword') oldPassword!: ElementRef;
  @ViewChild('newPassword') newPassword!: ElementRef;
  @ViewChild('btnCloseModal') btnCloseModal!: ElementRef;
  show: boolean = true;
  baseUrl: string = `${Environment.apiBaseUrl}`;

  search: string = '';
  searchIndex: number = 0;

  recentSearches: { value: string, timestamp: number }[] = [];

  products: any[] = [];

  profile: any;

  isAdmin: boolean = false;

  isLogin: boolean = false;
  isTokenExpired: boolean = true;

  totalProductInCart: number = 0;

  categories: CategoryModel[] = [];

  private findAllSubscription: Subscription | undefined;

  email: string = '';

  changePasswordForm: FormGroup = new FormGroup({
    id: new FormControl(null),
    oldPassword: new FormControl('', [Validators.required]),
    newPassword: new FormControl('', [Validators.required, Validators.minLength(6), Validators.maxLength(30)]),
  });

  constructor(
    private accountService: AccountService,
    private tokenService: TokenService,
    private categoryService: CategoryService,
    private customerService: CustomerService,
    private productService: ProductService,
    private toastr: ToastrService,
    private cartService: CartService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private title: Title
  ) { }

  ngOnInit() {
    this.title.setTitle('Trang chủ');
    this.getCategories();

    this.recentSearches = this.getRecentSearches();

    if (this.tokenService.getToken() !== null) {
      this.isLogin = true;
      this.isTokenExpired = this.tokenService.isTokenExpired();

      if (!this.isTokenExpired) {
        this.getProfile();
        this.email = this.tokenService.getUserName();
      }

      if (!this.tokenService.getUserRoles().includes('ROLE_USER')) {
        this.isAdmin = true;
      }

      if (!this.isTokenExpired && this.tokenService.getUserRoles().includes('ROLE_USER')) {
        this.getCartByAccountEmail();
      }
    }

    this.cartService.cartItemCount$.subscribe(count => {
      this.totalProductInCart = count;
    });
  }

  ngOnDestroy(): void {
    if (this.findAllSubscription) {
      this.findAllSubscription.unsubscribe();
    }
  }

  getCategories() {
    this.categoryService.getAll().subscribe({
      next: (response) => {
        console.log(response);
        this.categories = response;
      }
    });
  }

  getCartByAccountEmail() {
    this.findAllSubscription = this.cartService.getCartByAccountEmail(this.tokenService.getUserName()).subscribe({
      next: (response) => {
        this.totalProductInCart = response.totalProduct;
      },
      error: (error) => {
        console.log(error);
      }
    })
  }

  getProfile() {
    this.customerService.findByEmail(this.tokenService.getUserName()).subscribe({
      next: (response) => {
        console.log(response);
        this.profile = response;
      },
      error: (error) => {
        console.log(error);
      }
    });
  }


  onChangePassword() {
    this.changePasswordForm.get('id')?.setValue(this.profile.account.id);
    console.log(this.changePasswordForm.value);
    this.accountService.changePassword(this.changePasswordForm.value).subscribe({
      next: (response) => {
        console.log(response);
        this.toastr.success('Đổi mật khẩu thành công');
        this.resetText();
        this.btnCloseModal.nativeElement.click();
      },
      error: (error) => {
        console.log(error);
        if (error.status === 400 && error.error === 'INVALID_PASSWORD') {
          this.toastr.error('Mật khẩu cũ không đúng');
        } else if (error.status === 400 && error.error === 'ACCOUNT_NOT_FOUND') {
          this.toastr.error('Tài khoản không tồn tại');
        } else
          this.toastr.error('Đổi mật khẩu thất bại');
      }
    });
  }

  togglePassword() {
    this.show = !this.show;
    if (this.show) {
      this.oldPassword.nativeElement.setAttribute('type', 'password');
      this.newPassword.nativeElement.setAttribute('type', 'password');
    }
    else {
      this.oldPassword.nativeElement.setAttribute('type', 'text');
      this.newPassword.nativeElement.setAttribute('type', 'text');
    }
  }

  onSearch() {
    // lưu vào localStorage
    // Lấy danh sách tìm kiếm đã lưu từ localStorage, hoặc tạo một mảng mới nếu không tồn tại
    let searches: { value: string, timestamp: number }[] = JSON.parse(localStorage.getItem('searches') || '[]');

    //kiểm tra xem giá trị tìm kiếm đã tồn tại trong mảng chưa không phân biệt hoa thường
    const index = searches.findIndex(item => item.value.toLowerCase() === this.search.toLowerCase());
    // Nếu tồn tại thì xóa giá trị tìm kiếm đó khỏi mảng và lưu giá trị mới vào vị trí đầu tiên
    if (index !== -1) {
      searches.splice(index, 1);
    }

    // Thêm giá trị tìm kiếm mới vào mảng với thời gian hiện tại
    searches.push({ value: this.search, timestamp: Date.now() });

    // Lưu lại mảng tìm kiếm vào localStorage
    localStorage.setItem('searches', JSON.stringify(searches));


    //this.router.navigate(['/product'], { queryParams: { search: this.search } });

    // dùng href giữ nguyên param trên url
    
    window.location.href = `/product?search=${this.search}`;
  }

  // lấy 5 phần tử gần đây nhất theo thời gian trong localStorage 
  getRecentSearches(): { value: string, timestamp: number }[] {
    let searches: { value: string, timestamp: number }[] = JSON.parse(localStorage.getItem('searches') || '[]');
    searches = searches.sort((a, b) => b.timestamp - a.timestamp).slice(0, 5);
    return searches;
  }

  removeRecentSearch(searchItem: { value: string, timestamp: number }) {
    let searches: { value: string, timestamp: number }[] = JSON.parse(localStorage.getItem('searches') || '[]');
    searches = searches.filter(item => item.value !== searchItem.value);
    localStorage.setItem('searches', JSON.stringify(searches));
    this.recentSearches = this.getRecentSearches();
  }


  resetText() {
    this.changePasswordForm.reset();
  }

  logout(): void {
    this.accountService.logout();
  }

  resetSearch() {
    this.search = '';
    this.products = [];
    this.searchIndex = 0;

    const navigationExtras: NavigationExtras = {
      queryParams: {},
      //queryParamsHandling: 'merge',
    };
    this.router.navigate([], navigationExtras);
  }

  searchProduct(event: KeyboardEvent) {
    // Kiểm tra nếu phím nhấn là Backspace (keyCode = 8) và độ dài của chuỗi search là 0
    if (event.key === 'Backspace' && this.search.length === 0) {
      this.search = ''; // Đặt giá trị của biến search về ''
    }

    this.searchIndex++;

    if (this.search === '') {
      this.products = [];
      return;
    }
    this.productService.search(this.search).subscribe({
      next: (response) => {
        console.log(response);
        this.products = response;
      },
      error: (error) => {
        console.log(error);
      }
    });
  }
}

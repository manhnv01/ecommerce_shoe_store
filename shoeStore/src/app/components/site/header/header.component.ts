import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { AccountService } from 'src/app/service/account.service';
import { CategoryService } from 'src/app/service/category.service';
import { CategoryModel } from 'src/app/model/category.model';
import { TokenService } from 'src/app/service/token.service';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CartService } from 'src/app/service/cart.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  isLogin: boolean = false;
  isTokenExpired: boolean = true;

  totalProductInCart: number = 0;

  categories: CategoryModel[] = [];

  private findAllSubscription: Subscription | undefined;

  email: string = '';

  constructor(
    private accountService: AccountService,
    private tokenService: TokenService,
    private categoryService: CategoryService,
    private toastr: ToastrService,
    private cartService: CartService,
    private title: Title
  ) { }

  ngOnInit() {
    this.title.setTitle('Trang chủ');

    if (this.tokenService.getToken() !== null) {
      this.isLogin = true;
      this.isTokenExpired = this.tokenService.isTokenExpired();

      if (!this.isTokenExpired) {
        this.email = this.tokenService.getUserName();
      }

      if (this.isLogin && !this.isTokenExpired && this.tokenService.getUserRoles().includes('ROLE_USER')) {
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

  logout(): void {
    this.accountService.logout();
  }
}

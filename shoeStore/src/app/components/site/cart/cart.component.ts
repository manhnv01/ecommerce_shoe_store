import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { CartService } from 'src/app/service/cart.service';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { TokenService } from 'src/app/service/token.service';
import { Title } from '@angular/platform-browser';
import { Environment } from 'src/app/environment/environment';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {

  @ViewChild('checkAll') checkAll!: ElementRef;
  cart: any;
  totalElement: number = 0;

  count: number = 0;

  cartDetails: any[] = [];

  totalPrice: number = 0; // Tổng tiền hàng gốc
  totalDiscount: number = 0; // Tổng tiền giảm giá

  totalQuantity: number = 0; // Tổng số lượng sản phẩm

  baseUrl: string = `${Environment.apiBaseUrl}`;

  private findAllSubscription: Subscription | undefined;

  constructor(
    private cartService: CartService,
    private toastr: ToastrService,
    private tokenService: TokenService,
    private title: Title
  ) {
    this.cart = [];
    this.title.setTitle('Giỏ hàng');
  }

  ngOnInit() {
    if (this.tokenService.isLogin()
      && this.tokenService.getUserRoles().includes('ROLE_USER')
      && !this.tokenService.isTokenExpired()) {
      this.getCartByAccountEmail();
    }
  }

  ngOnDestroy(): void {
    if (this.findAllSubscription) {
      this.findAllSubscription.unsubscribe();
    }
  }

  getCartByAccountEmail() {
    this.findAllSubscription = this.cartService.getCartByAccountEmail(this.tokenService.getUserName()).subscribe({
      next: (response) => {
        this.cart = response;
        console.log(this.cart);

        this.calculateTotal();
      },
      error: (error) => {
        console.log(error);
        this.toastr.error('Có lỗi xảy ra, vui lòng thử lại sau', 'Thông báo');
      }
    })
  }

  updateQuantity(cartDetails: any, action: boolean): void {
    const newQuantity = action ? cartDetails.quantity + 1 : cartDetails.quantity - 1;

    if (newQuantity < 1) {
      return;
    }

    if (newQuantity > cartDetails.productQuantity && action) {
      this.toastr.info(`Số lượng sản phẩm đã đạt giới hạn`);
      return;
    }

    cartDetails.quantity = newQuantity;

    this.cartService.updateQuantity(cartDetails).subscribe({
      next: (response: any) => {
        this.getCartByAccountEmail();
      },
      error: (error: any) => {
        console.log(error);
        if (error.status === 400 && error.error === 'CART_DETAILS_NOT_FOUND') {
          this.toastr.error(`Không tìm thấy sản phẩm này để cập nhật`);
        } else {
          this.toastr.error(`Có lỗi xảy ra, vui lòng thử lại sau`, 'Thông báo');
        }
      }
    });
  }

  clearSelected() {
    this.cartDetails = [];
    this.checkAll.nativeElement.checked = false;
  }

  isSelected(cartDetails: any): boolean {
    return this.cartDetails.findIndex(c => c.id === cartDetails.id) !== -1;
  }

  toggleSelectAll() {
    const areAllSelected = this.cartDetails.length === this.cart.totalProduct;
    this.cartDetails = areAllSelected ? [] : [...this.cart.cartDetails];
    this.calculateTotal();
  }
  
  onCheckboxChange(cartDetails: any): void {
    const index = this.cartDetails.findIndex(c => c.id === cartDetails.id);
    const isChecked = index !== -1;
  
    if (!isChecked) {
      this.cartDetails.push(cartDetails);
    } else {
      this.cartDetails.splice(index, 1);
      this.checkAll.nativeElement.checked = false;
    }
  
    if (this.cartDetails.length === this.cart.totalProduct) {
      this.checkAll.nativeElement.checked = true;
    }
  
    if (this.cartDetails.length === 0) {
      this.calculateTotal();
    } else {
      this.calculateTotalChooseProduct();
    }
  }
  

  // tính toán tổng tiền hàng và tổng tiền giảm giá khi chọn sản phẩm
  calculateTotalChooseProduct(): void {
    this.totalPrice = 0;
    this.totalDiscount = 0;
    this.totalQuantity = 0;

    for (let i = 0; i < this.cartDetails.length; i++) {
      this.totalPrice += this.cartDetails[i].totalPrice;
      this.totalQuantity += this.cartDetails[i].quantity;
      if (this.cartDetails[i].salePrice !== null) {
        this.totalDiscount += this.cartDetails[i].totalPrice - this.cartDetails[i].totalSalePrice;
      }
    }
  }

  checkOut(): void {
    if (this.cartDetails.length === 0) {
      this.toastr.info('Hãy chọn sản phẩm để thanh toán', 'Thông báo');
    } else {

      // làm sạch session storage
      sessionStorage.removeItem('cartDetails');

      console.log(this.cartDetails);

      // lưu sản phẩm vào session storage
      sessionStorage.setItem('cartDetails', JSON.stringify(this.cartDetails));
      window.location.href = '/check-out';
    }
  }

  // tính toán tổng tiền hàng và tổng tiền giảm giá của tất cả sản phẩm
  calculateTotal(): void {
    this.totalPrice = 0;
    this.totalDiscount = 0;
    this.totalQuantity = 0;

    for (let i = 0; i < this.cart.totalProduct; i++) {
      this.totalPrice += this.cart.cartDetails[i].totalPrice;
      this.totalQuantity += this.cart.cartDetails[i].quantity;
      if (this.cart.cartDetails[i].salePrice !== null) {
        this.totalDiscount += this.cart.cartDetails[i].totalPrice - this.cart.cartDetails[i].totalSalePrice;
      }
    }
  }

  deleteCartDetailsById(id: number): void {
    Swal.fire({
      title: 'Bạn có chắc chắn muốn xóa?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Xóa',
      cancelButtonText: 'Hủy',
      customClass: {
        confirmButton: 'btn btn-sm btn-danger',
        cancelButton: 'btn btn-sm btn-dark'
      },
    }).then((result) => {
      if (result.isConfirmed) {
        this.cartService.deleteCartDetailsById(id).subscribe({
          next: (response: any) => {
            this.toastr.success(`Xóa thành công`);
            this.getCartByAccountEmail();
            this.cart.cartDetails = this.cart.cartDetails.filter((c: any) => c.id !== id);
            this.calculateTotal();
            // Cập nhật số lượng hiển thị trong header
            if (this.cart.totalProduct > 0)
              this.cartService.setCartItemCount(this.cart.totalProduct - 1);
          },
          error: (error: any) => {
            console.log(error);
            if (error.status === 400 && error.error === 'CART_DETAILS_NOT_FOUND')
              this.toastr.error(`Không tìm thấy sản phẩm này để xóa`);
            else
              this.toastr.error(`Có lỗi xảy ra, vui lòng thử lại sau`, 'Thông báo');
          }
        });
      }
    });
  }

  deleteMany(): void {
    if (this.cartDetails.length == 0)
      this.toastr.info('Hãy chọn sản phẩm để xóa', 'Thông báo');
    else {
      Swal.fire({
        title: 'Bạn có chắc chắn muốn xóa?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Xóa',
        cancelButtonText: 'Hủy',
        customClass: {
          confirmButton: 'btn btn-sm btn-danger',
          cancelButton: 'btn btn-sm btn-dark'
        },
      }).then((result) => {
        if (result.isConfirmed) {
          for (let i = 0; i < this.cartDetails.length; i++) {
            const cartDetails = this.cartDetails[i];
            let listLength = this.cartDetails.length;
            this.cartService.deleteCartDetailsById(cartDetails.id).subscribe({
              next: (response: any) => {
                this.getCartByAccountEmail();

                // Cập nhật tiền hàng
                this.cart.cartDetails = this.cart.cartDetails.filter((c: any) => c.id !== cartDetails.id);
                this.calculateTotal();

                this.count++;
                if (listLength === this.count) {
                  this.toastr.success(`Xóa ${this.count} mục thành công!`, 'Thông báo');
                }
              },
              error: (error: any) => {
                console.log(error);
                this.toastr.error(`Lỗi không xác định!`, 'Thông báo');
              }
            });
          }
          this.cartDetails = [];
        }
      });
    }
  }
}

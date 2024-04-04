import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { Environment } from 'src/app/environment/environment';
import { AccountService } from 'src/app/service/account.service';
import { CustomerService } from 'src/app/service/customer.service';
import { TokenService } from 'src/app/service/token.service';
import { Title } from '@angular/platform-browser';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { CartService } from 'src/app/service/cart.service';
import { OrderService } from 'src/app/service/order.service';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-check-out',
  templateUrl: './check-out.component.html',
  styleUrls: ['./check-out.component.css']
})
export class CheckOutComponent implements OnInit {

  @ViewChild('btnCloseModal') btnCloseModal!: ElementRef;
  selectedProducts: any;
  selectedPaymentMethod: number = 0;

  totalMoney: number = 0; // Tổng tiền hàng

  totalPrincipal: number = 0; // Tổng tiền hàng gốc
  totalPrice: number = 0; // Tổng tiền hàng sau khi giảm giá

  cities: any;
  districts: any;
  wards: any;

  profile: any;

  shippingCost: number = 0;

  baseUrl: string = `${Environment.apiBaseUrl}`;

  shippingForm: FormGroup = new FormGroup({
    id: new FormControl(null),
    name: new FormControl('', [Validators.required, Validators.minLength(2), Validators.maxLength(30)]),
    phone: new FormControl('', [Validators.required, Validators.pattern('^[0-9]{10}$')]),
    city: new FormControl(null, [Validators.required]),
    district: new FormControl(null, [Validators.required]),
    ward: new FormControl(null, [Validators.required]),
    addressDetail: new FormControl('', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]),
  });

  orderForm: FormGroup = new FormGroup({
    name: new FormControl('', [Validators.required]),
    phone: new FormControl('', [Validators.required]),
    city: new FormControl(null, [Validators.required]),
    district: new FormControl(null, [Validators.required]),
    ward: new FormControl(null, [Validators.required]),
    addressDetail: new FormControl(null, [Validators.required]),
    paymentMethod: new FormControl('', [Validators.required]),
    note: new FormControl('', [Validators.maxLength(100)]),
  });

  constructor(
    private accountService: AccountService,
    private customerService: CustomerService,
    private cartService: CartService,
    private orderService: OrderService,
    private tokenService: TokenService,
    private toastr: ToastrService,
    private title: Title,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit() {
    this.title.setTitle('Đặt hàng');
    if (this.tokenService.isUserLogin()) {
      this.getProfile();
    }

    this.getCartDetails();
    this.getJsonDataAddress();
  }

  onSubmitShippingForm () {
    if (this.shippingForm.invalid) {
      this.shippingForm.markAllAsTouched();
      return;
    }

    this.profile.name = this.shippingForm.get('name')?.value;
    this.profile.phone = this.shippingForm.get('phone')?.value;
    this.profile.city = this.shippingForm.get('city')?.value;
    this.profile.district = this.shippingForm.get('district')?.value;
    this.profile.ward = this.shippingForm.get('ward')?.value;
    this.profile.addressDetail = this.shippingForm.get('addressDetail')?.value;

    this.orderForm.get('name')?.setValue(this.profile.name);
    this.orderForm.get('phone')?.setValue(this.profile.phone);
    this.orderForm.get('city')?.setValue(this.profile.city);
    this.orderForm.get('district')?.setValue(this.profile.district);
    this.orderForm.get('ward')?.setValue(this.profile.ward);
    this.orderForm.get('addressDetail')?.setValue(this.profile.addressDetail);

    this.btnCloseModal.nativeElement.click();
    console.log(this.orderForm.value);
  }

  saveInfo() {
    this.shippingForm.get('id')?.setValue(this.profile.id);
    console.log(this.shippingForm.value);
    this.customerService.updateProfile(this.shippingForm.value).subscribe({
      next: (response) => {
        this.toastr.success('Lưu thành công');
        this.profile.name = this.shippingForm.get('name')?.value;
        this.profile.phone = this.shippingForm.get('phone')?.value;
        this.profile.city = this.shippingForm.get('city')?.value;
        this.profile.district = this.shippingForm.get('district')?.value;
        this.profile.ward = this.shippingForm.get('ward')?.value;
        this.profile.addressDetail = this.shippingForm.get('addressDetail')?.value;

        this.btnCloseModal.nativeElement.click();
      },
      error: (error) => {
        console.log(error);
        this.toastr.error('Lưu thật bại, vui lòng thử lại sau');
      }
    });
  }

  resetText() {
    this.shippingForm.reset();
  }

  getCartDetails() {
    const jsonStr = sessionStorage.getItem('cartDetails');

    // Chuyển đổi JSON thành đối tượng JavaScript
    if (jsonStr) {
      this.selectedProducts = JSON.parse(jsonStr);
      this.totalPrice = 0;
      this.totalPrincipal = 0;

      for (let i = 0; i < this.selectedProducts.length; i++) {
        this.totalPrice += this.selectedProducts[i].totalPrice;
        this.totalPrincipal += this.selectedProducts[i].productPrice * this.selectedProducts[i].quantity;
      }
      console.log(this.selectedProducts);
    }
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

  getDistrictsControl(): FormControl {
    const cityControl = this.shippingForm.get('city') as FormControl;
    cityControl.valueChanges.pipe().subscribe((id: any) => {
      this.cities?.forEach((city: any) => {
        if (city.name === id) {
          this.districts = city.districts;
          this.shippingForm.get('district')?.setValue(this.districts[0]?.name); // Đảm bảo mảng districts không rỗng trước khi gán giá trị
        }
      });
    });
    return cityControl;
  }

  getWardsControl(): FormControl {
    const districtControl = this.shippingForm.get('district') as FormControl;
    districtControl.valueChanges.pipe().subscribe((name: any) => {
      this.districts?.forEach((district: any) => {
        if (district.name === name) {
          this.wards = district.wards;
          this.shippingForm.get('ward')?.setValue(this.wards[0]?.name); // Đảm bảo mảng wards không rỗng trước khi gán giá trị
        }
      });
    });
    return districtControl;
  }

  openModal() {
    this.shippingForm.patchValue(this.profile);
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

  onSubmit() {
    // if (this.orderForm.invalid) {
    //   return;
    // }

    this.orderForm.get('paymentMethod')?.setValue(this.selectedPaymentMethod);
    this.orderForm.get('note')?.setValue(this.orderForm.get('note')?.value);
    this.orderForm.get('name')?.setValue(this.profile.name);
    this.orderForm.get('phone')?.setValue(this.profile.phone);
    this.orderForm.get('city')?.setValue(this.profile.city);
    this.orderForm.get('district')?.setValue(this.profile.district);
    this.orderForm.get('ward')?.setValue(this.profile.ward);
    this.orderForm.get('addressDetail')?.setValue(this.profile.addressDetail);
    

    console.log("Phương thức thanh toán: ", this.selectedPaymentMethod);
    this.create();
  }

  create() {
    let orderDto = this.orderForm.value;
    orderDto.paymentStatus = 0;
    orderDto.orderStatus = 0; // 0: Chờ xác nhận, 1: Đã xác nhận, 2: Đang giao, 3: Đã giao, 4: Đã hủy
    orderDto.orderDetails = this.selectedProducts.map((item: any) => {
      return {
        productDetailsId: item.productDetailsId,
        quantity: item.quantity,
        price: item.productPrice
      };
    });
    if (orderDto.orderDetails.length === 0) {
      this.toastr.error('Vui lòng chọn sản phẩm');
      return;
    }

    console.log("Đơn hàng: ", orderDto);
    return;

    this.orderService.create(orderDto).subscribe({
      next: (response: any) => {
        this.toastr.success('Đặt hàng thành công');

        // xóa session storage và sản phẩm đã đặt trong giỏ
        sessionStorage.removeItem('cartDetails');
        // this.cartsItems.forEach(item => {
        //   this.cartService.deleteCartItemOnServer(item.id).subscribe({
        //     next: () => {
        //       this.cartService.getCartItemsServer();
        //     }
        //   });
        // });
        if (orderDto.paymentMethod === '0') {
          this.router.navigateByUrl('/don-hang/' + response.id);
        } else if (orderDto.paymentMethod === '1') {
          this.orderService.payment(this.totalMoney, response.id).subscribe({
            next: (data: any) => {
              window.location.href = data.redirectUrl;
            },
            error: (error: any) => {
              console.log(error);
              this.toastr.error('Lỗi thực hiện, vui lòng thử lại sau');
            }
          });
        }
      },
      error: (error: any) => {
        if (error.status === 400)
          this.toastr.error(error.error);
        else
          this.toastr.error('Lỗi thực hiện, vui lòng thử lại sau');
      }
    });
  }
}

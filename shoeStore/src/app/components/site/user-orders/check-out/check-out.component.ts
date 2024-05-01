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
import { AddressService } from 'src/app/service/address.service';

@Component({
  selector: 'app-check-out',
  templateUrl: './check-out.component.html',
  styleUrls: ['./check-out.component.css']
})
export class CheckOutComponent implements OnInit {

  @ViewChild('btnCloseModal') btnCloseModal!: ElementRef;
  selectedProducts: any;
  selectedPaymentMethod: number = 0;

  cart: any;

  totalPrice: number = 0; // Tổng tiền hàng gốc
  totalDiscount: number = 0; // Tổng tiền giảm giá

  totalQuantity: number = 0; // Tổng số lượng sản phẩm

  cities: any;
  districts: any;
  wards: any;

  cities2: any;
  districts2: any;
  wards2: any;

  chooseCityId: string = '100000';
  chooseDistrictId: string = '101200';

  profile: any;

  shippingCost: number = 0;

  //đơn vị vận chuyển
  shippingUnits: any;

  baseUrl: string = `${Environment.apiBaseUrl}`;

  shippingForm: FormGroup = new FormGroup({
    id: new FormControl(null),
    name: new FormControl('', [Validators.required, Validators.minLength(2), Validators.maxLength(30)]),
    phone: new FormControl('', [Validators.required, Validators.pattern('^(0)[0-9]{9}$')]),
    city: new FormControl(null, [Validators.required]),
    district: new FormControl(null, [Validators.required]),
    ward: new FormControl(null, [Validators.required]),
    addressDetail: new FormControl('', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]),
  });

  orderForm: FormGroup = new FormGroup({
    name: new FormControl('', [Validators.required]),
    phone: new FormControl('', [Validators.required]),
    address: new FormControl(null, [Validators.required]),
    paymentMethod: new FormControl(null, [Validators.required]),
    note: new FormControl('', [Validators.maxLength(100)]),
    orderType: new FormControl(true),

    carrier_name: new FormControl(null, [Validators.required]),
    carrier_logo: new FormControl(null),
    service: new FormControl(null),
    total_fee: new FormControl(0),
  });

  constructor(
    private accountService: AccountService,
    private customerService: CustomerService,
    private cartService: CartService,
    private orderService: OrderService,
    private tokenService: TokenService,
    private addressService: AddressService,
    private toastr: ToastrService,
    private title: Title,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit() {
    this.title.setTitle('Đặt hàng');

    this.getProductSelectedInSessionStorage();
    this.getJsonDataAddress();
    this.getCities();
    this.getDistricts();
    this.getWards();

    this.getCartByAccountEmail();

    this.shippingForm.get('city')?.valueChanges.subscribe((name: any) => {
      this.districts = this.districts2.filter((item: any) => item.city_id === this.getCityId(name));
      this.chooseCityId = this.getCityId(name);
      this.shippingForm.get('district')?.setValue(this.districts[0]?.name);
    });

    this.shippingForm.get('district')?.valueChanges.subscribe((name: any) => {
      this.wards = this.wards2.filter((item: any) => item.district_id === this.getDistrictId(name));
      this.chooseDistrictId = this.getDistrictId(name);
      this.shippingForm.get('ward')?.setValue(this.wards[0]?.name);
    });

    if (this.tokenService.isUserLogin()) {
      this.getProfile();
    }
    this.shippingUnits = this.getRates();
  }

  onSubmitShippingForm() {
    if (this.shippingForm.invalid) {
      this.shippingForm.markAllAsTouched();
      return;
    }
    this.profile = this.shippingForm.value;
    this.orderForm.patchValue(this.profile);
    this.orderForm.get('address')?.setValue(this.profile.addressDetail + ', ' + this.profile.ward + ', ' + this.profile.district + ', ' + this.profile.city);

    this.getRates();
    this.btnCloseModal.nativeElement.click();
  }

  saveInfo() {
    this.shippingForm.get('id')?.setValue(this.profile.id);
    console.log(this.shippingForm.value);
    this.customerService.updateProfile(this.shippingForm.value).subscribe({
      next: (response) => {
        this.toastr.success('Lưu thành công');
        this.profile = this.shippingForm.value;
        this.orderForm.patchValue(this.profile);
        this.orderForm.get('address')?.setValue(this.profile.addressDetail + ', ' + this.profile.ward + ', ' + this.profile.district + ', ' + this.profile.city);
        this.getRates();
        this.btnCloseModal.nativeElement.click();
      },
      error: (error) => {
        console.log(error);
        if (error.status === 400 && error.error === 'DUPPLICATE_PHONE') {
          this.toastr.error('Số điện thoại đã được sử dụng');
        }
        else
          this.toastr.error('Lưu thất bại, vui lòng thử lại sau');
      }
    });
  }

  resetText() { this.shippingForm.reset(); }

  getProductSelectedInSessionStorage() {
    const jsonStr = sessionStorage.getItem('cartDetails');

    // Chuyển đổi JSON thành đối tượng JavaScript
    if (jsonStr) {
      this.selectedProducts = JSON.parse(jsonStr);
      this.calculateTotal();
      console.log(this.selectedProducts);
    }
  }

  // tính toán tổng tiền hàng và tổng tiền giảm giá của tất cả sản phẩm
  calculateTotal(): void {
    this.totalPrice = 0;
    this.totalDiscount = 0;
    this.totalQuantity = 0;

    for (let i = 0; i < this.selectedProducts.length; i++) {
      this.totalPrice += this.selectedProducts[i].totalPrice;
      this.totalQuantity += this.selectedProducts[i].quantity;
      if (this.selectedProducts[i].salePrice !== null) {
        this.totalDiscount += this.selectedProducts[i].totalPrice - this.selectedProducts[i].totalSalePrice;
      }
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

        this.orderForm.patchValue(this.profile);
        this.orderForm.get('address')?.setValue(this.profile.addressDetail + ', ' + this.profile.ward + ', ' + this.profile.district + ', ' + this.profile.city);

        if (this.profile.city === null || this.profile.city === '' || this.profile.district === null || this.profile.district === "") {
          return;
        }
        this.chooseCityId = this.getCityId(this.profile.city);
        this.chooseDistrictId = this.getDistrictId(this.profile.district);
      },
      error: (error) => {
        console.log(error);
      }
    });
  }

  onSubmit() {
    if (this.orderForm.invalid) {
      return;
    }
    this.create();
  }

  create() {
    let orderDto = this.orderForm.value;
    orderDto.paymentStatus = 0;
    orderDto.orderStatus = 0;
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

    this.orderService.create(orderDto).subscribe({
      next: (response: any) => {
        this.toastr.success('Đặt hàng thành công');

        this.updateCartItemCount(this.selectedProducts.length);

        //xóa session storage và sản phẩm đã đặt trong giỏ
        sessionStorage.removeItem('cartDetails');
        this.selectedProducts.forEach((item: any) => {
          this.cartService.deleteCartDetailsById(item.id).subscribe({
            next: () => {
              console.log('Xóa sản phẩm khỏi giỏ hàng thành công');
            }
          });
        });

        if (orderDto.paymentMethod === '0') {
          this.router.navigateByUrl('order/order-success/' + response.id);
        } else if (orderDto.paymentMethod === '1') {
          this.orderService.payment(this.totalPrice + this.shippingCost - this.totalDiscount, response.id).subscribe({
            next: (data: any) => {
              window.location.href = data.redirectUrl;
            },
            error: (error: any) => {
              console.log(error);
              this.toastr.error('Lỗi không xác định, vui lòng thử lại sau');
            }
          });
        }
      },
      error: (error: any) => {
        if (error.status === 400) {
          console.log(error);
          this.toastr.error(error.error);
        }
        else
          this.toastr.error('Lỗi thực hiện, vui lòng thử lại sau');
      }
    });
  }

  getCartByAccountEmail() {
    this.cartService.getCartByAccountEmail(this.tokenService.getUserName()).subscribe({
      next: (response) => {
        this.cart = response;
        this.calculateTotal();
      },
      error: (error) => {
        console.log(error);
      }
    })
  }

  private updateCartItemCount(quantity: number) {
    const itemCount = this.cart.totalProduct - quantity;

    // Gọi phương thức cập nhật số lượng từ CartService
    this.cartService.setCartItemCount(itemCount);
  }


  /// GO SHIP API//////////////////////////////////////////////////////////////////////

  getCities() {
    this.addressService.getJsonDataCity().subscribe({
      next: (response) => {
        this.cities2 = response.data;
      },
      error: (error) => {
        console.log(error);
      }
    });
  }

  getDistricts() {
    this.addressService.getJsonDataDistrict().subscribe({
      next: (response) => {
        this.districts2 = response.data;
      },
      error: (error) => {
        console.log(error);
      }
    });
  }

  getWards() {
    this.addressService.getJsonDataWard().subscribe({
      next: (response) => {
        this.wards2 = response.data;
      },
      error: (error) => {
        console.log(error);
      }
    });
  }

  getCityId(cityName: string): string {
    for (let city of this.cities2) {
      if (city.name === cityName) {
        return city.id;
      }
    }
    return '';
  }

  getDistrictId(districtName: string): string {
    for (let district of this.districts2) {
      if (district.name === districtName) {
        return district.id;
      }
    }
    return '';
  }

  getRates() {
    const data =
    {
      "shipment": {
        "address_from": {
          "city": "100000",
          "district": "101200"
        },
        "address_to": {},
        "parcel": {}
      }
    };
    data.shipment.address_to = {
      "city": this.chooseCityId,
      "district": this.chooseDistrictId
    };

    data.shipment.parcel = {
      "cod": this.totalPrice + this.shippingCost - this.totalDiscount,
      "weight": "500",
      "width": "30",
      "height": "15",
      "length": "3"
    }

    this.addressService.getRates(data).subscribe({
      next: (response) => {
        console.log(response);
        this.shippingUnits = response;
      },
      error: (error) => {
        console.log(error);
      }
    });
  }

  onChooseshippingUnit(item: any) {
    this.orderForm.get('carrier_name')?.setValue(item.carrier_name);
    this.orderForm.get('carrier_logo')?.setValue(item.carrier_logo);
    this.orderForm.get('service')?.setValue(item.service);
    this.orderForm.get('total_fee')?.setValue(item.total_fee);
    this.shippingCost = +item.total_fee;
  }

  /////////////////////////////////////////////////////////////////////////////////////
}

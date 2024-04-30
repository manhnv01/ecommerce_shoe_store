import { Component, ElementRef, HostListener, Input, OnInit, Renderer2, ViewChild } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { DomSanitizer, Title } from '@angular/platform-browser';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Environment } from 'src/app/environment/environment';
import { CustomerService } from 'src/app/service/customer.service';
import { ProductService } from 'src/app/service/product.service';
import { TokenService } from 'src/app/service/token.service';
import Swiper, { SwiperOptions } from 'swiper';
import { ToastrService } from 'ngx-toastr';
import { CartDetailsModel } from 'src/app/model/cart-details.model';
import { CartService } from 'src/app/service/cart.service';
import { CartModel } from 'src/app/model/cart.model';


@Component({
  selector: 'app-user-product-detail',
  templateUrl: './user-product-detail.component.html',
  styleUrls: ['./user-product-detail.component.css']
})
export class UserProductDetailComponent implements OnInit {
  protected readonly Environment = Environment;
  product: any;

  cart: any;
  isImage: string = '';

  cartDetailsList: any[] = [];

  similarProducts: any;

  tag: boolean = false;
  image: string = '';

  currentQuantity: number = 0;

  selectedColor: number = 0;
  selectedSize: number = 0;
  selectedQuantity: number = 1;

  cartDetails: CartDetailsModel;

  sizeList: any;

  baseUrl: string = `${Environment.apiBaseUrl}`;

  constructor(
    private productService: ProductService,
    private title: Title,
    private activatedRoute: ActivatedRoute,
    private sanitizer: DomSanitizer,
    private customerService: CustomerService,
    private cartService: CartService,
    private toastr: ToastrService,
    private elementRef: ElementRef,
    private tokenService: TokenService) {
    this.cartDetails = new CartDetailsModel({});
  }

  // Swiper
  swiperThumbsConfig: SwiperOptions = {
    slidesPerView: 1, // Số lượng slide hiển thị trên một lần trượt
    spaceBetween: 20, // Khoảng cách giữa các slide
    speed: 500, // Tốc độ chuyển slide (milliseconds)
    autoplay: { // Tự động chuyển slide
      delay: 3000, // Thời gian delay giữa các slide (milliseconds)
      disableOnInteraction: false, // Tạm dừng tự động chuyển slide khi người dùng tương tác
    },
    navigation: { // Hiển thị nút điều hướng
      nextEl: '.swiper-button-next', // Nút next
      prevEl: '.swiper-button-prev', // Nút prev
    },
    breakpoints: {
      450: { slidesPerView: 2, spaceBetween: 16 },
      768: { slidesPerView: 3, spaceBetween: 16 },
      992: { slidesPerView: 4, spaceBetween: 16 },
      1200: { slidesPerView: 5, spaceBetween: 16 }
    },
  };

  swiperImageConfig: SwiperOptions = {
    slidesPerView: 1, // Số lượng slide hiển thị trên một lần trượt
    spaceBetween: 16, // Khoảng cách giữa các slide
    navigation: { // Hiển thị nút điều hướng
      nextEl: '.swiper-next', // Nút next
      prevEl: '.swiper-prev', // Nút prev
    },
    breakpoints: {
      200: { slidesPerView: 3, spaceBetween: 16 },
      450: { slidesPerView: 4, spaceBetween: 16 },
      768: { slidesPerView: 4, spaceBetween: 16 },
      992: { slidesPerView: 4, spaceBetween: 16 },
      1200: { slidesPerView: 4, spaceBetween: 16 }
    },
  };

  ngOnInit(): void {
    this.findBySlug(this.activatedRoute.snapshot.params["slug"]);
    const currentDate = new Date();
    const createdAt = new Date(this.product?.createdAt);
    const diff = currentDate.getTime() - createdAt.getTime();
    const diffDays = Math.ceil(diff / (1000 * 3600 * 24));
    if (diffDays <= 7) {
      this.tag = true;
    }

    // cuộn về đầu trang mượt smooth
    this.scrollToTop();
  }

  scrollToTop() {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  findBySlug(slug: string) {
    this.productService.findBySlug(slug).subscribe({
      next: (data: any) => {
        this.product = data;
        this.currentQuantity = data.totalQuantity;
        this.image = data.thumbnail;
        this.sizeList = this.product?.productColors[0]?.productDetails;
        this.title.setTitle(this.product?.name);
        this.similarProduct(data.categoryId, data.brandId);

        // lưu lịch sử xem sản phẩm vào session storage
        // kiểm tra xem sản phẩm này đã có trong danh sách sản phẩm đã xem chưa
        // kiểm tra xem session storage đã tồn tại chưa
        let viewedProduct = JSON.parse(sessionStorage.getItem('viewedProduct') || '[]');
        let index = viewedProduct.findIndex((x: any) => x.id == data.id);

        if (index === -1) {
          viewedProduct.push(data);
          sessionStorage.setItem('viewedProduct', JSON.stringify(viewedProduct));
        } else {
          viewedProduct.splice(index, 1);
          viewedProduct.push(data);
          sessionStorage.setItem('viewedProduct', JSON.stringify(viewedProduct));
        }
      },
      error: (error: any) => {
        console.log(error);
        if (error.status === 400 && error.error === 'PRODUCT_NOT_FOUND') {
          this.toastr.error('Không tìm thấy sản phẩm này');
          window.location.href = '/product';
        }
      }
    });
  }

  similarProduct(categoryId: number, brandId: number) {
    this.productService.similarProduct(categoryId, brandId).subscribe({
      next: (data: any) => {
        this.similarProducts = data;
        // xóa sản phẩm hiện tại khỏi danh sách sản phẩm tương tự
        this.similarProducts = this.similarProducts.filter((x: any) => x.id != this.product.id);
      },
      error: (error: any) => {
        console.log(error);
      }
    });
  }

  plus() {
    if (this.selectedQuantity < this.currentQuantity) {
      this.selectedQuantity++;
    }
    else {
      this.toastr.error('Số lượng sản phẩm đã đạt giới hạn');
    }
  }

  minus() {
    if (this.selectedQuantity > 1) {
      this.selectedQuantity--;
    }
  }

  buyNow() {
    if (!this.tokenService.isUserLogin()) {
      this.toastr.error('Vui lòng đăng nhập để mua hàng');
      return;
    }

    if (this.selectedColor == 0 || this.selectedSize == 0) {
      this.toastr.error('Vui lòng chọn màu sắc và kích cỡ');
      return;
    }

    // làm sạch session storage
    sessionStorage.removeItem('cartDetails');
    this.cartDetailsList = [];

    this.cartDetails.productDetailsId = this.selectedSize;
    this.cartDetails.quantity = this.selectedQuantity;
    this.cartDetails.productColor = this.product?.productColors.find((x: any) => x.id == this.selectedColor)?.color;
    this.cartDetails.productSize = this.sizeList.find((x: any) => x.id == this.selectedSize)?.size;
    this.cartDetails.productName = this.product?.name;
    this.cartDetails.productPrice = this.product?.price;
    this.cartDetails.productThumbnail = this.product?.thumbnail;
    this.cartDetails.productSlug = this.product?.slug;
    this.cartDetails.salePrice = this.product?.salePrice;
    this.cartDetails.totalPrice = this.product?.price * this.selectedQuantity;
    this.cartDetails.totalSalePrice = this.product?.salePrice * this.selectedQuantity;

    this.cartDetailsList.push(this.cartDetails);

    sessionStorage.removeItem('cartDetails');

    // lưu sản phẩm vào session storage
    sessionStorage.setItem('cartDetails', JSON.stringify(this.cartDetailsList));
    window.location.href = '/check-out';
  }

  chooseImage(image: any) {
    this.image = image;
    this.isImage = image;
  }

  onRadioColorChange(event: any) {
    this.selectedSize = 0;
    this.sizeList = this.product?.productColors.find((x: any) => x.id == this.selectedColor)?.productDetails;
  }

  onRadioSizeChange(event: any) {
    this.currentQuantity = this.sizeList.find((x: any) => x.id == this.selectedSize)?.quantity;
    if (this.selectedQuantity > this.currentQuantity) {
      this.selectedQuantity = this.currentQuantity;
    }
  }

  getHtmlProductDescription() {
    if (this.product?.description == '' || this.product?.description == null) {
      return 'Chưa có thông tin mô tả sản phẩm.';
    }
    return this.sanitizer.bypassSecurityTrustHtml(this.product?.description);
  }

  addToCart() {

    if (this.selectedColor == 0 || this.selectedSize == 0) {
      this.toastr.error('Vui lòng chọn màu sắc và kích cỡ');
      return;
    }

    this.cartDetails.productDetailsId = this.selectedSize;
    this.cartDetails.quantity = this.selectedQuantity;

    if (!this.tokenService.isUserLogin()) {
      this.toastr.error('Vui lòng đăng nhập để thêm vào giỏ hàng');
      return;
    }
    if (this.tokenService.isLogin() && !this.tokenService.isTokenExpired()) {
      this.cartService.addToCart(this.cartDetails).subscribe({
        next: (data: any) => {
          window.location.href = '/cart';
          this.toastr.success('Thêm vào giỏ thành công');
        },
        error: (error: any) => {
          console.log(error);
          this.toastr.error('Có lỗi xảy ra, vui lòng thử lại sau');
        }
      });
    }
  }

  scrollToTarget() {
    const targetElement = this.elementRef.nativeElement.querySelector('#targetElement');
    if (targetElement) {
      targetElement.scrollIntoView({ behavior: 'smooth', block: 'start' });
    }
  }
}
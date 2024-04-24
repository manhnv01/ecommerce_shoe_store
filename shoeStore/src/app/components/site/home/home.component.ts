import { Component, HostListener, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Environment } from 'src/app/environment/environment';
import { BrandModel } from 'src/app/model/brand.model';
import { BrandService } from 'src/app/service/brand.service';
import { ProductService } from 'src/app/service/product.service';
import Swiper, { SwiperOptions } from 'swiper';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  baseUrl: string = `${Environment.apiBaseUrl}`;

  brands: BrandModel[] = [];

  productInSale: any;
  productNewest: any;
  viewedProducts: any;

  isLogin: boolean = false;

  // tạo mảng chứ text
  //../../../../assets/bannerhome.jpg

  banners: string[] = [
    "../../../../assets/bannerhome.jpg",
    "../../../../assets/bannerhome2.webp",
    "../../../../assets/bannerhome3.jpg",
  ];

  constructor(
    private title: Title,
    private brandService: BrandService,
    private productService: ProductService,
  ) { }

  // Swiper
  swiperNewestConfig: SwiperOptions = {
    slidesPerView: 1,
    spaceBetween: 20,
    speed: 900,
    navigation: { // Hiển thị nút điều hướng
      nextEl: '.swiper-button-next', // Nút next
      prevEl: '.swiper-button-prev', // Nút prev
    },
    breakpoints: {
      450: { slidesPerView: 1, spaceBetween: 16 },
      768: { slidesPerView: 1, spaceBetween: 16 },
      992: { slidesPerView: 5, spaceBetween: 16 },
      1200: { slidesPerView: 5, spaceBetween: 16 }
    },
  };

  // Swiper
  swiperSaleConfig: SwiperOptions = {
    slidesPerView: 1,
    spaceBetween: 16,
    speed: 500,
    autoplay: { // Tự động chuyển slide
      delay: 2000, // Thời gian delay giữa các slide (milliseconds)
    },
    slidesPerGroup: 1, // Số lượng slide được chuyển mỗi lần
    loopedSlides: 1,
    breakpoints: {
      450: { slidesPerView: 1 },
      576: { slidesPerView: 2 },
      768: { slidesPerView: 3 },
      992: { slidesPerView: 4 },
      1200: { slidesPerView: 5 },
    },  
  };

  // Swiper
  swiperviewedConfig: SwiperOptions = {
    slidesPerView: 1,
    spaceBetween: 20,
    allowTouchMove: false, // Cho phép chạm để chuyển slide
    speed: 500,
    autoplay: { // Tự động chuyển slide
      delay: 2000, // Thời gian delay giữa các slide (milliseconds)
    },
    breakpoints: {
      450: { slidesPerView: 1, spaceBetween: 16 },
      768: { slidesPerView: 1, spaceBetween: 16 },
      992: { slidesPerView: 5, spaceBetween: 16 },
      1200: { slidesPerView: 5, spaceBetween: 16 }
    },
  };

  // Swiper
  swiperBannerConfig: SwiperOptions = {
    slidesPerView: 1, // Số lượng slide hiển thị trên một lần trượt
    spaceBetween: 20, // Khoảng cách giữa các slide
    speed: 500, // Tốc độ chuyển slide (milliseconds)
    autoplay: { // Tự động chuyển slide
      delay: 4000, // Thời gian delay giữa các slide (milliseconds)
    },
    allowTouchMove: false, // Cho phép chạm để chuyển slide
    loop: true,
  };

  ngOnInit() {
    this.title.setTitle('Trang chủ');
    this.getBrands();
    this.getProductNewest();
    this.getViewedProduct();
    this.getProductInSale();

    this.isLogin = localStorage.getItem('token') ? true : false;
  }

  getViewedProduct() {
    this.viewedProducts = JSON.parse(sessionStorage.getItem('viewedProduct') || '[]');
    console.log(this.viewedProducts);
  }

  getBrands() {
    this.brandService.getAll().subscribe({
      next: (response) => {
        console.log(response);
        this.brands = response;
      }
    });
  }

  getProductNewest() {
    this.productService.newest().subscribe({
      next: (response) => {
        console.log(response);
        this.productNewest = response;
      }
    });
  }

  getProductInSale() {
    this.productService.inSale().subscribe({
      next: (response) => {
        console.log(response);
        this.productInSale = response;
      }
    });
  }
}

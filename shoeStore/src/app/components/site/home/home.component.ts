import { Component, OnInit } from '@angular/core';
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

  productNewest: any;

  constructor(
    private title: Title,
    private brandService: BrandService,
    private productService: ProductService,
  ) { }

  // Swiper
  swiperNewestConfig: SwiperOptions = {
    slidesPerView: 1,
    spaceBetween: 20,
    speed: 500,
    navigation: { // Hiển thị nút điều hướng
      nextEl: '.swiper-button-next', // Nút next
      prevEl: '.swiper-button-prev', // Nút prev
    },
    breakpoints: {
      450: { slidesPerView: 1, spaceBetween: 16 },
      768: { slidesPerView: 1, spaceBetween: 16 },
      992: { slidesPerView: 5, spaceBetween: 16 },
      1200: { slidesPerView: 6, spaceBetween: 16 }
    },
  };

  ngOnInit() {
    this.title.setTitle('Trang chủ');
    this.getBrands();
    this.getProductNewest();
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
}

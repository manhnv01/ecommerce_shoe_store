import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import { PaginationModel } from 'src/app/model/pagination.model';
import { ProductService } from 'src/app/service/product.service';
import { Subscription } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { Environment } from 'src/app/environment/environment';
import { BrandModel } from 'src/app/model/brand.model';
import { BrandService } from 'src/app/service/brand.service';
import { CategoryModel } from 'src/app/model/category.model';
import { CategoryService } from 'src/app/service/category.service';


@Component({
  selector: 'app-user-product',
  templateUrl: './user-product.component.html',
  styleUrls: ['./user-product.component.css'],
})
export class UserProductComponent implements OnInit {

  paginationModel: PaginationModel;

  isFiltering: boolean = false;

  brands: BrandModel[] = [];
  chooseBrands: string[] = [];

  categories: CategoryModel[] = [];
  chooseCategories: string[] = [];

  chooseProductSizes: string[] = [];

  sortedId: any;

  priceMin: number = 0;
  priceMax: number = 10000000;

  // tạo 1 mảng kichx cỡ sản phẩm từ 34-44
  productSizes: string[] = ['34', '35', '36', '37', '38', '39', '40', '41', '42', '43', '44'];

  baseUrl: string = `${Environment.apiBaseUrl}`;;

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private toastr: ToastrService,
    private brandService: BrandService,
    private categoryService: CategoryService,
    private productService: ProductService,
    private title: Title
  ) {
    this.title.setTitle('Sản phẩm');
    this.paginationModel = new PaginationModel({});
  }

  private findAllSubscription: Subscription | undefined;

  ngOnInit() {
    this.getBrands();
    this.getCategories();
    this.getUriParams();

    this.activatedRoute.queryParams.subscribe((params) => {
      const { size = 20, page = 1, 'sort-direction': sortDir = 'ASC', 'sort-by': sortBy = 'id' } = params;
      this.filter(+page, +size, sortDir, sortBy);
    });
  }

  ngOnDestroy(): void {
    if (this.findAllSubscription) {
      this.findAllSubscription.unsubscribe();
    }
  }

  // lấy dữ liệu từ url
  getUriParams(): void {
    // lấy categories từ url nếu có gán vào chooseCategories
    const categories = this.activatedRoute.snapshot.queryParamMap.getAll('category');
    if (categories.length > 0) {
      this.chooseCategories = categories;
    }

    // lấy brands từ url nếu có gán vào chooseBrands
    const brands = this.activatedRoute.snapshot.queryParamMap.getAll('brand');
    if (brands.length > 0) {
      this.chooseBrands = brands;
    }

    // lấy product-size từ url nếu có gán vào chooseProductSizes
    const productSizes = this.activatedRoute.snapshot.queryParamMap.getAll('product-size');
    if (productSizes.length > 0) {
      this.chooseProductSizes = productSizes;
    }

    //lấy sort-by và sort-direction từ url nếu có gán vào sortedId
    const sortBy = this.activatedRoute.snapshot.queryParamMap.get('sort-by');
    const sortDir = this.activatedRoute.snapshot.queryParamMap.get('sort-direction');

    // Xác định giá trị của selectedValue dựa trên sortBy và sortDir
    if (sortBy === 'id' && sortDir === 'ASC') {
      this.sortedId = 1;
    } else if (sortBy === 'price' && sortDir === 'ASC') {
      this.sortedId = 2;
    } else if (sortBy === 'price' && sortDir === 'DESC') {
      this.sortedId = 3;
    } else if (sortBy === 'name' && sortDir === 'ASC') {
      this.sortedId = 4;
    } else if (sortBy === 'name' && sortDir === 'DESC') {
      this.sortedId = 5;
    } else if (sortBy === 'createdAt' && sortDir === 'ASC') {
      this.sortedId = 6;
    } else if (sortBy === 'createdAt' && sortDir === 'DESC') {
      this.sortedId = 7;
    } else {
      this.sortedId = 1;
    }

    // lấy price-min và price-max từ url nếu có gán vào priceMin và priceMax
    const priceMin = this.activatedRoute.snapshot.queryParamMap.get('price-min');
    if (priceMin) {
      this.priceMin = +priceMin;
    }

    const priceMax = this.activatedRoute.snapshot.queryParamMap.get('price-max');
    if (priceMax) {
      this.priceMax = +priceMax;
    }
  }

  scrollToTop(): void {
    // Cuộn về đầu trang
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  startFilter() {
    this.scrollToTop();
    this.router.navigate([], {
      queryParams: {
        'brand': this.chooseBrands,
        'category': this.chooseCategories,
        'product-size': this.chooseProductSizes,
        'price-min': this.priceMin,
        'price-max': this.priceMax
      }, queryParamsHandling: 'merge'
    }).then(() => { });
    this.filter(1, this.paginationModel.pageSize, 'ASC', 'id');
  }

  filter(page: number, size: number, sortDir: string, sortBy: string): void {

    if (this.chooseBrands.length > 0 
      || this.chooseCategories.length > 0 
      || this.chooseProductSizes.length > 0 
      || this.priceMin > 0 
      || this.priceMax < 10000000) {
      this.isFiltering = true;
    }
    else {
      this.isFiltering = false;
    }

    this.productService.findAllAndFilterAndSort(size, page, sortDir, sortBy, this.chooseBrands, this.chooseCategories, this.chooseProductSizes, this.priceMin, this.priceMax).subscribe({
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
      },
      error: (error: any) => {
        console.log(error);
      }
    });
  }

  onMinChange(event: any) {
    const minValue = event.target.value; // Lấy giá trị của đầu chọn min
    this.priceMin = minValue;
    console.log('Min value:', minValue);
  }

  onMaxChange(event: any) {
    const maxValue = event.target.value; // Lấy giá trị của đầu chọn max
    this.priceMax = maxValue;
    console.log('Max value:', maxValue);
  }

  onChangeSort(event: any): void {
    const selectedValue = event.target.value;
    if (selectedValue == 1) this.changeSort('id', 'ASC');
    if (selectedValue == 2) this.changeSort('price', 'ASC');
    if (selectedValue == 3) this.changeSort('price', 'DESC');
    if (selectedValue == 4) this.changeSort('name', 'ASC');
    if (selectedValue == 5) this.changeSort('name', 'DESC');
    if (selectedValue == 6) this.changeSort('createdAt', 'ASC');
    if (selectedValue == 7) this.changeSort('createdAt', 'DESC');
  }

  clearAllParams(): void {
    const navigationExtras: NavigationExtras = {
      queryParams: {},
    };
    this.router.navigate([], navigationExtras);
  }

  changeSort(sortBy: string, sortDir: string): void {
    this.router.navigate([], { queryParams: { 'sort-direction': sortDir, 'sort-by': sortBy }, queryParamsHandling: 'merge' }).then(() => { });
  }

  changePageSize(pageSize: number): void {
    this.router.navigate([], { queryParams: { size: pageSize, page: 1 }, queryParamsHandling: 'merge' }).then(() => { });
  }
  changePageNumber(pageNumber: number): void {
    if (pageNumber === this.paginationModel.pageNumber) return;
    this.router.navigate([], { queryParams: { page: pageNumber }, queryParamsHandling: 'merge' }).then(() => { });
  }

  getBrands() {
    this.brandService.getAll().subscribe({
      next: (response) => {
        this.brands = response;
      }
    });
  }

  isSelectedBrand(brand: string): boolean {
    return this.chooseBrands.findIndex(c => c === brand) !== -1;
  }

  onChooseBrand(brandSlug: string): void {
    const index = this.chooseBrands.findIndex(c => c === brandSlug);
    const isChecked = index !== -1;

    if (!isChecked) {
      this.chooseBrands.push(brandSlug);
    } else {
      this.chooseBrands.splice(index, 1);
    }
    this.startFilter();
  }

  getCategories() {
    this.categoryService.getAll().subscribe({
      next: (response) => {
        this.categories = response;
      }
    });
  }

  isSelectedCategory(categorySlug: string): boolean {
    return this.chooseCategories.findIndex(c => c === categorySlug) !== -1;
  }

  onChooseCategory(categorySlug: string): void {
    const index = this.chooseCategories.findIndex(c => c === categorySlug);
    const isChecked = index !== -1;

    if (!isChecked) {
      this.chooseCategories.push(categorySlug);
    } else {
      this.chooseCategories.splice(index, 1);
    }
    this.startFilter();
  }

  isSelectedProductSize(sizeName: string): boolean {
    return this.chooseProductSizes.findIndex(c => c === sizeName) !== -1;
  }

  onChooseProductSize(sizeName: string): void {
    const index = this.chooseProductSizes.findIndex(c => c === sizeName);
    const isChecked = index !== -1;

    if (!isChecked) {
      this.chooseProductSizes.push(sizeName);
    } else {
      this.chooseProductSizes.splice(index, 1);
    }
    this.startFilter();
  }

  clearFilter(): void {
    this.chooseBrands = [];
    this.chooseCategories = [];
    this.chooseProductSizes = [];
    this.priceMin = 0;
    this.priceMax = 10000000;

    this.clearAllParams();
  }
}

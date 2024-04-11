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

  brands: BrandModel[] = [];
  chooseBrands: string[] = [];

  categories: CategoryModel[] = [];
  chooseCategories: string[] = [];

  priceMin: number = 0;
  priceMax: number = 10000000;

  sortId: number = 1;

  brandSlug: string = '';

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

  ngOnInit() {
    this.brandSlug = this.activatedRoute.snapshot.params["slug"];

    this.getBrands();
    this.getCategories();

    this.getUriParams();

    this.activatedRoute.queryParams.subscribe((params) => {
      const { size = 20, page = 1, 'sort-direction': sortDir = 'ASC', 'sort-by': sortBy = 'id' } = params;
      this.filter(+page, +size, sortDir, sortBy);

      // if (this.brandSlug !== null && this.brandSlug !== undefined) {
      //   //this.findAllByEnabledIsTrueAnd_Slug(+page, +size, sortDir, sortBy, this.brandSlug);
      //   this.filter(+page, +size, sortDir, sortBy);
      // } else {
      //   //this.findAllByEnabledIsTrue(+page, +size, sortDir, sortBy);
      //   this.filter(+page, +size, sortDir, sortBy);
      // }
    });
  }

  // lấy dữ liệu từ url
  getUriParams(): void {
    // lấy categories từ url nếu có gán vào chooseCategories
    const categories = this.activatedRoute.snapshot.queryParamMap.getAll('categories');
    if (categories.length > 0) {
      this.chooseCategories = categories;
    }

    // lấy brands từ url nếu có gán vào chooseBrands
    const brands = this.activatedRoute.snapshot.queryParamMap.getAll('brands');
    if (brands.length > 0) {
      this.chooseBrands = brands;
    }
  }

  loc() {
    this.router.navigate([], { queryParams: { 'brands': this.chooseBrands, 'categories': this.chooseCategories }, queryParamsHandling: 'merge' }).then(() => { });
    this.filter(1, 20, 'ASC', 'id');
  }

  filter(page: number, size: number, sortDir: string, sortBy: string): void {
    this.productService.findAllAndFilterAndSort(size, page, sortDir, sortBy, this.chooseBrands, this.chooseCategories, this.priceMin, this.priceMax).subscribe({
      next: (response: any) => {
        this.get(response);
      },
      error: (error: any) => {
        console.log(error);
      }
    });
  }

  get(response: any) {
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
  } 

  findAllByEnabledIsTrueAnd_Slug(page: number, size: number, sortDir: string, sortBy: string, brandSlug: string): void {
    this.productService.findAllByEnabledIsTrueAnd_Slug(page, size, sortDir, sortBy, brandSlug).subscribe({
      next: (response: any) => {
       this.get(response);
      },
      error: (error: any) => {
        console.log(error);
      }
    });
  }

  ngOnDestroy(): void {
    if (this.findAllSubscription) {
      this.findAllSubscription.unsubscribe();
    }
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

    this.sortId = selectedValue;
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

  private findAllSubscription: Subscription | undefined;

  findAllByEnabledIsTrue(page: number = 1, pageSize: number = this.paginationModel.pageSize, sortDir: string = 'ASC', sortBy: string = 'id'): void {
    this.findAllSubscription = this.productService.findAllByEnabledIsTrue(page, pageSize, sortDir, sortBy).subscribe({
      next: (response: any) => {
        this.get(response);
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

  onChooseBrand(brandSlug: string): void{
    const index = this.chooseBrands.findIndex(c => c === brandSlug);
    const isChecked = index !== -1;

    if (!isChecked) {
      this.chooseBrands.push(brandSlug);
    } else {
      this.chooseBrands.splice(index, 1);
    }

    this.loc();

    console.log("chooseBrands: ", this.chooseBrands);
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

    this.loc();

    console.log("chooseCategories: ", this.chooseCategories);
  }

  clearFilter(): void {
    this.chooseBrands = [];
    this.chooseCategories = [];
    this.priceMin = 0;
    this.priceMax = 10000000;
  }
}

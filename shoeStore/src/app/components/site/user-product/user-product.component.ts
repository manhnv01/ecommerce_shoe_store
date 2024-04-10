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
  styleUrls: ['./user-product.component.css']
})
export class UserProductComponent implements OnInit {

  paginationModel: PaginationModel;

  brands: BrandModel[] = [];
  chooseBrands: BrandModel[] = [];

  categories: CategoryModel[] = [];
  chooseCategories: CategoryModel[] = [];

  search: string = '';

  sortId: number = 1;

  brandSlug: string = '';

  baseUrl: string = `${Environment.apiBaseUrl}`;

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

  ngOnInit() {
    this.brandSlug = this.activatedRoute.snapshot.params["slug"];

    this.getBrands();
    this.getCategories();

    this.activatedRoute.queryParams.subscribe((params) => {
      const { search = '', size = 20, page = 1, 'sort-direction': sortDir = 'ASC', 'sort-by': sortBy = 'id' } = params;

      if (this.brandSlug !== null && this.brandSlug !== undefined) {
        this.findAllByEnabledIsTrueAnd_Slug(+page, +size, sortDir, sortBy, this.brandSlug);
      } else {
        this.findAllByEnabledIsTrue(+page, +size, sortDir, sortBy);
      }
    });
  }


  findAllByEnabledIsTrueAnd_Slug(page: number, size: number, sortDir: string, sortBy: string, brandSlug: string): void {
    this.productService.findAllByEnabledIsTrueAnd_Slug(page, size, sortDir, sortBy, brandSlug).subscribe({
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


  ngOnDestroy(): void {
    if (this.findAllSubscription) {
      this.findAllSubscription.unsubscribe();
    }
  }

  onChangeSort(event: any): void {
    const selectedValue = event.target.value;
    if (selectedValue == 1) this.clearAllParams();
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

  isSelectedBrand(brand: any): boolean {
    return this.chooseBrands.findIndex(c => c.id === brand.id) !== -1;
  }

  onChooseBrand(brand: any): void {
    const index = this.chooseBrands.findIndex(c => c.id === brand.id);
    const isChecked = index !== -1;

    if (!isChecked) {
      this.chooseBrands.push(brand);
    } else {
      this.chooseBrands.splice(index, 1);
    }

    console.log("chooseBrands: ", this.chooseBrands);
  }

  getCategories() {
    this.categoryService.getAll().subscribe({
      next: (response) => {
        this.categories = response;
      }
    });
  }

  isSelectedCategory(category: any): boolean {
    return this.chooseCategories.findIndex(c => c.id === category.id) !== -1;
  }

  onChooseCategory(category: any): void {
    const index = this.chooseCategories.findIndex(c => c.id === category.id);
    const isChecked = index !== -1;

    if (!isChecked) {
      this.chooseCategories.push(category);
    } else {
      this.chooseCategories.splice(index, 1);
    }

    console.log("chooseCategories: ", this.chooseCategories);
  }
}

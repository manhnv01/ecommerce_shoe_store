import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import { PaginationModel } from 'src/app/model/pagination.model';
import { ProductService } from 'src/app/service/product.service';
import { Subscription } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { Environment } from 'src/app/environment/environment';


@Component({
  selector: 'app-user-product',
  templateUrl: './user-product.component.html',
  styleUrls: ['./user-product.component.css']
})
export class UserProductComponent implements OnInit {

  paginationModel: PaginationModel;

  search: string = '';

  sortId: number = 1;

  baseUrl: string = `${Environment.apiBaseUrl}`;

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private toastr: ToastrService,
    private productService: ProductService,
    private title: Title
  ) { 
    this.title.setTitle('Sản phẩm');
    this.paginationModel = new PaginationModel({});
  }

  ngOnInit() {
    this.activatedRoute.queryParams.subscribe((params) => {
      const { search = '', size = 20, page = 1, 'sort-direction': sortDir = 'ASC', 'sort-by': sortBy = 'id' } = params;

      this.findAllByEnabledIsTrue(+page, +size, sortDir, sortBy);
    });
  }

  ngOnDestroy(): void {
    if (this.findAllSubscription) {
      this.findAllSubscription.unsubscribe();
    }
  }
  
  onChangeSort(event: any): void {
    const selectedValue = event.target.value;
    console.log(selectedValue);
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
        console.log(this.paginationModel.content);
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
}

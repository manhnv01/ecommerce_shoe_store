import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import { Environment } from 'src/app/environment/environment';
import { PaginationModel } from 'src/app/model/pagination.model';
import { SaleModel } from 'src/app/model/sale.model';
import { ProductService } from 'src/app/service/product.service';
import Swal from 'sweetalert2';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { SaleService } from 'src/app/service/sale.service';

@Component({
  selector: 'app-list-sale',
  templateUrl: './list-sale.component.html',
  styleUrls: ['./list-sale.component.css']
})
export class ListSaleComponent implements OnInit {
  paginationModel: PaginationModel;
  sale: any;
  search: string = '';
  sales: SaleModel[] = [];
  count: number = 0;
  total: number = 0;

  private findAllSubscription: Subscription | undefined;

  constructor(
    private productService: ProductService,
    private saleService: SaleService,
    private toastr: ToastrService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private title: Title
  ) {
    this.title.setTitle('Quản lý khuyến mãi');
    this.paginationModel = new PaginationModel({});
  }

  ngOnInit(): void {
    this.getTotals();
    this.activatedRoute.queryParams.subscribe((params) => {
      const { search = '', size = 5, page = 1, 'sort-direction': sortDir = 'ASC', 'sort-by': sortBy = 'id' } = params;

      this.findAll(+page, +size, sortDir, sortBy, search);
    });
    this.search = this.activatedRoute.snapshot.queryParams['search'] || '';
  }

  ngOnDestroy(): void {
    if (this.findAllSubscription) {
      this.findAllSubscription.unsubscribe();
    }
  }

  updateSale(id: number) {
    this.router.navigate(['admin/sale/save', id]);
  }

  isSelected(product: SaleModel): boolean {
    return this.sales.findIndex(c => c.id === product.id) !== -1;
  }


  onCheckboxChange(product: SaleModel) {
    const index = this.sales.findIndex(c => c.id === product.id);

    if (index === -1) {
      this.sales.push(product);
    } else {
      this.sales.splice(index, 1);
    }
  }

  getTotals() {
    this.saleService.getTotals().subscribe({
      next: (response: any) => {
        console.log(response);
        this.total = response.total;
      },
      error: (error: any) => {
        console.log(error);
      }
    });
  }

  findAll(page: number = 1, pageSize: number = this.paginationModel.pageSize, sortDir: string = 'ASC', sortBy: string = 'id', search: string = this.search): void {
    this.findAllSubscription = this.saleService.findAll(page, pageSize, sortDir, sortBy, search).subscribe({
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
        this.getTotals()
      },
      error: (error: any) => {
        console.log(error);
      }
    });
  }

  changePageSize(pageSize: number): void {
    this.router.navigate([], { queryParams: { size: pageSize, page: 1 }, queryParamsHandling: 'merge' }).then(() => { });
    this.sales = [];
  }
  changePageNumber(pageNumber: number): void {
    if (pageNumber === this.paginationModel.pageNumber) return;
    this.sales = [];
    this.router.navigate([], { queryParams: { page: pageNumber }, queryParamsHandling: 'merge' }).then(() => { });
  }
  searchItem(): void {
    this.router.navigate([], { queryParams: { search: this.search, page: 1 }, queryParamsHandling: 'merge' }).then(() => { });
  }
  changeSort(sortBy: string): void {
    let sortDir = 'ASC';
    if (this.activatedRoute.snapshot.queryParams['sort-direction'] === sortDir) {
      sortDir = sortDir === 'ASC' ? 'DESC' : 'ASC';
    }
    this.router.navigate([], { queryParams: { 'sort-direction': sortDir, 'sort-by': sortBy }, queryParamsHandling: 'merge' }).then(() => { });
  }

  iconClass(sortBy: string): number {
    const sortBy2 = this.activatedRoute.snapshot.queryParams['sort-by'];
    const sortDir = this.activatedRoute.snapshot.queryParams['sort-direction'];
    if (sortDir === 'ASC' && sortBy2 === sortBy) return 1;
    else if (sortDir === 'DESC' && sortBy2 === sortBy) return 2;
    else return 0;
  }

  handleSuccess(): void {
    this.sales = [];
    const sortDir = this.activatedRoute.snapshot.queryParams['sort-direction'];
    const sortBy = this.activatedRoute.snapshot.queryParams['sort-by'];
    this.findAll(this.paginationModel.pageNumber, this.paginationModel.pageSize, sortDir, sortBy, this.search);
  }
  clearAllParams(): void {
    const navigationExtras: NavigationExtras = {
      queryParams: {},
    };
    this.router.navigate([], navigationExtras);
    this.handleSuccess();
    this.search = '';
  }

  deleteSale(id: number) {
    Swal.fire({
      title: 'Bạn có chắc chắn muốn xóa?',
      text: 'Dữ liệu sẽ không thể phục hồi sau khi xóa!',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Xác nhận',
      cancelButtonText: 'Hủy',
      buttonsStyling: false,
      customClass: {
        confirmButton: 'btn btn-danger me-1',
        cancelButton: 'btn btn-secondary'
      },
    }).then((result) => {
      if (result.isConfirmed) {
        this.saleService.delete(id).subscribe({
          next: () => {
            this.handleSuccess();
            this.toastr.success('Xóa khuyến mãi thành công', 'Thông báo');
            if (this.paginationModel.pageLast && this.paginationModel.content.length === 1 && this.paginationModel.pageNumber > 1)
                    this.router.navigate([], { queryParams: { page: this.paginationModel.pageNumber - 1 }, queryParamsHandling: 'merge' }).then(() => { });
          },
          error: (error: any) => {
            console.log(error);
            if (error.status === 400 && error.error === 'SALE_NOT_FOUND')
              this.toastr.error(`Không tìm thấy khuyến mãi để xóa!`, 'Thông báo');
            else
              this.toastr.error(`Xóa thất bại, Lỗi không xác định!`, 'Thông báo');
          }
        });
      }
    })
  }

  deletelist(): void {
    if (this.sales.length == 0)
      this.toastr.info('Bạn chưa chọn khuyến mãi để xóa!', 'Thông báo');
    else {
      Swal.fire({
        title: 'Bạn có chắc chắn muốn xóa?',
        text: 'Dữ liệu sẽ không thể phục hồi sau khi xóa!',
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
          this.count = 0;
          for (let i = 0; i < this.sales.length; i++) {
            const item = this.sales[i];
            let listLength = this.sales.length;
            this.saleService.delete(item.id).subscribe({
              next: (response: any) => {
                this.handleSuccess();
                this.count++;
                if (listLength === this.count){
                  this.toastr.success(`Xóa ${this.count} mục thành công!`, 'Thông báo');
                  if (this.paginationModel.pageLast && this.paginationModel.content.length <= listLength && this.paginationModel.pageNumber > 1)
                  this.router.navigate([], { queryParams: { page: this.paginationModel.pageNumber - 1 }, queryParamsHandling: 'merge' }).then(() => { });
                }
              },
              error: (error: any) => {
                console.log(error);
                if (error.status === 400 && error.error === 'SALE_NOT_FOUND')
                  this.toastr.error(`Không tìm thấy khuyến mãi "${item.name}" để xóa!`, 'Thông báo');
                else
                  this.toastr.error(`Xóa "${item.name}" thất bại, Lỗi không xác định!`, 'Thông báo');
              }
            });
          }
          this.sales = [];
        }
      });
    }
  }
}


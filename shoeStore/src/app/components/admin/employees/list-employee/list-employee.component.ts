import { Component, OnInit } from '@angular/core';
import { Environment } from 'src/app/environment/environment';
import { EmployeeModel } from 'src/app/model/employee.model';
import { PaginationModel } from 'src/app/model/pagination.model';
import { ToastrService } from 'ngx-toastr';
import { ActivatedRoute, Router, NavigationExtras } from '@angular/router';
import { Subscription } from 'rxjs';
import Swal from 'sweetalert2';
import { Title } from '@angular/platform-browser';
import { EmployeeService } from 'src/app/service/employee.service';

@Component({
  selector: 'app-list-employee',
  templateUrl: './list-employee.component.html',
  styleUrls: ['./list-employee.component.css']
})
export class ListEmployeeComponent implements OnInit {
  paginationModel: PaginationModel;
  product: any;
  search: string = '';
  status: string = '';
  employees: EmployeeModel[] = [];
  count: number = 0;

  baseUrl: string = `${Environment.apiBaseUrl}`;

  total: number = 0;
  totalEnabled: number = 0;
  totalDisabled: number = 0;

  private findAllSubscription: Subscription | undefined;

  constructor(
    private employeeService: EmployeeService,
    private toastr: ToastrService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private title: Title
  ) {
    this.title.setTitle('Quản lý nhân viên');
    this.paginationModel = new PaginationModel({});
  }

  ngOnInit(): void {
    this.getTotals();
    this.activatedRoute.queryParams.subscribe((params) => {
      const { search = '', size = 5, page = 1, 'sort-direction': sortDir = 'ASC', 'sort-by': sortBy = 'id' } = params;

      this.findAll(+page, +size, sortDir, sortBy, search);
    });
  }

  ngOnDestroy(): void {
    if (this.findAllSubscription) {
      this.findAllSubscription.unsubscribe();
    }
  }

  update(id: number) {
    this.router.navigate(['admin/employee/save', id]);
  }

  isSelected(product: EmployeeModel): boolean {
    return this.employees.findIndex(c => c.id === product.id) !== -1;
  }


  onCheckboxChange(product: EmployeeModel) {
    const index = this.employees.findIndex(c => c.id === product.id);

    if (index === -1) {
      this.employees.push(product);
    } else {
      this.employees.splice(index, 1);
    }
  }

  getTotals() {
    this.employeeService.getTotals().subscribe({
      next: (response: any) => {
        console.log(response);
        this.total = response.total;
        this.totalEnabled = response.totalEnabled;
        this.totalDisabled = response.totalDisabled;
      },
      error: (error: any) => {
        console.log(error);
      }
    });
  }

  findAll(page: number = 1, pageSize: number = this.paginationModel.pageSize, sortDir: string = 'ASC', sortBy: string = 'id', search: string = this.search): void {
    this.findAllSubscription = this.employeeService.findAll(page, pageSize, sortDir, sortBy, search).subscribe({
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
        console.log(this.paginationModel);  
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
    this.employees = [];
  }
  changePageNumber(pageNumber: number): void {
    if (pageNumber === this.paginationModel.pageNumber) return;
    this.employees = [];
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

  findByEnabled(status: string): void {
    this.status = status;
    this.handleSuccess();
  }

  handleSuccess(): void {
    this.employees = [];
    const sortDir = this.activatedRoute.snapshot.queryParams['sort-direction'];
    const sortBy = this.activatedRoute.snapshot.queryParams['sort-by'];
    this.findAll(this.paginationModel.pageNumber, this.paginationModel.pageSize, sortDir, sortBy, this.search);
  }
  clearAllParams(): void {
    const navigationExtras: NavigationExtras = {
      queryParams: {},
    };
    this.router.navigate([], navigationExtras);
    this, this.handleSuccess();
  }

  delete(id: number) {
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
        this.employeeService.delete(id).subscribe({
          next: () => {
            this.handleSuccess();
            this.toastr.success('Xóa sản phẩm thành công', 'Thông báo');
            if (this.paginationModel.pageLast && this.paginationModel.content.length === 1 && this.paginationModel.pageNumber > 1)
                    this.router.navigate([], { queryParams: { page: this.paginationModel.pageNumber - 1 }, queryParamsHandling: 'merge' }).then(() => { });
          },
          error: (error: any) => {
            console.log(error);
            if (error.status === 400 && error.error === 'PRODUCT_NOT_FOUND')
              this.toastr.error(`Không tìm thấy sản phẩm để xóa!`, 'Thông báo');
            else if (error.status === 400 && error.error === 'CANNOT_DELETE_PRODUCT_DETAILS')
              this.toastr.info(`Sản phẩm đã có hóa đơn không thể xóa!`, 'Thông báo');
            else
              this.toastr.error(`Xóa thất bại, Lỗi không xác định!`, 'Thông báo');
          }
        });
      }
    })
  }

  deletelist(): void {
    if (this.employees.length == 0)
      this.toastr.info('Bạn chưa chọn sản phẩm để xóa!', 'Thông báo');
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
          for (let i = 0; i < this.employees.length; i++) {
            const item = this.employees[i];
            let listLength = this.employees.length;
            this.employeeService.delete(item.id).subscribe({
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
                if (error.status === 400 && error.error === 'PRODUCT_NOT_FOUND')
                  this.toastr.error(`Không tìm thấy sản phẩm "${item.name}" để xóa!`, 'Thông báo');
                else if (error.status === 400 && error.error === 'CANNOT_DELETE_PRODUCT_DETAILS')
                  this.toastr.info(`Sản phẩm "${item.name}" đã có hóa đơn không thể xóa!`, 'Thông báo');
                else
                  this.toastr.error(`Xóa "${item.name}" thất bại, Lỗi không xác định!`, 'Thông báo');
              }
            });
          }
          this.employees = [];
        }
      });
    }
  }
}



import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import { SupplierModel } from 'src/app/model/supplier.model';
import { SupplierService } from 'src/app/service/supplier.service';
import { PaginationModel } from 'src/app/model/pagination.model';
import { Subscription } from 'rxjs';
import { TokenService } from 'src/app/service/token.service';
import { ToastrService } from 'ngx-toastr';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-supplier',
  templateUrl: './supplier.component.html',
  styleUrls: ['./supplier.component.css']
})
export class SupplierComponent implements OnInit {
  @ViewChild('headerModal') headerModal!: ElementRef;
  @ViewChild('btnSave') btnSave!: ElementRef;
  @ViewChild('btnCloseModal') btnCloseModal!: ElementRef;

  paginationModel: PaginationModel;
  supplier: any;
  search: string = '';
  duplicateName: string = '';
  duplicatePhone: string = '';
  duplicateTaxCode: string = '';
  suppliers: SupplierModel[] = [];

  isEmployee: boolean = false;

  count: number = 0;
  total: number = 0;

  supplierForm: FormGroup = new FormGroup({
    id: new FormControl(null),
    name: new FormControl('', [Validators.required, Validators.maxLength(50)]),
    phone: new FormControl('', [Validators.required, Validators.minLength(10), Validators.maxLength(10)]),
    address: new FormControl('', [Validators.required, Validators.maxLength(100)]),
  });

  private findAllSubscription: Subscription | undefined;

  constructor(
    private supplierService: SupplierService,
    private toastr: ToastrService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private tokenService: TokenService,
    private title: Title
  ) {
    this.title.setTitle('Quản lý nhà cung cấp');
    this.paginationModel = new PaginationModel({});
  }

  ngOnInit(): void {
    this.isEmployee = this.tokenService.isEmployeeLogin();
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

  onSubmit(): void {
    if (this.supplierForm.invalid) {
      console.log('supplierForm Invalid');
      return;
    }
    this.suppliers = [];
    this.supplierForm.value.id ? this.update() : this.create();
  }

  toggleSelectAll() {
    const areAllSelected = this.suppliers.length === this.paginationModel.content.length;

    if (areAllSelected) {
      this.suppliers = [];
    } else {
      this.suppliers = [...this.paginationModel.content];
    }
  }

  isSelected(supplier: SupplierModel): boolean {
    return this.suppliers.findIndex(c => c.id === supplier.id) !== -1;
  }


  onCheckboxChange(supplier: SupplierModel) {
    const index = this.suppliers.findIndex(c => c.id === supplier.id);

    if (index === -1) {
      this.suppliers.push(supplier);
    } else {
      this.suppliers.splice(index, 1);
    }
  }

  resetText(): void {
    this.duplicateName = '';
    this.duplicatePhone = '';
    this.duplicateTaxCode = '';
  }

  getTotals() {
    this.supplierService.getTotals().subscribe({
      next: (response: any) => {
        this.total = response.total;
      },
      error: (error: any) => {
        console.log(error);
      }
    });
  }

  findAll(page: number = 1, pageSize: number = this.paginationModel.pageSize, sortDir: string = 'ASC', sortBy: string = 'id', search: string = this.search): void {
    this.findAllSubscription = this.supplierService.findAll(page, pageSize, sortDir, sortBy, search).subscribe({
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

  findById(id: number): void {
    this.supplierService.findById(id).subscribe({
      next: (response: any) => {
        this.supplier = response;
      },
      error: (error: any) => {
        if (error.status === 400 && error.error === 'SUPPLIER_NOT_FOUND')
          this.toastr.error('Không tìm thấy nhà cung cấp này!', 'Thông báo');
        else
        console.log(error);
      }
    });
  }

  changePageSize(pageSize: number): void {
    this.router.navigate([], { queryParams: { size: pageSize, page: 1 }, queryParamsHandling: 'merge' }).then(() => { });
    this.suppliers = [];
  }
  changePageNumber(pageNumber: number): void {
    if (pageNumber === this.paginationModel.pageNumber) return;
    this.suppliers = [];
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

  clearAllParams(): void {
    const navigationExtras: NavigationExtras = {
      queryParams: {},
    };
    this.router.navigate([], navigationExtras);
    this.handleSuccess();
    this.search = '';
  }

  create(): void {
    this.supplierService.save(this.supplierForm.value).subscribe({
      next: (response: any) => {
        this.findAll();
        this.router.navigate([], { queryParams: { page: 1 }, queryParamsHandling: 'merge' }).then(() => { });
        this.toastr.success('Thêm thành công!', 'Thông báo');
        this.btnCloseModal.nativeElement.click();
      },
      error: (error: any) => this.handleError(error)
    });
  }

  update(): void {
    this.supplierService.save(this.supplierForm.value).subscribe({
      next: (response: any) => {
        this.handleSuccess();
        this.toastr.success('Cập nhật thành công!', 'Thông báo');
        this.btnCloseModal.nativeElement.click();
      },
      error: (error: any) => this.handleError(error)
    });
  }

  createSupplier(): void {
    this.supplierForm.reset();
    this.supplierForm.patchValue({ enabled: true });
    this.btnSave.nativeElement.innerHTML = 'Thêm mới';
    this.headerModal.nativeElement.innerHTML = 'Thêm nhà cung cấp mới';
  }

  editSupplier(supplier: SupplierModel): void {
    this.supplierForm.patchValue(supplier);
    this.btnSave.nativeElement.innerHTML = 'Cập nhật';
    this.headerModal.nativeElement.innerHTML = 'Cập nhật nhà cung cấp';
  }

  deleteSupplier(id: number): void {
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
        this.supplierService.deleteOne(id).subscribe({
          next: (response: any) => {
            this.handleSuccess();
            this.toastr.success('Xóa nhà cung cấp thành công!', 'Thông báo');
            if (this.paginationModel.pageLast && this.paginationModel.content.length === 1 && this.paginationModel.pageNumber > 1)
              this.router.navigate([], { queryParams: { page: this.paginationModel.pageNumber - 1 }, queryParamsHandling: 'merge' }).then(() => { });
          },
          error: (error: any) => {
            console.log(error);
            if (error.status === 400 && error.error === 'SUPPLIER_NOT_FOUND')
              this.toastr.error(`Không tìm thấy nhà cung cấp này để xóa!`, 'Thông báo');
            else if (error.status === 400 && error.error === 'CANNOT_DELETE_SUPPLIER')
              this.toastr.info(`Nhà cung cấp này đã có hóa đơn không thể xóa!`, 'Thông báo');
            else
              this.toastr.error(`Xóa thất bại, Lỗi không xác định!`, 'Thông báo');
          }
        });
      }
    });
  }

  handleSuccess(): void {
    this.suppliers = [];
    const sortDir = this.activatedRoute.snapshot.queryParams['sort-direction'];
    const sortBy = this.activatedRoute.snapshot.queryParams['sort-by'];
    this.findAll(this.paginationModel.pageNumber, this.paginationModel.pageSize, sortDir, sortBy, this.search);
  }

  private handleError(error: any): void {
    console.log(error);
    if (error.status === 400 && error.error === 'DUPLICATE_NAME') {
      this.duplicateName = 'Tên nhà cung cấp đã tồn tại!';
    }
    if (error.status === 400 && error.error === 'DUPLICATE_PHONE') {
      this.duplicatePhone = 'Số điện thoại đã tồn tại!';
    }
  }

  deletes(): void {
    if (this.suppliers.length == 0)
      this.toastr.info('Bạn chưa chọn nhà cung cấp để xóa!', 'Thông báo');
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
          for (let i = 0; i < this.suppliers.length; i++) {
            const supplier = this.suppliers[i];
            let listLength = this.suppliers.length;
            this.supplierService.deleteOne(supplier.id).subscribe({
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
                if (error.status === 400 && error.error === 'SUPPLIER_NOT_FOUND')
                  this.toastr.error(`Không tìm thấy "${supplier.name}" để xóa!`, 'Thông báo');
                else if (error.status === 400 && error.error === 'CANNOT_DELETE_SUPPLIER')
                  this.toastr.info(`Nhà cung cấp "${supplier.name}" đã có hóa đơn không thể xóa!`, 'Thông báo');
                else
                  this.toastr.error(`Xóa "${supplier.name}" thất bại, Lỗi không xác định!`, 'Thông báo');
              }
            });
          }
          this.suppliers = [];
        }
      });
    }
  }
}

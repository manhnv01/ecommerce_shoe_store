import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import slugify from 'slugify';
import { BrandModel } from 'src/app/model/brand.model';
import { PaginationModel } from 'src/app/model/pagination.model';
import { BrandService } from 'src/app/service/brand.service';
import Swal from 'sweetalert2';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { Environment } from 'src/app/environment/environment';
import { TokenService } from 'src/app/service/token.service';

@Component({
  selector: 'app-brand',
  templateUrl: './brand.component.html',
  styleUrls: ['./brand.component.css']
})
export class BrandComponent implements OnInit {
  @ViewChild('headerModal') headerModal!: ElementRef;
  @ViewChild('btnSave') btnSave!: ElementRef;
  @ViewChild('btnCloseModal') btnCloseModal!: ElementRef;

  baseUrl: string = `${Environment.apiBaseUrl}`;

  maxFileSize = 10 * 1024 * 1024; // 10MB

  selectedImageUrl: string = "";
  selectedImageFile: File = new File([""], "filename");
  paginationModel: PaginationModel;
  brand: any;
  search: string = '';
  enabled: string = '';
  errorName: string = '';
  errorSlug: string = '';
  brands: BrandModel[] = [];

  total: number = 0;
  totalEnabled: number = 0;
  totalDisabled: number = 0;

  isEmployee: boolean = false;

  count: number = 0;

  brandForm: FormGroup = new FormGroup({
    id: new FormControl(null),
    thumbnailFile: new FormControl(null, [Validators.required]),
    name: new FormControl('', [Validators.required, Validators.maxLength(30)]),
    slug: new FormControl('', [Validators.required, Validators.maxLength(50), Validators.pattern('^[a-z0-9]+(?:-[a-z0-9]+)*$')]),
    enabled: new FormControl(true),
  });

  private findAllSubscription: Subscription | undefined;

  constructor(
    private brandService: BrandService,
    private toastr: ToastrService,
    private activatedRoute: ActivatedRoute,
    private tokenService: TokenService,
    private router: Router,
    private title: Title
  ) {
    this.title.setTitle('Quản lý thương hiệu');
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
    this.brandForm.patchValue({ name: this.brandForm.value.name.trim() });
    if (this.brandForm.invalid) {
      console.log('brandForm Invalid');
      return;
    }
    this.brands = [];
    this.brandForm.value.id ? this.update() : this.create();
  }

  isSelected(brand: BrandModel): boolean {
    return this.brands.findIndex(c => c.id === brand.id) !== -1;
  }


  onCheckboxChange(brand: BrandModel) {
    const index = this.brands.findIndex(c => c.id === brand.id);

    if (index === -1) {
      this.brands.push(brand);
    } else {
      this.brands.splice(index, 1);
    }
  }


  slugify() {
    this.brandForm.patchValue({
      slug: slugify(this.brandForm.value.name, { lower: true, remove: /[\*+~.,()'"!:@]/g })
    });
  }

  resetText(): void {
    this.errorName = '';
    this.errorSlug = '';
  }

  onFileChange(event: any) {
    const file = event.target.files[0];

    if (file) {
      if (!file.type.includes('image')) {
        this.toastr.error('Không phải file hình ảnh!', 'Thông báo');
        return;
      }
      else if (file.size > this.maxFileSize) {
        this.toastr.error('Dung lượng hình ảnh không được quá 10MB!', 'Thông báo');
        return;
      }
      else {
        this.selectedImageFile = file;
        const reader = new FileReader();
        reader.onload = (e: any) => {
          this.selectedImageUrl = e.target.result;
        };
        reader.readAsDataURL(file);
      }
    }
  }

  getTotals() {
    this.brandService.getTotals().subscribe({
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

  findAll(page: number = 1, pageSize: number = this.paginationModel.pageSize, sortDir: string = 'ASC', sortBy: string = 'id', search: string = this.search, enabled: string = this.enabled): void {
    this.findAllSubscription = this.brandService.findAll(page, pageSize, sortDir, sortBy, search, enabled).subscribe({
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
    this.brandService.findById(id).subscribe({
      next: (response: any) => {
        this.brand = response;
      },
      error: (error: any) => {
        if (error.status === 400 && error.error === 'BRAND_NOT_FOUND') {
          this.toastr.error('Không tìm thấy thương hiệu này!', 'Thông báo');
        }
        console.log(error);
      }
    });
  }

  findByEnabled(enabled: string): void {
    this.enabled = enabled;
    this.handleSuccess();
  }

  changePageSize(pageSize: number): void {
    this.router.navigate([], { queryParams: { size: pageSize, page: 1 }, queryParamsHandling: 'merge' }).then(() => { });
    this.brands = [];
  }
  changePageNumber(pageNumber: number): void {
    if (pageNumber === this.paginationModel.pageNumber) return;
    this.brands = [];
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
    this.enabled = '';
    this.handleSuccess();
    this.search = '';
  }

  create(): void {
    this.brandService.create(this.brandForm.value, this.selectedImageFile).subscribe({
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
    this.brandService.update(this.brandForm.value, this.selectedImageFile).subscribe({
      next: (response: any) => {
        this.handleSuccess();
        this.toastr.success('Cập nhật thành công!', 'Thông báo');
        this.btnCloseModal.nativeElement.click();
      },
      error: (error: any) => this.handleError(error)
    });
  }

  changeSwitch(brand: any): void {
    brand.enabled = !brand.enabled;
    this.brandService.changeSwitch(brand.id).subscribe({
      next: () => this.handleSuccess(),
      error: (error: any) => {
        console.log(error);
      }
    });
  }

  createBrand(): void {
    this.brandForm.reset();
    this.brandForm.patchValue({ enabled: true });
    const thumbnailFileControl = this.brandForm.get('thumbnailFile');
    this.selectedImageUrl = '';
    thumbnailFileControl?.setValidators([Validators.required]);
    this.btnSave.nativeElement.innerHTML = 'Thêm mới';
    this.headerModal.nativeElement.innerHTML = 'Thêm thương hiệu mới';
  }

  editBrand(brand: BrandModel): void {
    this.brandForm.patchValue(brand);
    this.selectedImageUrl = Environment.apiBaseUrl + '/images/' + brand.thumbnail;
    const thumbnailFileControl = this.brandForm.get('thumbnailFile');
    thumbnailFileControl?.setValidators([Validators.nullValidator]);
    this.btnSave.nativeElement.innerHTML = 'Cập nhật';
    this.headerModal.nativeElement.innerHTML = 'Cập nhật thương hiệu';
  }

  deleteBrand(id: number): void {
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
        this.brandService.deleteOne(id).subscribe({
          next: (response: any) => {
            this.handleSuccess();
            this.toastr.success('Xóa thương hiệu thành công!', 'Thông báo');
            if (this.paginationModel.pageLast && this.paginationModel.content.length === 1 && this.paginationModel.pageNumber > 1)
              this.router.navigate([], { queryParams: { page: this.paginationModel.pageNumber - 1 }, queryParamsHandling: 'merge' }).then(() => { });
          },
          error: (error: any) => {
            console.log(error);
            if (error.status === 400 && error.error === 'BRAND_NOT_FOUND')
              this.toastr.error(`Không tìm thấy thương hiệu này để xóa!`, 'Thông báo');
            else if (error.status === 400 && error.error === 'CANNOT_DELETE_BRAND')
              this.toastr.info(`Thương hiệu này đã có sản phẩm không thể xóa!`, 'Thông báo');
            else
              this.toastr.error(`Xóa thất bại, Lỗi không xác định!`, 'Thông báo');
          }
        });
      }
    });
  }

  updateListStatus(enabled: boolean) {
    if (this.brands.length === 0) {
      this.toastr.info('Bạn chưa chọn thương hiệu cập nhật!', 'Thông báo');
      return;
    }
    const brandIds: number[] = this.brands.map(brand => brand.id);
    this.brandService.updatesStatus(brandIds, enabled).subscribe({
      next: (response: any) => {
        this.handleSuccess();
        this.toastr.success('Cập nhật thành công!', 'Thông báo');
      },
      error: (error: any) => console.log(error),
    });
  }

  handleSuccess(): void {
    this.brands = [];
    const sortDir = this.activatedRoute.snapshot.queryParams['sort-direction'];
    const sortBy = this.activatedRoute.snapshot.queryParams['sort-by'];
    this.findAll(this.paginationModel.pageNumber, this.paginationModel.pageSize, sortDir, sortBy, this.search);
  }

  private handleError(error: any): void {
    console.log(error);
    if (error.status === 400 && error.error === 'DUPLICATE_NAME') {
      this.errorName = 'Tên thương hiệu đã tồn tại!';
      this.errorSlug = '';
    } else if (error.status === 400 && error.error === 'DUPLICATE_SLUG') {
      this.errorSlug = 'Slug đã tồn tại!';
      this.errorName = '';
    } else if (error.status === 400 && error.error === 'IMAGE_NOT_FOUND') {
      this.toastr.error('Hình ảnh không tồn tại!', 'Thông báo');
    } else if (error.status === 400 && error.error === 'IMAGE_NOT_VALID') {
      this.toastr.error('Không phải file hình ảnh!', 'Thông báo');
    } else if (error.status === 400 && error.error === 'IMAGE_SIZE_TOO_LARGE_10MB') {
      this.toastr.error('Dung lượng hình ảnh không được quá 10MB!', 'Thông báo');
    } else {
      this.toastr.error('Lỗi không xác định.', 'Thông báo');
    }
  }

  deletes(): void {
    if (this.brands.length == 0)
      this.toastr.info('Bạn chưa chọn thương hiệu để xóa!', 'Thông báo');
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
          for (let i = 0; i < this.brands.length; i++) {
            const brand = this.brands[i];
            let listLength = this.brands.length;
            this.brandService.deleteOne(brand.id).subscribe({
              next: (response: any) => {
                this.handleSuccess();
                this.count++;
                if (listLength === this.count) {
                  this.toastr.success(`Xóa ${this.count} mục thành công!`, 'Thông báo');
                  if (this.paginationModel.pageLast && this.paginationModel.content.length <= listLength && this.paginationModel.pageNumber > 1)
                    this.router.navigate([], { queryParams: { page: this.paginationModel.pageNumber - 1 }, queryParamsHandling: 'merge' }).then(() => { });
                }
              },
              error: (error: any) => {
                console.log(error);
                if (error.status === 400 && error.error === 'BRAND_NOT_FOUND')
                  this.toastr.error(`Không tìm thấy "${brand.name}" để xóa!`, 'Thông báo');
                else if (error.status === 400 && error.error === 'CANNOT_DELETE_BRAND')
                  this.toastr.info(`Thương hiệu "${brand.name}" đã có sản phẩm không thể xóa!`, 'Thông báo');
                else
                  this.toastr.error(`Xóa "${brand.name}" thất bại, Lỗi không xác định!`, 'Thông báo');
              }
            });
          }
          this.brands = [];
        }
      });
    }
  }
}
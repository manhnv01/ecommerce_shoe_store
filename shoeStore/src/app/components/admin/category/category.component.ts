import { Component, ElementRef, OnInit, ViewChild, OnDestroy } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, NavigationEnd, NavigationExtras, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { CategoryModel } from 'src/app/model/category.model';
import { CategoryService } from 'src/app/service/category.service';
import Swal from 'sweetalert2';
import { Subscription, filter } from 'rxjs';
import slugify from 'slugify';
import { PaginationModel } from 'src/app/model/pagination.model';

@Component({
  selector: 'app-category',
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.css']
})
export class CategoryComponent implements OnInit, OnDestroy {
  @ViewChild('headerModal') headerModal!: ElementRef;
  @ViewChild('btnSave') btnSave!: ElementRef;
  @ViewChild('btnCloseModal') btnCloseModal!: ElementRef;

  paginationModel: PaginationModel;
  category: any;
  search: string = '';
  enabled: string = '';
  errorName: string = '';
  errorSlug: string = '';
  categories: CategoryModel[] = [];
  count: number = 0;

  totalCategories: number = 0;
  totalEnabledCategories: number = 0;
  totalDisabledCategories: number = 0;

  categoryForm: FormGroup = new FormGroup({
    id: new FormControl(null),
    name: new FormControl('', [Validators.required, Validators.maxLength(30)]),
    slug: new FormControl('', [Validators.required, Validators.maxLength(50), Validators.pattern('^[a-z0-9]+(?:-[a-z0-9]+)*$')]),
    enabled: new FormControl(true),
  });

  private findAllSubscription: Subscription | undefined;

  constructor(
    private categoryService: CategoryService,
    private toastr: ToastrService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private title: Title
  ) {
    this.title.setTitle('Quản lý danh mục');
    this.paginationModel = new PaginationModel({});
  }

  ngOnInit(): void {
    this.getCategoryTotals();
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
    if (this.categoryForm.invalid) {
      console.log('categoryForm Invalid');
      return;
    }
    this.categories = [];
    this.categoryForm.value.id ? this.update() : this.create();
  }

  toggleSelectAll() {
    const areAllSelected = this.categories.length === this.paginationModel.content.length;

    if (areAllSelected) {
      this.categories = [];
    } else {
      this.categories = [...this.paginationModel.content];
    }
  }

  isSelected(category: CategoryModel): boolean {
    return this.categories.findIndex(c => c.id === category.id) !== -1;
  }


  onCheckboxChange(category: CategoryModel) {
    const index = this.categories.findIndex(c => c.id === category.id);

    if (index === -1) {
      this.categories.push(category);
    } else {
      this.categories.splice(index, 1);
    }
  }


  slugify() {
    this.categoryForm.patchValue({
      slug: slugify(this.categoryForm.value.name, { lower: true, remove: /[\*+~.,()'"!:@]/g })
    });
  }

  resetText(): void {
    this.errorName = '';
    this.errorSlug = '';
  }

  getCategoryTotals() {
    this.categoryService.getCategoryTotals().subscribe({
      next: (response: any) => {
        this.totalCategories = response.totalCategories;
        this.totalEnabledCategories = response.totalEnabledCategories;
        this.totalDisabledCategories = response.totalDisabledCategories;
      },
      error: (error: any) => {
        console.log(error);
      }
    });
  }

  findAll(page: number = 1, pageSize: number = this.paginationModel.pageSize, sortDir: string = 'ASC', sortBy: string = 'id', search: string = this.search, enabled: string = this.enabled): void {
    this.findAllSubscription = this.categoryService.findAll(page, pageSize, sortDir, sortBy, search, enabled).subscribe({
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
        this.getCategoryTotals()
      },
      error: (error: any) => {
        console.log(error);
      }
    });
  }

  findById(id: number): void {
    this.categoryService.findById(id).subscribe({
      next: (response: any) => {
        this.category = response;
      },
      error: (error: any) => {
        if (error.status === 400 && error.error === 'CATEGORY_NOT_FOUND')
          this.toastr.error(`Không tìm thấy danh mục này!`, 'Thông báo');
        else
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
    this.categories = [];
  }
  changePageNumber(pageNumber: number): void {
    if (pageNumber === this.paginationModel.pageNumber) return;
    this.categories = [];
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
    //const queryParams = { ...this.router.routerState.snapshot.root.queryParams };
    // delete queryParams['yourParamName'];

    const navigationExtras: NavigationExtras = {
      queryParams: {},
      //queryParamsHandling: 'merge',
    };
    this.router.navigate([], navigationExtras);
    this.enabled = '';
    this.handleSuccess();
    this.search = '';
  }

  create(): void {

    this.categoryService.save(this.categoryForm.value).subscribe({
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
    this.categoryService.save(this.categoryForm.value).subscribe({
      next: (response: any) => {
        this.handleSuccess();
        this.toastr.success('Cập nhật thành công!', 'Thông báo');
        this.btnCloseModal.nativeElement.click();
      },
      error: (error: any) => this.handleError(error)
    });
  }

  changeSwitch(category: any): void {
    category.enabled = !category.enabled;
    this.categoryService.save(category).subscribe({
      next: () => this.handleSuccess(),
      error: (error: any) => {
        console.log(error);
      }
    });
  }
  

  createCategory(): void {
    this.categoryForm.reset();
    this.categoryForm.patchValue({ enabled: true });
    this.btnSave.nativeElement.innerHTML = 'Thêm mới';
    this.headerModal.nativeElement.innerHTML = 'Thêm danh mục mới';
  }

  editCategory(category: CategoryModel): void {
    this.categoryForm.patchValue(category);
    this.btnSave.nativeElement.innerHTML = 'Cập nhật';
    this.headerModal.nativeElement.innerHTML = 'Cập nhật danh mục';
  }

  deleteCategory(id: number): void {
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
        this.categoryService.deleteOne(id).subscribe({
          next: (response: any) => {
            console.log(response);
            this.handleSuccess();
            if (response.message === 'DELETE_CATEGORY_SUCCESS') {
              this.toastr.success('Xóa danh mục thành công!', 'Thông báo');
              if (this.paginationModel.pageLast && this.paginationModel.content.length === 1 && this.paginationModel.pageNumber > 1)
                this.router.navigate([], { queryParams: { page: this.paginationModel.pageNumber - 1 }, queryParamsHandling: 'merge' }).then(() => { });
            }
          },
          error: (error: any) => {
            console.log(error);
            if (error.status === 400 && error.error === 'CATEGORY_NOT_FOUND')
              this.toastr.error(`Không tìm thấy danh mục này để xóa!`, 'Thông báo');
            else if (error.status === 400 && error.error === 'CANNOT_DELETE_CATEGORY')
              this.toastr.info(`Danh mục này đã có sản phẩm không thể xóa!`, 'Thông báo');
            else
              this.toastr.error(`Xóa thất bại, Lỗi không xác định!`, 'Thông báo');
          }
        });
      }
    });
  }

  updateListStatus(enabled: boolean) {
    if (this.categories.length === 0) {
      this.toastr.info('Bạn chưa chọn danh mục cập nhật!', 'Thông báo');
      return;
    }
    const categoryIds: number[] = this.categories.map(category => category.id);
    this.categoryService.updatesStatus(categoryIds, enabled).subscribe({
      next: (response: any) => {
        this.handleSuccess();
        this.toastr.success('Cập nhật thành công!', 'Thông báo');
      },
      error: (error: any) => console.log(error),
    });
  }

  handleSuccess(): void {
    this.categories = [];
    const sortDir = this.activatedRoute.snapshot.queryParams['sort-direction'];
    const sortBy = this.activatedRoute.snapshot.queryParams['sort-by'];
    this.findAll(this.paginationModel.pageNumber, this.paginationModel.pageSize, sortDir, sortBy, this.search);
  }

  private handleError(error: any): void {
    console.log(error);
    if (error.status === 400 && error.error === 'DUPLICATE_NAME') {
      this.errorName = 'Tên danh mục đã tồn tại!';
      this.errorSlug = '';
    } else if (error.status === 400 && error.error === 'DUPLICATE_SLUG') {
      this.errorSlug = 'Slug đã tồn tại!';
      this.errorName = '';
    } else {
      this.toastr.error('Lỗi không xác định.', 'Thông báo');
    }
  }

  deletes(): void {
    if (this.categories.length == 0)
      this.toastr.info('Bạn chưa chọn danh mục để xóa!', 'Thông báo');
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
          for (let i = 0; i < this.categories.length; i++) {
            const category = this.categories[i];
            let listLength = this.categories.length;
            this.categoryService.deleteOne(category.id).subscribe({
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
                if (error.status === 400 && error.error === 'CATEGORY_NOT_FOUND')
                  this.toastr.error(`Không tìm thấy "${category.name}" để xóa!`, 'Thông báo');
                else if (error.status === 400 && error.error === 'CANNOT_DELETE_CATEGORY')
                  this.toastr.info(`Danh mục "${category.name}" đã có sản phẩm không thể xóa!`, 'Thông báo');
                else
                  this.toastr.error(`Xóa "${category.name}" thất bại, Lỗi không xác định!`, 'Thông báo');
              }
            });
          }
          this.categories = [];
        }
      });
    }
  }
}

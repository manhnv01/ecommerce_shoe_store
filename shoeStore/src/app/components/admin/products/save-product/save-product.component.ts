import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { BrandService } from 'src/app/service/brand.service';
import { CategoryService } from 'src/app/service/category.service';
import { ProductService } from 'src/app/service/product.service';
import { ToastrService } from 'ngx-toastr';
import Swal from 'sweetalert2';
import { Environment } from 'src/app/environment/environment';
import { CategoryModel } from 'src/app/model/category.model';
import { BrandModel } from 'src/app/model/brand.model';
import slugify from 'slugify';

@Component({
  selector: 'app-save-product',
  templateUrl: './save-product.component.html',
  styleUrls: ['./save-product.component.css']
})
export class SaveProductComponent implements OnInit {
  protected readonly Environment = Environment;
  duplicateName: string = '';
  duplicateSlug: string = '';
  titleString: string = "";

  maxFileSize: number = 10 * 1024 * 1024;

  selectedImageUrl: string = "";
  selectedImageFile: File = new File([""], "filename");
  selectedImageProductFiles: File[] = [];
  selectedImageProductUrl: string[] = [];
  categories: CategoryModel[] = [];
  brands: BrandModel[] = [];
  editorConfig = {
    height: 314,
    selector: 'textarea',
    base_url: '/tinymce',
    suffix: '.min',
    plugins: 'link image table fullscreen',
    menubar: '',
    toolbar: 'undo redo | image link | cut copy paste | bold italic underline | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | fullscreen',
  };

  isDisplayNone: boolean = false;
  btnSave: string = "";

  productForm: FormGroup = new FormGroup({
    id: new FormControl(null),
    name: new FormControl('', [Validators.required, Validators.maxLength(50)]),
    price: new FormControl('', [
      Validators.required,
      Validators.min(0),
      Validators.max(1000000000),
      Validators.pattern(/^-?\d+\.?\d*$/)
    ]),
    thumbnailFile: new FormControl(null),
    slug: new FormControl('', [
      Validators.required,
      Validators.maxLength(100),
      Validators.pattern(/^[a-zA-Z0-9-]*$/)
    ]),
    description: new FormControl(''),
    enabled: new FormControl(true),
    images: new FormControl(null, [Validators.required]),
    categoryId: new FormControl(null, [Validators.required]),
    brandId: new FormControl(null, [Validators.required]),
    productColors: new FormArray([
      new FormGroup({
        id: new FormControl(null),
        color: new FormControl('', [Validators.required, Validators.maxLength(20)]),
      })
    ])
  }
  );

  constructor(
    private title: Title,
    private categoryService: CategoryService,
    private brandService: BrandService,
    private productService: ProductService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef,
    private toastr: ToastrService) {
  }

  ngOnInit() {
    const thumbnailFileControl = this.productForm.get('thumbnailFile');
    const imagesControl = this.productForm.get('images');
    if (this.activatedRoute.snapshot.params["id"] === undefined) {
      this.btnSave = "Thêm mới";
      this.titleString = "Thêm sản phẩm";
      thumbnailFileControl?.setValidators([Validators.required]);
      imagesControl?.setValidators([Validators.required]);
    } else {
      this.titleString = "Cập nhật sản phẩm";
      this.btnSave = "Cập nhật";
      thumbnailFileControl?.setValidators([Validators.nullValidator]);
      imagesControl?.setValidators([Validators.nullValidator]);
      this.findProductById(this.activatedRoute.snapshot.params["id"]);
    }
    thumbnailFileControl?.updateValueAndValidity();
    imagesControl?.updateValueAndValidity();
    this.title.setTitle(this.titleString);

    this.findAllCategory();
    this.findAllBrand();
  }

  slugify() {
    this.productForm.patchValue({
      slug: slugify(this.productForm.value.name, { lower: true, remove: /[\*+~.,()'"!:@]/g })
    });
  }

  onSubmit() {
    if (this.productForm.invalid) {
      console.log("Form invalid");
      return;
    }
    if (this.activatedRoute.snapshot.params["id"] === undefined) {
      this.createProduct();
    } else {
      this.updateProduct();
    }
  }

  createProduct() {
    console.log(this.productForm.value);
    this.productService.create(this.productForm.value, this.selectedImageFile, this.selectedImageProductFiles).subscribe({
      next: () => {
        this.toastr.success("Thêm sản phẩm thành công");
        this.router.navigateByUrl("/admin/product");
      },
      error: (err: any) => {
        this.handleError(err);
      }
    });
  }

  updateProduct() {
    this.productService.update(this.productForm.value, this.selectedImageFile, this.selectedImageProductFiles).subscribe({
      next: () => {
        this.toastr.success("Cập nhật sản phẩm thành công");
        this.router.navigateByUrl("/admin/product");
      },
      error: (err: any) => {
        this.handleError(err);
      }
    });
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

  onSelect(event: any) {
    for (let i = 0; i < event.addedFiles.length; i++) {
      const file = event.addedFiles[i];
      if (file) {
        if (!file.type.includes('image')) {
          this.toastr.error('Không phải file hình ảnh!', 'Thông báo');
          return;
        } else if (file.size > this.maxFileSize) {
          this.toastr.error('Dung lượng hình ảnh không được quá 10MB!', 'Thông báo');
          return;
        }
      }
    }
    this.selectedImageProductFiles.push(...event.addedFiles);
    this.productForm.get('images')?.setValue(this.selectedImageProductFiles);
  }

  onRemove(event: any) {
    this.selectedImageProductFiles.splice(this.selectedImageProductFiles.indexOf(event), 1);
    this.productForm.get('productColors')?.get('images')?.setValue(this.selectedImageProductFiles);
  }

  get productColors() {
    return this.productForm.get('productColors') as FormArray;
  }

  addProductDetails() {
    const productColors = this.productForm.get('productColors') as FormArray;
    productColors.push(
      new FormGroup({
        id: new FormControl(null),
        color: new FormControl('', [Validators.required]),
      })
    );

    this.cdr.detectChanges();
  }

  removeProductDetails(index: number) {
    const productDetails = this.productForm.get('productColors') as FormArray;

    if (productDetails.at(index).get('id')?.value !== null) {
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
          this.productService.deleteProductColor(productDetails.at(index).get('id')?.value).subscribe({
            next: () => {
              this.toastr.success("Xóa màu sắc thành công", "Thông báo");
              productDetails.removeAt(index);
            },
            error: (err: any) => {
              console.log(err);
              this.toastr.error("Đã có hóa đơn không thể xóa", "Thông báo");
            }
          });
        }
      });
    } else {
      productDetails.removeAt(index);
    }
  }

  deleteImageProduct(imageName: string) {
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
        this.productService.deleteImage(this.activatedRoute.snapshot.params["id"], imageName).subscribe({
          next: () => {
            this.toastr.success("Xóa hình ảnh thành công", "Thông báo");
            if (this.selectedImageProductUrl.length === 0) {
              const imageProductFilesControl = this.productForm.get('images');
              imageProductFilesControl?.setValidators([Validators.required]);
              imageProductFilesControl?.updateValueAndValidity();
            }

            this.selectedImageProductUrl = this.selectedImageProductUrl.filter((image: string) => {
              return image !== imageName;
            });
          },
          error: (err: any) => {
            console.log(err);
            this.toastr.error("Xóa hình ảnh thất bại", "Thông báo");
          }
        });
      }
    });
  }

  findAllCategory() {
    this.categoryService.findAllOption(1, 100, "ASC", "name").subscribe(
      (data: any) => {
        this.categories = data.content;
      }
    );
  }

  findAllBrand() {
    this.brandService.findAllOption(1, 100, "ASC", "name").subscribe(
      (data: any) => {
        console.log(data);
        this.brands = data.content;
      }
    );
  }

  private handleError(error: any): void {
    console.log(error);
    if (error.status === 400 && error.error === 'DUPLICATE_NAME') {
      this.duplicateName = 'Tên sản phẩm đã tồn tại!';
      this.duplicateSlug = '';
    } else if (error.status === 400 && error.error === 'DUPLICATE_SLUG') {
      this.duplicateSlug = 'Slug đã tồn tại!';
      this.duplicateName = '';
    } else if (error.status === 400 && error.error === 'DUPLICATE_PRODUCT_COLOR') {
      this.toastr.error('Màu sắc sản phẩm trùng tên!', 'Thông báo');
    } else if (error.status === 400 && error.error === 'IMAGE_NOT_FOUND') {
      this.toastr.error('Hình ảnh không tồn tại!', 'Thông báo');
    } else if (error.status === 400 && error.error === 'IMAGE_NOT_VALID') {
      this.toastr.error('Không phải file hình ảnh!', 'Thông báo');
    } else if (error.status === 400 && error.error === 'IMAGE_SIZE_TOO_LARGE_10MB') {
      this.toastr.error('Dung lượng hình ảnh không được quá 10MB!', 'Thông báo');
    } else if (error.status === 400 && error.error === 'CATEGORY_NOT_FOUND') {
      this.toastr.error('Danh mục không tồn tại!', 'Thông báo');
    } else if (error.status === 400 && error.error === 'BRAND_NOT_FOUND') {
      this.toastr.error('Thương hiệu không tồn tại!', 'Thông báo');
    } else {
      this.toastr.error('Lỗi không xác định.', 'Thông báo');
    }
  }

  findProductById(id: number) {
    this.productService.findById(id).subscribe({
      next: (data: any) => {
        this.productForm.patchValue(data);
        this.productForm.get('categoryId')?.setValue(data.categoryId);
        this.productForm.get('brandId')?.setValue(data.brandId);
        
        this.selectedImageUrl = Environment.apiBaseUrl + '/images/' + data.thumbnail;
        this.selectedImageFile = new File([""], "filename");
        this.selectedImageProductFiles = [];
        this.selectedImageProductUrl = data.images.map((image: string) => {
          return image;
        });
        this.productForm.get('images')?.setValue(this.selectedImageProductFiles);
        this.productForm.get('enabled')?.setValue(data.enabled.toString());
        this.productForm.setControl('productColors', new FormArray([]));
        data.productColors.forEach((productColor: any) => {
          this.productColors.push(
            new FormGroup({
              id: new FormControl(productColor.id),
              color: new FormControl(productColor.color, [Validators.required]),
            })
          );
        });
      },
      error: (err: any) => {
        this.toastr.error(err.error, "Thất bại");
      }
    });
  }
}

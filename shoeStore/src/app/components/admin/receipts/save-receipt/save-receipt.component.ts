import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormArray, FormControl, FormGroup, Validators } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductService } from 'src/app/service/product.service';
import { ReceiptService } from 'src/app/service/receipt.service';
import { ToastrService } from 'ngx-toastr';
import { ProductModel } from 'src/app/model/product.model';
import { SupplierService } from 'src/app/service/supplier.service';
import { SupplierModel } from 'src/app/model/supplier.model';
import { Subject } from 'rxjs';
import { takeUntil, debounceTime, distinctUntilChanged, switchMap } from 'rxjs/operators';


@Component({
  selector: 'app-save-receipt',
  templateUrl: './save-receipt.component.html',
  styleUrls: ['./save-receipt.component.css']
})
export class SaveReceiptComponent implements OnInit {
  duplicateName: string = '';
  errorDate: string = '';
  titleString: string = "";

  totalMoney: number = 0;
  totalPrices: number[] = [];

  products: ProductModel[] = [];
  supliers: SupplierModel[] = [];
  selectedProducts: ProductModel[] = [];

  colors: any;
  sizes: any;

  count: number = 1;

  isDisplayNone: boolean = false;
  btnSave: string = "";

  receiptForm: FormGroup = new FormGroup({
    id: new FormControl(null),
    supplierId: new FormControl(null, [Validators.required]),
    receiptDetails: new FormArray([
      new FormGroup({
        id: new FormControl(null),
        price: new FormControl(null, [Validators.required, Validators.min(0), Validators.max(1000000000)]),
        quantity: new FormControl(null, [Validators.required, Validators.min(1), Validators.max(1000)]),
        productDetailsId: new FormControl(null, [Validators.required]),

        productId: new FormControl(null),
        productColorId: new FormControl(null),
      })
    ])
  });

  get receiptDetails() {
    return this.receiptForm.get('receiptDetails') as FormArray;
  }

  constructor(
    private title: Title,
    private productService: ProductService,
    private receiptService: ReceiptService,
    private supplierService: SupplierService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private toastr: ToastrService) {
  }

  ngOnInit() {
    if (this.activatedRoute.snapshot.params["id"] === undefined) {
      this.btnSave = "Thêm mới";
      this.titleString = "Tạo đơn nhập hàng";
    } else {
      this.titleString = "Cập nhật đơn nhập hàng";
      this.btnSave = "Cập nhật";
      this.findReceiptById(this.activatedRoute.snapshot.params["id"]);
    }
    this.title.setTitle(this.titleString);
    this.findAllSupplier();
    this.findAllProduct();

    const receiptDetailsArray = this.receiptForm.get('receiptDetails') as FormArray;
    receiptDetailsArray.valueChanges.subscribe(() => {
      this.totalMoney = this.getTotalMoney();
    });

    receiptDetailsArray.valueChanges.subscribe(() => {
      this.totalPrices = this.getTotalPrice();
    });
  }

  findAllSupplier() {
    this.supplierService.findAll(1, 100, "ASC", "name", "").subscribe(
      (data: any) => {
        this.supliers = data.content;
      }
    );
  }

  findReceiptById(id: number) {
    this.receiptService.findById(id).subscribe({
      next: (data: any) => {
        this.receiptForm.patchValue(data);
        this.selectedProducts = data.products;
      },
      error: (err: any) => {
        console.log(err);
        this.toastr.error("Không tìm thấy hóa đơn nhập này", "Thông báo");
      }
    });
  }

  addReceiptDetails() {

    if (this.receiptForm.invalid) {
      this.toastr.error("Vui lòng nhập đầy đủ thông tin", "Thông báo");
      return;
    }

    const receiptDetails = this.receiptForm.get('receiptDetails') as FormArray;
    const receiptDetailsGroup = receiptDetails.at(receiptDetails.length - 1) as FormGroup;
    const productDetailsId = receiptDetailsGroup.get('productDetailsId') as FormControl;

    // Kiểm tra xem productDetailsId đã được chọn trước đó hay chưa
    if (productDetailsId.value && productDetailsId.value !== undefined && !this.isProductDetailsIdSelected(productDetailsId.value)) {
      // Nếu chưa được chọn trước đó, thực hiện các bước tiếp theo
      this.productService.findProductDetailsById(productDetailsId.value).subscribe({
        next: (data: any) => {
          const receiptDetails = this.receiptForm.get('receiptDetails') as FormArray;
          receiptDetails.push(
            new FormGroup({
              id: new FormControl(null),
              price: new FormControl(null, [Validators.required, Validators.min(0), Validators.max(1000000000)]),
              quantity: new FormControl(null, [Validators.required, Validators.min(1), Validators.max(1000)]),
              productDetailsId: new FormControl(null, [Validators.required]),
              productId: new FormControl(null),
              productColorId: new FormControl(null),
            })
          );

          this.count++;
          // Đánh dấu là productDetailsId đã được chọn
          this.markProductDetailsIdSelected(productDetailsId.value);
        },
        error: (err: any) => {
          console.log(err);
          this.toastr.error("Không tìm thấy sản phẩm này", "Thông báo");
        }
      });
    } else {
      this.toastr.error('Sản phẩm với màu sắc và kích cỡ này đã được chọn', 'Thông báo');
    }
  }

  // Các hàm hỗ trợ
  private selectedProductDetailsIds: Set<any> = new Set<any>();

  private isProductDetailsIdSelected(id: any): boolean {
    console.log(this.selectedProductDetailsIds);
    return this.selectedProductDetailsIds.has(id);
  }

  private markProductDetailsIdSelected(id: any): void {
    this.selectedProductDetailsIds.add(id);
    console.log(this.selectedProductDetailsIds);
  }


  getTotalMoney(): number {
    const receiptDetails = this.receiptForm.get('receiptDetails') as FormArray;
    let total = 0;

    receiptDetails.controls.forEach((control: AbstractControl<any, any>) => {
      const price = (control.get('price') as FormControl)?.value || 0;
      const quantity = (control.get('quantity') as FormControl)?.value || 0;
      total += price * quantity;
    });


    return total;
  }

  removeProductDetails(index: number) {
    const receiptDetails = this.receiptForm.get('receiptDetails') as FormArray;
    if (receiptDetails.length > 1) {
      this.selectedProductDetailsIds.delete(receiptDetails.at(index - 1).get('productDetailsId')?.value);
      console.log(this.selectedProductDetailsIds);
      receiptDetails.removeAt(index);
      this.count--;
    }
  }

  onSubmit() {
    if (this.receiptForm.invalid) {
      console.log("Form invalid");
      return;
    }
    this.create();
  }

  create() {
    // kiểm tra trùng productDeatilsId
    const receiptDetails = this.receiptForm.get('receiptDetails') as FormArray;
    const productDetailsIds: any[] = [];
    receiptDetails.controls.forEach((control: AbstractControl<any, any>) => {
      const productDetailsId = (control.get('productDetailsId') as FormControl)?.value;
      productDetailsIds.push(productDetailsId);
    });

    const set = new Set(productDetailsIds);
    if (set.size !== productDetailsIds.length) {
      this.toastr.error('Không thể tạo hóa đơn với sản phẩm trùng nhau', 'Thông báo');
      return;
    }

    this.receiptService.create(this.receiptForm.value).subscribe({
      next: () => {
        this.toastr.success("Tạo hóa đơn nhập thành công", "Thông báo");
        this.router.navigateByUrl("/admin/receipt");
      },
      error: (err: any) => {
        this.handleError(err);
      }
    });
  }

  private handleError(error: any): void {
    console.log(error);
    if (error.status === 400 && error.error === 'EMPLOYEE_NOT_FOUND') {
      this.duplicateName = 'Không tìm thấy thông tin đăng nhập, Vui lòng đăng nhập lại!';
    }
    if (error.status === 400 && error.error === 'SUPPLIER_NOT_FOUND') {
      this.errorDate = 'Không tìm thấy nhà cung cấp này!';
    }
    if (error.status === 400 && error.error === 'PRODUCT_DETAILS_NOT_FOUND') {
      this.toastr.error('Không tìm thấy sản phẩm này!', 'Thông báo');
    }
    if (error.status === 400) {
      console.log(error.error);
    }
  }

  private unsubscribe$: Subject<void> = new Subject<void>();

  findAllProduct() {
    this.productService.findAllOption(1, 100, "ASC", "name").pipe(
      takeUntil(this.unsubscribe$)
    ).subscribe(
      (data: any) => {
        this.products = data.content;
      }
    );
  }

  onKeyDown(event: any, type: boolean) {
    const input = event.target as HTMLInputElement;
    const isBackspaceOrDelete = event.key === 'Backspace' || event.key === 'Delete';
    const hasSelection = input.selectionStart !== input.selectionEnd;

    if (type) {
      // Kiểm tra nếu số lượng ký tự vượt quá maxLength
      if (input.value.length >= 10 && !isBackspaceOrDelete && !hasSelection) {
        // Ngăn chặn sự kiện và không cho phép nhập
        event.preventDefault();
      }
    }
    else {
      // Kiểm tra nếu số lượng ký tự vượt quá maxLength
      if (input.value.length >= 4 && !isBackspaceOrDelete && !hasSelection) {
        // Ngăn chặn sự kiện và không cho phép nhập
        event.preventDefault();
      }
    }
  }

  getProductColorIdControl(index: number): FormControl {
    const receiptDetailsFormArray = this.receiptForm.get('receiptDetails') as FormArray;
    const receiptDetailsGroup = receiptDetailsFormArray.at(index) as FormGroup;
    const productIdControl = receiptDetailsGroup.get('productId') as FormControl;

    productIdControl.valueChanges.pipe().subscribe((productId: any) => {
      this.products.forEach((product: ProductModel) => {
        if (product.id === productId) {
          this.colors = product.productColors;
          receiptDetailsGroup.get('productColorId')?.setValue(this.colors[0]?.id); // Đảm bảo mảng colors không rỗng trước khi gán giá trị
        }
      });
    });
    return productIdControl;
  }

  getProductDetailsIdControl(index: number): FormControl {
    const receiptDetailsFormArray = this.receiptForm.get('receiptDetails') as FormArray;
    const receiptDetailsGroup = receiptDetailsFormArray.at(index) as FormGroup;
    const productColorIdControl = receiptDetailsGroup.get('productColorId') as FormControl;

    productColorIdControl.valueChanges.pipe().subscribe((productColorId: any) => {
      this.colors.forEach((color: any) => {
        if (color.id === productColorId) {
          this.sizes = color.productDetails;
          receiptDetailsGroup.get('productDetailsId')?.setValue(this.sizes[0]?.id); // Đảm bảo mảng sizes không rỗng trước khi gán giá trị
        }
      });
    });

    return productColorIdControl;
  }


  getTotalPrice(): number[] {
    const receiptDetails = this.receiptForm.get('receiptDetails') as FormArray;
    const totals: number[] = [];

    receiptDetails.controls.forEach((control: AbstractControl<any, any>) => {
      const price = (control.get('price') as FormControl)?.value || 0;
      const quantity = (control.get('quantity') as FormControl)?.value || 0;
      const total = price * quantity;
      totals.push(total);
    });

    return totals;
  }
}

import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { AbstractControl, FormArray, FormControl, FormGroup, Validators } from '@angular/forms';
import { Environment } from 'src/app/environment/environment';
import { ProductService } from 'src/app/service/product.service';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { OrderService } from 'src/app/service/order.service';

@Component({
  selector: 'app-save-return',
  templateUrl: './save-return.component.html',
  styleUrls: ['./save-return.component.css']
})
export class SaveReturnComponent implements OnInit {
  @ViewChild('btnAddReturn') btnAddReturn!: ElementRef;
  baseUrl: string = `${Environment.apiBaseUrl}`;
  titleString: string = "";
  btnSave: string = "";

  listProductSelected: any[] = [];

  totalMoney: number = 0;
  totalPrices: number[] = [];

  listProductInOrder: any[] = [];

  orderCompleted: any[] = [];

  order: any;

  returnForm: FormGroup = new FormGroup({
    id: new FormControl(null),
    status: new FormControl(false),
    orderId: new FormControl(null, [Validators.required]),
    returnProductDetails: new FormArray([
      new FormGroup({
        id: new FormControl(null),
        price: new FormControl(null),
        quantity: new FormControl(null, [Validators.required, Validators.min(1), Validators.max(1000)]),
        productDetailsId: new FormControl(null, [Validators.required]),
        reason: new FormControl(null)
      })
    ])
  });

  constructor(
    private title: Title,
    private orderService: OrderService,
    private productService: ProductService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private toastr: ToastrService) {
  }

  ngOnInit() {
    if (this.activatedRoute.snapshot.params["id"] === undefined) {
      this.btnSave = "Thêm mới";
      this.titleString = "Tạo phiếu đổi trả";
    } else {
      this.titleString = "Cập nhật phiếu đổi trả";
      this.btnSave = "Cập nhật";
    }
    this.title.setTitle(this.titleString);
    this.getAllOrder();

    this.returnForm.controls['orderId'].valueChanges.subscribe({
      next: (value) => {
        this.order = this.orderCompleted.find(x => x.id === value);
        this.listProductInOrder = this.order.orderDetails.slice();
        console.log(this.order);
        console.log('danh scash', this.listProductInOrder);
      }
    });

    const returnProductDetailsArray = this.returnForm.get('returnProductDetails') as FormArray;
    returnProductDetailsArray.valueChanges.subscribe(() => {
      this.totalMoney = this.getTotalMoney();
    });

    returnProductDetailsArray.valueChanges.subscribe(() => {
      this.totalPrices = this.getTotalPrice();
    });
  }

  get returnProductDetails() {
    return this.returnForm.get('returnProductDetails') as FormArray;
  }

  onSubmit() {
    if (this.returnForm.invalid) {
      console.log("Form invalid");
      return;
    }
    this.create();
  }

  create() {
    console.log(this.returnForm.value);
    // kiểm tra trùng productDeatilsId
    const returnProductDetails = this.returnForm.get('returnProductDetails') as FormArray;
    const productDetailsIds: any[] = [];
    returnProductDetails.controls.forEach((control: AbstractControl<any, any>) => {
      const productDetailsId = (control.get('returnProductDetails') as FormControl)?.value;
      productDetailsIds.push(productDetailsId);
    });

    const set = new Set(productDetailsIds);
    if (set.size !== productDetailsIds.length) {
      this.toastr.error('Sản phẩm hoàn trả trùng lặp', 'Thông báo');
      return;
    }
  }

  getAllOrder() {
    this.orderService.findAllCompleted().subscribe({
      next: (response) => {
        console.log(response);
        this.orderCompleted = response;
      },
      error: (error) => {
        this.handleError(error);
      }
    });
  }

  private handleError(error: any): void {
    console.log(error);
    if (error.status === 400 && error.error === 'PRODUCT_DETAILS_NOT_FOUND') {
      this.toastr.error('Không tìm thấy sản phẩm này!', 'Thông báo');
    }
    if (error.status === 400) {
      console.log(error.error);
    }
  }

  getTotalPrice(): number[] {
    const returnProductDetails = this.returnForm.get('returnProductDetails') as FormArray;
    const totals: number[] = [];

    returnProductDetails.controls.forEach((control: AbstractControl<any, any>) => {
      const price = (control.get('price') as FormControl)?.value || 0;
      const quantity = (control.get('quantity') as FormControl)?.value || 0;
      const total = price * quantity;
      totals.push(total);
    });

    return totals;
  }

  getTotalMoney(): number {
    const receiptDetails = this.returnForm.get('returnProductDetails') as FormArray;
    let total = 0;

    receiptDetails.controls.forEach((control: AbstractControl<any, any>) => {
      const price = (control.get('price') as FormControl)?.value || 0;
      const quantity = (control.get('quantity') as FormControl)?.value || 0;
      total += price * quantity;
    });


    return total + this.order?.total_fee;
  }

  removeProductDetails(index: number) {
    const receiptDetails = this.returnForm.get('returnProductDetails') as FormArray;
    if (receiptDetails.length > 1) {
      // lấy giá trị tại vị trí index
      const productDetailsId = receiptDetails.at(index).get('productDetailsId')?.value;
      // so sánh với listProductSelected
      const indexProductSelected = this.listProductSelected.findIndex(x => x.productDetailsId === productDetailsId);
      // nếu có thì thêm lại vào listProductInOrder
      if (indexProductSelected !== -1) {
        this.listProductInOrder.push(this.listProductSelected[indexProductSelected]);
      }
      receiptDetails.removeAt(index);
    }
    else {
      // clear all value
      receiptDetails.at(0).reset();
    }
  }

  getProductColorIdControl(index: number): FormControl {
    const receiptDetailsFormArray = this.returnForm.get('returnProductDetails') as FormArray;
    const receiptDetailsGroup = receiptDetailsFormArray.at(index) as FormGroup;
    const productIdControl = receiptDetailsGroup.get('productDetailsId') as FormControl;

    productIdControl.valueChanges.pipe().subscribe((productId: any) => {
      this.listProductInOrder.forEach((product: any) => {
        if (product.productDetailsId === productId) {
          // xóa sản phẩm đã chọn khỏi listProductInOrder
          const index = this.listProductInOrder.findIndex(x => x.productDetailsId === productId);
          this.listProductInOrder.splice(index, 1);
          this.listProductSelected.push(product); ////

          if (this.listProductInOrder.length === 0) {
            this.btnAddReturn.nativeElement.disabled = true;
          }
          receiptDetailsGroup.get('price')?.setValue(product.productPrice); // Đảm bảo mảng colors không rỗng trước khi gán giá trị
        }
      });
    });
    return productIdControl;
  }

  // kiểm tra số lượng nhập với số lượng đã mua
  checkQuantity(index: number): boolean {
    const receiptDetailsFormArray = this.returnForm.get('returnProductDetails') as FormArray;
    const receiptDetailsGroup = receiptDetailsFormArray.at(index) as FormGroup;
    const productIdControl = receiptDetailsGroup.get('productDetailsId') as FormControl;

    let quantity = 0;
    this.order?.orderDetails.forEach((product: any) => {
      if (product.productDetailsId === productIdControl.value) {
        quantity = product.quantity;
      }
    });

    if (receiptDetailsGroup.get('quantity')?.value > quantity) {
      this.returnForm.get('returnProductDetails')?.setErrors({ 'quantity': true });
      return true;
    }
    return false;
  }

  onKeyDown(event: KeyboardEvent, index: number): void {
    const input = event.target as HTMLInputElement;
    const isBackspaceOrDelete = event.key === 'Backspace' || event.key === 'Delete';
    const hasSelection = input.selectionStart !== input.selectionEnd;
    if (this.checkQuantity(index) && !isBackspaceOrDelete && !hasSelection) {
      event.preventDefault(); // Ngăn chặn hành động mặc định của sự kiện keydown
      console.log('Số lượng không hợp lệ');
    }
  }
  

  addReceiptDetails() {
    const receiptDetails = this.returnForm.get('returnProductDetails') as FormArray;
    const receiptDetailsGroup = receiptDetails.at(receiptDetails.length - 1) as FormGroup;
    const productDetailsId = receiptDetailsGroup.get('productDetailsId') as FormControl;

    this.productService.findProductDetailsById(productDetailsId.value).subscribe({
      next: (data: any) => {
        const receiptDetails = this.returnForm.get('returnProductDetails') as FormArray;
        receiptDetails.push(
          new FormGroup({
            id: new FormControl(null),
            price: new FormControl(null),
            quantity: new FormControl(null, [Validators.required, Validators.min(1), Validators.max(1000)]),
            productDetailsId: new FormControl(null, [Validators.required]),
            reason: new FormControl(null)
          })
        );
      },
      error: (err: any) => {
        console.log(err);
        this.toastr.error("Không tìm thấy sản phẩm này", "Thông báo");
      }
    });
  }
}

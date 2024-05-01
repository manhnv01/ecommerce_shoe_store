import { Component, OnInit, ViewChild } from '@angular/core';
import { Environment } from 'src/app/environment/environment';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { OrderService } from 'src/app/service/order.service';
import { OrderModel } from 'src/app/model/order.model';
import { FormGroup } from '@angular/forms';
import { FormControl } from '@angular/forms';
import { Validators } from '@angular/forms';
import { ElementRef } from '@angular/core';
import { FormArray } from '@angular/forms';
import { AbstractControl } from '@angular/forms';
import { ReturnService } from 'src/app/service/return.service';
import { ReportService } from 'src/app/service/report.service';

@Component({
  selector: 'app-user-order-detail',
  templateUrl: './user-order-detail.component.html',
  styleUrls: ['./user-order-detail.component.css']
})
export class UserOrderDetailComponent implements OnInit {

  @ViewChild('btnCloseModal') btnCloseModal!: ElementRef;
  @ViewChild('textAreaOrtherReason') textAreaOrtherReason!: ElementRef;
  @ViewChild('btnAddReturn') btnAddReturn!: ElementRef;
  @ViewChild('btnCloseOffcanvas') btnCloseOffcanvas!: ElementRef;

  listProductSelected: any[] = [];
  listProductInOrder: any[] = [];
  listProductSecondary: any[] = [];

  order: any;
  totalMoney: number = 0; // Tổng tiền hàng
  totalPrincipal: number = 0; // Tổng tiền hàng gốc
  totalPrice: number = 0; // Tổng tiền hàng sau khi giảm giá
  baseUrl: string = `${Environment.apiBaseUrl}`;
  constructor(private title: Title, private toastr: ToastrService,
    private router: Router, private activatedRoute: ActivatedRoute,
    private returnService: ReturnService,
    private reportService: ReportService,
    private orderService: OrderService) {
  }

  returnForm: FormGroup = new FormGroup({
    id: new FormControl(null),
    status: new FormControl('RETURN_PENDING'),
    orderId: new FormControl(null, [Validators.required]),
    returnProductDetails: new FormArray([
      new FormGroup({
        id: new FormControl(null),
        quantitySold: new FormControl(null),
        quantity: new FormControl(null, [Validators.required, Validators.min(1), Validators.max(1000)]),
        productDetailsId: new FormControl(null, [Validators.required]),
        reason: new FormControl(null, [Validators.maxLength(100)]),
        returnType: new FormControl(null, [Validators.required])
      })
    ])
  });

  cancelOrderForm: FormGroup = new FormGroup({
    id: new FormControl(null),
    orderStatus: new FormControl(4),
    cancelReason: new FormControl(null, [Validators.required, Validators.maxLength(100)]),
    typeReason: new FormControl(null)
  });

  ngOnInit(): void {
    this.title.setTitle('Chi tiết đơn hàng');
    this.findById();
  }

  onRadioChangeReason(event: any) {
    this.cancelOrderForm.controls['typeReason'].setValue(event.target.value);
    if (event.target.value == 'otherReason') {
      this.textAreaOrtherReason.nativeElement.classList.remove('d-none');
      this.textAreaOrtherReason.nativeElement.focus();
      // nếu chọn textArea có giá trị thì set giá trị cho cancelReason là giá trị của textArea
      if (this.textAreaOrtherReason.nativeElement.value) {
        this.cancelOrderForm.controls['cancelReason'].setValue(this.textAreaOrtherReason.nativeElement.value);
      }
      else {
        this.cancelOrderForm.controls['cancelReason'].setValue('Lý do khác');
      }
    }
    else {
      this.textAreaOrtherReason.nativeElement.classList.add('d-none');
      this.cancelOrderForm.controls['cancelReason'].setValue(event.target.value);
    }
  }

  findById() {
    this.orderService.findByIdWithClient(this.activatedRoute.snapshot.params["id"]).subscribe({
      next: (data: any) => {
        this.order = data;
        this.listProductInOrder = this.order.orderDetails.slice();
        this.listProductSecondary = this.order.orderDetails.slice();
        this.returnForm.controls['orderId'].setValue(this.order.id);
        console.log(this.order);
      }
      ,
      error: (error: any) => {
        if (error.status == 400 && error.error == 'ORDER_NOT_FOUND') {
          this.toastr.error('Đơn hàng không tồn tại');
        } else {
          this.toastr.error('Lỗi thực hiện, vui lòng thử lại sau');
        }
      }
    })
  }

  inputOtherReason(event: any) {
    if (event.target.value) {
      this.cancelOrderForm.controls['cancelReason'].setValue(event.target.value);
    }
    else {
      this.cancelOrderForm.controls['cancelReason'].setValue('Lý do khác');
    }
  }

  resetForm() {
    this.cancelOrderForm.reset();
  }

  cancelOrder() {
    this.cancelOrderForm.controls['id'].setValue(this.order.id);
    this.orderService.updateOrderStatus(this.cancelOrderForm.value).subscribe({
      next: (data: any) => {
        this.toastr.success('Hủy đơn hàng thành công');
        this.btnCloseModal.nativeElement.click();
        this.findById();
      },
      error: (error: any) => {
        if (error.status == 400 && error.error == 'ORDER_NOT_FOUND') {
          this.toastr.error('Đơn hàng không tồn tại.');
        } else {
          this.toastr.error('Lỗi không xác định.');
        }
      }
    });
  }

  repayment() {
    this.orderService.payment(this.order?.totalMoney + this.order?.total_fee - this.order?.totalDiscount, this.order?.id).subscribe({
      next: (data: any) => {
        window.location.href = data.redirectUrl;
      },
      error: (error: any) => {
        console.log(error);
        this.toastr.error('Lỗi không xác định.');
      }
    });
  }



  /////////////////////////////////////////// ĐỔI TRẢ */////////////////////////////////

  get returnProductDetails() {
    return this.returnForm.get('returnProductDetails') as FormArray;
  }

  addNewDetails() {
    // kiểm tra returnForm invalid thì không thêm mới
    if (this.returnForm.invalid) {
      this.toastr.error("Vui lòng nhập đầy đủ thông tin");
      return;
    }

    this.listProductSelected = [];

    const receiptDetails = this.returnForm.get('returnProductDetails') as FormArray;
    receiptDetails.push(
      new FormGroup({
        id: new FormControl(null),
        quantitySold: new FormControl(null),
        quantity: new FormControl(null, [Validators.required, Validators.min(1), Validators.max(1000)]),
        productDetailsId: new FormControl(null, [Validators.required]),
        reason: new FormControl(null, [Validators.maxLength(100)]),
        returnType: new FormControl(null, [Validators.required])
      })
    );
  }


  onSubmit() {
    if (this.returnForm.invalid) {
      console.log("Form invalid");
      return;
    }
    this.create();
  }

  create() {
    // đổi tất cả các returnType từ string sang boolean
    const returnProductDetails = this.returnForm.get('returnProductDetails') as FormArray;
    returnProductDetails.controls.forEach((control: AbstractControl<any, any>) => {
      control.get('returnType')?.setValue(control.get('returnType')?.value === 'true');
    });
    console.log(this.returnForm.value);

    this.returnService.saveReturn(this.returnForm.value).subscribe({
      next: (response) => {
        console.log(response);
        this.toastr.success('Yêu cầu đổi trả thành công!');
        this.listProductInOrder = this.order.orderDetails.slice();
        this.btnCloseOffcanvas.nativeElement.click();
      },
      error: (error) => {
        this.handleError(error);
      }
    });
  }
  private handleError(error: any): void {
    console.log(error);
    if (error.status === 400) {
      if (error.error === 'QUANTITY_RETURN_PRODUCT_MUST_BE_LESS_THAN_QUANTITY_ORDER') {
        this.toastr.error('Số lượng đổi trả phải nhỏ hơn số lượng đã mua!');
      }
      if (error.error === 'ORDER_NOT_COMPLETED') {
        this.toastr.error('Hóa đơn chưa hoàn thành!');
      }
      if (error.error === 'ORDER_NOT_FOUND') {
        this.toastr.error('Hóa đơn không tồn tại!');
      }
      if (error.error === 'RETURN_PRODUCT_NOT_BELONG_ORDER') {
        this.toastr.error('Sản phẩm đổi trả không thuộc hóa đơn!');
      }
      if (error.error === 'PRODUCT_DETAILS_NOT_FOUND') {
        this.toastr.error('Sản phẩm không tồn tại!');
      }
      if (error.error === 'RETURN_PRODUCT_PENDING_EXISTED') {
        this.toastr.error('Đơn hàng này đang có yêu cầu đổi trả chưa xử lý vui lòng chờ!');
      }
      if (error.error === 'RETURN_PRODUCT_STATUS_INVALID') {
        this.toastr.error('Trạng thái đổi trả không hợp lệ!');
      }
    } else {
      this.toastr.error('Lỗi không xác định!');
    }
  }

  removeProductDetails(index: number) {
    const receiptDetails = this.returnForm.get('returnProductDetails') as FormArray;
    if (receiptDetails.length > 1) {
      const productDetailsId = receiptDetails.at(index).get('productDetailsId')?.value;
      const indexProductSelected = this.listProductSecondary.findIndex(x => x.productDetailsId === productDetailsId);
      if (indexProductSelected !== -1) {
        this.listProductInOrder.push(this.listProductSecondary[indexProductSelected]);
      }
      receiptDetails.removeAt(index);
      this.btnAddReturn.nativeElement.disabled = false;
    }
    else {
      const productDetailsId = receiptDetails.at(index).get('productDetailsId')?.value;
      const indexProductSelected = this.listProductSecondary.findIndex(x => x.productDetailsId === productDetailsId);
      if (indexProductSelected !== -1) {
        this.listProductInOrder.push(this.listProductSecondary[indexProductSelected]);
      }
      receiptDetails.at(0).reset();
    }
  }

  getProductItem(index: number): FormControl {
    const receiptDetailsFormArray = this.returnForm.get('returnProductDetails') as FormArray;
    const receiptDetailsGroup = receiptDetailsFormArray.at(index) as FormGroup;
    const productIdControl = receiptDetailsGroup.get('productDetailsId') as FormControl;

    productIdControl.valueChanges.pipe().subscribe((productId: any) => {
      this.listProductInOrder.forEach((product: any) => {
        if (product.productDetailsId === productId) {
          // Lấy thằng cuối trong listProductSelected ném vào listProductInOrder và xóa khỏi listProductSelected
          if (this.listProductSelected.length > 0) {
            this.listProductInOrder.push(this.listProductSelected[this.listProductSelected.length - 1]);
            this.listProductSelected.pop();
          }

          // xóa sản phẩm đã chọn khỏi listProductInOrder
          const index = this.listProductInOrder.findIndex(x => x.productDetailsId === productId);
          this.listProductInOrder.splice(index, 1);
          this.listProductSelected.push(product); ////

          if (this.listProductInOrder.length === 0) {
            this.btnAddReturn.nativeElement.disabled = true;
          }
          else
            this.btnAddReturn.nativeElement.disabled = false;
          this.returnForm.controls['orderId'].setValue(this.order.id);
          receiptDetailsGroup.get('quantitySold')?.setValue(product.quantity);
          receiptDetailsGroup.get('quantity')?.setValue(1);
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
    if (event.keyCode === 38 || event.keyCode === 40 || event.key === '-' || this.checkQuantity(index) && !isBackspaceOrDelete && !hasSelection) {
      event.preventDefault(); // Ngăn chặn hành động mặc định của sự kiện keydown
      console.log('Số lượng không hợp lệ');

      // chặn nhập ký tự --
      if (event.key === '-') {
        event.preventDefault();
      }
    }
  }

  cancelRequire() {
    const returnProductDetailsArray = this.returnForm.get('returnProductDetails') as FormArray;
    returnProductDetailsArray.clear();
    this.listProductInOrder = this.order.orderDetails.slice();
    this.addNewDetails();
  }

  checkTimeReturnProduct(): boolean {

    if (!this.order?.completedDate == null) {
      return false;
    }

    const date = new Date();
    const dateOrder = new Date(this.order?.completedDate);
    const time = Math.abs(date.getTime() - dateOrder.getTime());
    const diffDays = Math.ceil(time / (1000 * 3600 * 24));
    if (diffDays > 7) {
      return false;
    }
    return true;
  }

  /////////////////////// Xem online /////////////////////////

  exportInvoicePdf() {
    this.reportService.exportInvoicePdf(this.order?.id).subscribe({
      next: (data: any) => {
        const blob = new Blob([data], { type: 'application/pdf' });
        const url = window.URL.createObjectURL(blob);
        window.open(url);
      },
      error: (error: any) => {
        console.log(error);
        this.toastr.error('Lỗi không xác định');
      }
    });
  }

  ///////////////// Tải trực tiếp có cấu hình tên file /////////////////////////

  // exportInvoicePdf() {
  //   this.reportService.exportInvoicePdf(this.order?.id).subscribe({
  //     next: (data: any) => {
  //       const blob = new Blob([data], { type: 'application/pdf' });
  //       const url = window.URL.createObjectURL(blob);
  
  //       // Tạo phần tử anchor để tải xuống
  //       const a = document.createElement('a');
  //       a.href = url;
  //       a.download = `invoice-${this.order?.id}-shoesstation.pdf`;  // Đặt tên file tải xuống
  //       document.body.appendChild(a); // Thêm phần tử vào body để có thể click
  //       a.click();  // Kích hoạt sự kiện click để tải xuống
  
  //       // Dọn dẹp
  //       window.URL.revokeObjectURL(url);
  //       a.remove();  // Xóa phần tử anchor sau khi tải xong
  //     },
  //     error: (error: any) => {
  //       console.log(error);
  //       this.toastr.error('Lỗi không xác định');
  //     }
  //   });
  // }
}

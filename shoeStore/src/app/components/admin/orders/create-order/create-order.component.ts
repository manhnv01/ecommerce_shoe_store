import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductService } from 'src/app/service/product.service';
import { ReceiptService } from 'src/app/service/receipt.service';
import { SupplierService } from 'src/app/service/supplier.service';
import { ProductModel } from 'src/app/model/product.model';
import { SupplierModel } from 'src/app/model/supplier.model';
import { FormControl, FormGroup, FormArray, Validators, AbstractControl } from '@angular/forms';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { ToastrService } from 'ngx-toastr';
import { OrderService } from 'src/app/service/order.service';
import { ReportService } from 'src/app/service/report.service';


@Component({
  selector: 'app-create-order',
  templateUrl: './create-order.component.html',
  styleUrls: ['./create-order.component.css']
})
export class CreateOrderComponent implements OnInit {
  duplicateName: string = '';
  errorDate: string = '';
  titleString: string = "Tạo đơn hàng";

  totalMoney: number = 0;
  totalPrices: number[] = [];

  products: ProductModel[] = [];
  supliers: SupplierModel[] = [];
  selectedProducts: ProductModel[] = [];

  colors: any;
  sizes: any;

  count: number = 1;

  isDisplayNone: boolean = false;

  orderForm: FormGroup = new FormGroup({
    name: new FormControl(null, [Validators.required]),
    address: new FormControl('Số 97, Đặng Văn Ngữ, Quận Đống Đa, Hà Nội'),
    phone: new FormControl(null, [Validators.required, Validators.pattern('^(0)[0-9]{9}$')]),
    orderType: new FormControl(false),
    paymentMethod: new FormControl(null, [Validators.required]),
    paymentStatus: new FormControl(true),
    orderStatus: new FormControl(3),
    orderDetails: new FormArray([
      new FormGroup({
        price: new FormControl(null, [Validators.required, Validators.min(0), Validators.max(1000000000)]),
        quantity: new FormControl(null, [Validators.required, Validators.min(1), Validators.max(1000)]),
        productDetailsId: new FormControl(null, [Validators.required]),

        productId: new FormControl(null),
        productColorId: new FormControl(null),
      })
    ])
  });

  get orderDetails() {
    return this.orderForm.get('orderDetails') as FormArray;
  }

  constructor(
    private title: Title,
    private productService: ProductService,
    private orderService: OrderService,
    private reportService: ReportService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private toastr: ToastrService) {
  }

  ngOnInit() {
    this.title.setTitle(this.titleString);
    this.findAllProduct();

    const orderDetailsArray = this.orderForm.get('orderDetails') as FormArray;
    orderDetailsArray.valueChanges.subscribe(() => {
      this.totalMoney = this.getTotalMoney();
    });

    orderDetailsArray.valueChanges.subscribe(() => {
      this.totalPrices = this.getTotalPrice();
    });
  }

  addOrderDetails() {

    if (this.orderForm.invalid) {
      this.toastr.error("Vui lòng nhập đầy đủ thông tin", "Thông báo");
      return;
    }

    const orderDetails = this.orderForm.get('orderDetails') as FormArray;
    const orderDetailsGroup = orderDetails.at(orderDetails.length - 1) as FormGroup;
    const productDetailsId = orderDetailsGroup.get('productDetailsId') as FormControl;

    // Kiểm tra xem productDetailsId đã được chọn trước đó hay chưa
    if (productDetailsId.value && productDetailsId.value !== undefined && !this.isProductDetailsIdSelected(productDetailsId.value)) {
      // Nếu chưa được chọn trước đó, thực hiện các bước tiếp theo
      this.productService.findProductDetailsById(productDetailsId.value).subscribe({
        next: (data: any) => {
          const orderDetails = this.orderForm.get('orderDetails') as FormArray;
          orderDetails.push(
            new FormGroup({
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
    const orderDetails = this.orderForm.get('orderDetails') as FormArray;
    let total = 0;

    orderDetails.controls.forEach((control: AbstractControl<any, any>) => {
      const price = (control.get('price') as FormControl)?.value || 0;
      const quantity = (control.get('quantity') as FormControl)?.value || 0;
      total += price * quantity;
    });


    return total;
  }

  removeProductDetails(index: number) {
    const orderDetails = this.orderForm.get('orderDetails') as FormArray;
    if (orderDetails.length > 1) {
      this.selectedProductDetailsIds.delete(orderDetails.at(index - 1).get('productDetailsId')?.value);
      console.log(this.selectedProductDetailsIds);
      orderDetails.removeAt(index);
      this.count--;
    }
  }

  onSubmit() {
    if (this.orderForm.invalid) {
      console.log("Form invalid");
      return;
    }
    this.create();
  }

  create() {
    // kiểm tra trùng productDeatilsId
    const orderDetails = this.orderForm.get('orderDetails') as FormArray;
    const productDetailsIds: any[] = [];
    orderDetails.controls.forEach((control: AbstractControl<any, any>) => {
      const productDetailsId = (control.get('productDetailsId') as FormControl)?.value;
      productDetailsIds.push(productDetailsId);
    });

    const set = new Set(productDetailsIds);
    if (set.size !== productDetailsIds.length) {
      this.toastr.error('Không thể tạo hóa đơn với sản phẩm trùng nhau', 'Thông báo');
      return;
    }

    this.orderService.createOff(this.orderForm.value).subscribe({
      next: (data: any) => {
        this.toastr.success("Tạo đơn hàng thành công", "Thông báo");

        // clear form
        this.orderForm.reset();


        this.exportInvoicePdf(data.id);
      },
      error: (err: any) => {
        this.handleError(err);
      }
    });
  }

  private handleError(error: any): void {
    console.log(error);
    if (error.error === 'PRODUCT_NOT_FOUND') {
      this.toastr.error('Không tìm thấy sản phẩm', 'Thông báo');
    } else if (error.error === 'PRODUCT_QUANTITY_NOT_ENOUGH') {
      this.toastr.error('Số lượng sản phẩm không đủ', 'Thông báo');
    } else {
      this.toastr.error('Lỗi không xác định', 'Thông báo');
    }
  }

  findAllProduct() {
    this.productService.getAllNonPage().subscribe({
      next: (data: any) => {
        this.products = data;
        console.log('Danh sách sản phẩm', this.products);
      },
      error: (err: any) => {
        console.log(err);
      }
    });
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
    const orderDetailsFormArray = this.orderForm.get('orderDetails') as FormArray;
    const orderDetailsGroup = orderDetailsFormArray.at(index) as FormGroup;
    const productIdControl = orderDetailsGroup.get('productId') as FormControl;

    productIdControl.valueChanges.pipe().subscribe((productId: any) => {
      this.products.forEach((product: any) => {
        if (product.id === productId) {
          this.colors = product.productColors;
          
          orderDetailsGroup.get('price')?.setValue(product.price);

          if (product.salePrice > 0) {
            orderDetailsGroup.get('price')?.setValue(product.salePrice);
          }
          
          orderDetailsGroup.get('productColorId')?.setValue(this.colors[0]?.id); // Đảm bảo mảng colors không rỗng trước khi gán giá trị
        }
      });
    });
    return productIdControl;
  }

  getProductDetailsIdControl(index: number): FormControl {
    const orderDetailsFormArray = this.orderForm.get('orderDetails') as FormArray;
    const orderDetailsGroup = orderDetailsFormArray.at(index) as FormGroup;
    const productColorIdControl = orderDetailsGroup.get('productColorId') as FormControl;

    productColorIdControl.valueChanges.pipe().subscribe((productColorId: any) => {
      this.colors.forEach((color: any) => {
        if (color.id === productColorId) {
          this.sizes = color.productDetails;
          orderDetailsGroup.get('productDetailsId')?.setValue(this.sizes[0]?.id); // Đảm bảo mảng sizes không rỗng trước khi gán giá trị
        }
      });
    });

    return productColorIdControl;
  }


  getTotalPrice(): number[] {
    const orderDetails = this.orderForm.get('orderDetails') as FormArray;
    const totals: number[] = [];

    orderDetails.controls.forEach((control: AbstractControl<any, any>) => {
      const price = (control.get('price') as FormControl)?.value || 0;
      const quantity = (control.get('quantity') as FormControl)?.value || 0;
      const total = price * quantity;
      totals.push(total);
    });

    return totals;
  }

  /////////////////////// Xem online /////////////////////////

  exportInvoicePdf(id: number) {
    this.reportService.exportInvoicePdf(id).subscribe({
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
}

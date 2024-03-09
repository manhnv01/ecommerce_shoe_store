import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductModel } from 'src/app/model/product.model';
import { ProductService } from 'src/app/service/product.service';
import { SaleService } from 'src/app/service/sale.service';
import { ToastrService } from 'ngx-toastr';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { formatDate } from '@angular/common';
import {DatePipe} from "@angular/common";

@Component({
  selector: 'app-save-sale',
  templateUrl: './save-sale.component.html',
  styleUrls: ['./save-sale.component.css']
})
export class SaveSaleComponent implements OnInit {
  duplicateName: string = '';
  errorDate: string = '';
  titleString: string = "";

  products: ProductModel[] = [];
  selectedProducts: ProductModel[] = [];

  productId: any;

  isDisplayNone: boolean = false;
  btnSave: string = "";

  saleForm: FormGroup = new FormGroup({
    id: new FormControl(null),
    name: new FormControl('', [Validators.required, Validators.maxLength(50)]),
    discount: new FormControl('', [Validators.required, Validators.min(1), Validators.max(100)]),
    startDate: new FormControl('', [Validators.required]),
    endDate: new FormControl('', [Validators.required]),
    productIds: new FormControl([]),
  });

  constructor(
    private title: Title,
    private datePipe: DatePipe,
    private productService: ProductService,
    private saleService: SaleService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private toastr: ToastrService) {
  }

  ngOnInit() {
    if (this.activatedRoute.snapshot.params["id"] === undefined) {
      this.btnSave = "Thêm mới";
      this.titleString = "Thêm khuyến mãi";
    } else {
      this.titleString = "Cập nhật khuyến mãi";
      this.btnSave = "Cập nhật";
      this.findSaleById(this.activatedRoute.snapshot.params["id"]);
    }
    this.title.setTitle(this.titleString);

    this.findAllProduct();
  }

  findAllProduct() {
    this.productService.findAllOption(1, 100, "ASC", "name").subscribe(
      (data: any) => {
        this.products = data.content;
      }
    );
  }

  findSaleById(id: number) {
    this.saleService.findById(id).subscribe({
      next: (data: any) => {
        this.saleForm.patchValue(data);
        this.saleForm.get('startDate')?.setValue(this.datePipe.transform(data.startDate, 'yyyy-MM-dd'));
        this.saleForm.get('endDate')?.setValue(this.datePipe.transform(data.endDate, 'yyyy-MM-dd'));
        this.selectedProducts = data.products;
      },
      error: (err: any) => {
        console.log(err);
        this.toastr.error("Không tìm thấy khuyến mãi này", "Thông báo");
      }
    });
  }

  onSubmit() {
    if (this.saleForm.invalid) {
      console.log("Form invalid");
      return;
    }
    if (this.activatedRoute.snapshot.params["id"] === undefined) {
      this.create();
    } else {
      this.update();
    }
  }

  onSelect() {
    if(this.productId === undefined || this.productId === null || this.productId === "") {
      this.toastr.error("Bạn chưa chọn sản phẩm", "Thông báo");
      return;
    }
    this.saleForm.get('productIds')?.setValue([this.productId]);
    console.log(this.saleForm.value);
    this.saleService.validateProductInSale(this.saleForm.value).subscribe({
      next: (data: any) => {
        this.productService.findById(this.productId).subscribe({
          next: (data: any) => {
            if (this.selectedProducts.find(p => p.id === data.id)) {
              this.toastr.error("Sản phẩm đã được chọn", "Thông báo");
              return;
            }
            this.selectedProducts.push(data);
          },
          error: (err: any) => {
            this.toastr.error("Không tìm thấy sản phẩm này.", "Thông báo");
          }
        });
      },
      error: (err: any) => {
        console.log(err);
        this.handleError(err);
      }
    });
  }

  create() {
    const productIds = this.selectedProducts.map(p => p.id);
    this.saleForm.get('productIds')?.setValue(productIds);
    console.log(this.saleForm.value);
    this.saleService.save(this.saleForm.value).subscribe({
      next: () => {
        this.toastr.success("Thêm khuyến mãi thành công", "Thông báo");
        this.router.navigateByUrl("/admin/sale");
      },
      error: (err: any) => {
        this.handleError(err);
      }
    });
  }

  update() {
    const productIds = this.selectedProducts.map(p => p.id);
    this.saleForm.get('productIds')?.setValue(productIds);
    console.log(this.saleForm.value);
    this.saleService.save(this.saleForm.value).subscribe({
      next: () => {
        this.toastr.success("Cập nhật khuyến mãi thành công", "Thông báo");
        this.router.navigateByUrl("/admin/sale");
      },
      error: (err: any) => {
        this.handleError(err);
      }
    });
  }


  private handleError(error: any): void {
    console.log(error);
    if (error.status === 400 && error.error === 'DUPLICATE_NAME') {
      this.duplicateName = 'Tên khuyến mãi đã tồn tại!';
    }
    if (error.status === 400 && error.error === 'START_DATE_AND_END_DATE_REQUIRED') {
      this.toastr.error('Ngày bắt đầu và ngày kết thúc không được để trống!', 'Thông báo');
    }
    if (error.status === 400 && error.error === 'START_DATE_MUST_BE_BEFORE_END_DATE') {
      this.errorDate = 'Ngày kết thúc phải lớn hơn ngày bắt đầu!';
    }
    if (error.status === 400 && error.error === 'PRODUCT_ALREADY_IN_SALE') {
      this.toastr.error('Có sản phẩm đã được áp dụng chương trình khuyến mãi!', 'Thông báo');
    }
  }

  removeProduct(id: number) {
    this.selectedProducts = this.selectedProducts.filter(p => p.id !== id);
  }
}

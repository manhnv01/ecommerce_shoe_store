import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Title } from '@angular/platform-browser';
import { ProductService } from 'src/app/service/product.service';
import { ReceiptService } from 'src/app/service/receipt.service';
import { ToastrService } from 'ngx-toastr';
import { ReturnService } from 'src/app/service/return.service';
import { Environment } from 'src/app/environment/environment';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ElementRef, ViewChild } from '@angular/core';


@Component({
  selector: 'app-detail-return',
  templateUrl: './detail-return.component.html',
  styleUrls: ['./detail-return.component.css']
})
export class DetailReturnComponent implements OnInit {
  @ViewChild('btnCloseModal') btnCloseModal!: ElementRef;
  @ViewChild('textAreaReason') textAreaReason!: ElementRef;
  returnProduct: any;
  returnProductDetails: any;
  titleString = '';
  baseUrl = Environment.apiBaseUrl;
  productId: any;

  updateReturnStatusForm: FormGroup = new FormGroup({
    id: new FormControl(null),
    status: new FormControl(null, [Validators.required]),
    reason: new FormControl(null),
  }
);

  constructor(
    private title: Title,
    private productService: ProductService,
    private returnService: ReturnService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private toastr: ToastrService) {
  }

  ngOnInit() {
    this.title.setTitle('Chi tiết phiếu đổi trả');
    this.findById(this.activatedRoute.snapshot.params["id"]);

    this.updateReturnStatusForm.get('status')?.valueChanges.subscribe((status: string) => {
      if (status == 'RETURN_REJECTED') {
        this.updateReturnStatusForm.get('reason')?.setValidators([Validators.required, Validators.maxLength(100)]);
        this.updateReturnStatusForm.get('reason')?.updateValueAndValidity();
        this.textAreaReason.nativeElement.focus();
        this.textAreaReason.nativeElement.classList.remove('d-none');
      } else {
        this.textAreaReason.nativeElement.classList.add('d-none');
        this.updateReturnStatusForm.get('reason')?.reset();
        this.updateReturnStatusForm.get('reason')?.clearValidators();
        this.updateReturnStatusForm.get('reason')?.updateValueAndValidity();
      }
    });
  }

  findById(id: number) {
    this.returnService.findById(id).subscribe({
      next: (data: any) => {
        this.returnProduct = data;
        this.returnProductDetails = data.returnProductDetails;
        console.log(data);
      },
      error: (err: any) => {
        console.log(err);
        this.toastr.error("Không tìm thấy phiếu đổi trả này", "Thông báo");
      }
    });
  }

  resetForm() {
    this.updateReturnStatusForm.reset();
    this.updateReturnStatusForm.patchValue(this.returnProduct);
  }

  updateReturnStatus() {
    this.updateReturnStatusForm.patchValue({ id: this.returnProduct.id });

    console.log(this.updateReturnStatusForm.value);
    this.returnService.updateReturnStatus(this.updateReturnStatusForm.value).subscribe({
      next: (data: any) => {
        this.toastr.success('Xử lý đổi trả thành công', 'Thông báo');
        this.findById(this.returnProduct.id);
        this.btnCloseModal.nativeElement.click();
      },
      error: (error: any) => {
        console.log(error);
        if (error.status == 400 && error.error == 'RETURN_PRODUCT_NOT_FOUND') {
          this.toastr.error('Phiếu đổi trả không tồn tại', 'Thông báo');
        } else if (error.status == 400 && error.error == 'RETURN_PRODUCT_STATUS_CANNOT_BE_CHANGED') {
          this.toastr.error('Phiếu đổi trả đã được xử lý', 'Thông báo');
        } else {
          this.toastr.error('Lỗi không xác định', 'Thông báo');
        }
      }
    })
  }
}

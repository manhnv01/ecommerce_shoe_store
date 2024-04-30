import { Component, OnInit } from '@angular/core';
import { Environment } from 'src/app/environment/environment';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { ProductService } from 'src/app/service/product.service';
import { ReturnService } from 'src/app/service/return.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ElementRef, ViewChild } from '@angular/core';

@Component({
  selector: 'app-user-return-product-detail',
  templateUrl: './user-return-product-detail.component.html',
  styleUrls: ['./user-return-product-detail.component.css']
})
export class UserReturnProductDetailComponent implements OnInit {
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
        this.toastr.error("Không tìm thấy phiếu đổi trả này");
      }
    });
  }
}

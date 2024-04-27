import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Environment } from 'src/app/environment/environment';
import { ProductService } from 'src/app/service/product.service';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-save-return',
  templateUrl: './save-return.component.html',
  styleUrls: ['./save-return.component.css']
})
export class SaveReturnComponent implements OnInit {
  baseUrl: string = `${Environment.apiBaseUrl}`;
  titleString: string = "";
  btnSave: string = "";

  receiptForm: FormGroup = new FormGroup({
    id: new FormControl(null),
    supplierId: new FormControl(null, [Validators.required])
  });

  constructor(
    private title: Title,
    private productService: ProductService,
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
    }
    this.title.setTitle(this.titleString);
  }

  onSubmit() {
    if (this.receiptForm.invalid) {
      console.log("Form invalid");
      return;
    }
    this.create();
  }

  create (){

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
}

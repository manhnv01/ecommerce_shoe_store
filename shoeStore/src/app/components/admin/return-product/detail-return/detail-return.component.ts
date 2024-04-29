import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Title } from '@angular/platform-browser';
import { ProductService } from 'src/app/service/product.service';
import { ReceiptService } from 'src/app/service/receipt.service';
import { ToastrService } from 'ngx-toastr';
import { ReturnService } from 'src/app/service/return.service';
import { Environment } from 'src/app/environment/environment';

@Component({
  selector: 'app-detail-return',
  templateUrl: './detail-return.component.html',
  styleUrls: ['./detail-return.component.css']
})
export class DetailReturnComponent implements OnInit {

  returnProduct: any;
  returnProductDetails: any;
  titleString = '';
  baseUrl = Environment.apiBaseUrl;
  productId: any;

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
    this.findReceiptById(this.activatedRoute.snapshot.params["id"]);
  }

  findReceiptById(id: number) {
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
}

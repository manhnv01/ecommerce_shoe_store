import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductModel } from 'src/app/model/product.model';
import { ProductService } from 'src/app/service/product.service';
import { ReceiptService } from 'src/app/service/receipt.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-detail-receipt',
  templateUrl: './detail-receipt.component.html',
  styleUrls: ['./detail-receipt.component.css']
})
export class DetailReceiptComponent implements OnInit {

  receipt: any;
  receiptDetails: any;

  productId: any;

  constructor(
    private title: Title,
    private productService: ProductService,
    private receiptService: ReceiptService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private toastr: ToastrService) {
  }

  ngOnInit() {
    this.title.setTitle('Chi tiết hóa đơn nhập');
    this.findReceiptById(this.activatedRoute.snapshot.params["id"]);
  }

  findReceiptById(id: number) {
    this.receiptService.findById(id).subscribe({
      next: (data: any) => {
        this.receipt = data;
        this.receiptDetails = data.receiptDetails;
        console.log(data);
      },
      error: (err: any) => {
        console.log(err);
        this.toastr.error("Không tìm thấy hóa đơn nhập này", "Thông báo");
      }
    });
  }
}



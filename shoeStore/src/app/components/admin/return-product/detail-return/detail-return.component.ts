import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Title } from '@angular/platform-browser';
import { ProductService } from 'src/app/service/product.service';
import { ReceiptService } from 'src/app/service/receipt.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-detail-return',
  templateUrl: './detail-return.component.html',
  styleUrls: ['./detail-return.component.css']
})
export class DetailReturnComponent implements OnInit {

  receipt: any;
  receiptDetails: any;
  titleString = '';

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

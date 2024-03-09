import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductService } from 'src/app/service/product.service';
import { SaleService } from 'src/app/service/sale.service';
import { ToastrService } from 'ngx-toastr';
import { ProductModel } from 'src/app/model/product.model';

@Component({
  selector: 'app-detail-sale',
  templateUrl: './detail-sale.component.html',
  styleUrls: ['./detail-sale.component.css']
})
export class DetailSaleComponent implements OnInit {
  sale: any;
  saleId: any;
  selectedProducts: ProductModel[] = [];

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
    this.title.setTitle('Chi tiết khuyến mãi');
    this.saleId = this.activatedRoute.snapshot.params["id"];
    this.findSaleById(this.activatedRoute.snapshot.params["id"]);
  }

  findSaleById(id: number) {
    this.saleService.findById(id).subscribe({
      next: (data: any) => {
        this.sale = data;
        this.selectedProducts = data.products;
        console.log(data);
      },
      error: (err: any) => {
        console.log(err);
        this.toastr.error("Không tìm thấy khuyến mãi này", "Thông báo");
      }
    });
  }
}


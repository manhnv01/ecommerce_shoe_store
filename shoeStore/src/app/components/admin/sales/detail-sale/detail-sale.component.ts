import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductService } from 'src/app/service/product.service';
import { SaleService } from 'src/app/service/sale.service';
import { ToastrService } from 'ngx-toastr';
import { ProductModel } from 'src/app/model/product.model';
import { TokenService } from 'src/app/service/token.service';

@Component({
  selector: 'app-detail-sale',
  templateUrl: './detail-sale.component.html',
  styleUrls: ['./detail-sale.component.css']
})
export class DetailSaleComponent implements OnInit {
  sale: any;
  saleId: any;
  titleString = '';
  isEmployee: boolean = false;
  
  selectedProducts: ProductModel[] = [];

  constructor(
    private title: Title,
    private saleService: SaleService,
    private activatedRoute: ActivatedRoute,
    private tokenService: TokenService,
    private toastr: ToastrService) {
  }

  ngOnInit() {
    this.isEmployee = this.tokenService.isEmployeeLogin();
    this.title.setTitle('Chi tiết khuyến mãi');
    this.saleId = this.activatedRoute.snapshot.params["id"];
    this.findSaleById(this.activatedRoute.snapshot.params["id"]);
  }

  findSaleById(id: number) {
    this.saleService.findById(id).subscribe({
      next: (data: any) => {
        this.sale = data;
        this.selectedProducts = data.products;
        this.title.setTitle(this.sale.name);
        this.titleString = this.sale.name;
        console.log(data);
      },
      error: (err: any) => {
        console.log(err);
        this.toastr.error("Không tìm thấy khuyến mãi này", "Thông báo");
      }
    });
  }
}


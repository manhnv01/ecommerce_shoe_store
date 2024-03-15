import { Component, OnInit } from '@angular/core';
import { DomSanitizer, Title } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';
import { Environment } from 'src/app/environment/environment';
import { ProductModel } from 'src/app/model/product.model';
import { ProductService } from 'src/app/service/product.service';

@Component({
  selector: 'app-detail-product',
  templateUrl: './detail-product.component.html',
  styleUrls: ['./detail-product.component.css']
})
export class DetailProductComponent implements OnInit {
  protected readonly Environment = Environment;
  titleString = 'Chi tiết sản phẩm';
  product: any;

  totalQuantity: number = 0;

  constructor(private productService: ProductService, private title: Title, private activatedRoute: ActivatedRoute,
              private sanitizer: DomSanitizer) {
  }

  ngOnInit(): void {
    this.title.setTitle(this.titleString);
    this.getProductById(this.activatedRoute.snapshot.params["id"]);
  }

  getProductById(id: number) {
    this.productService.findById(id).subscribe({
      next: (data: any) => {
        this.product = data;
      },
      error: (error: any) => {
        console.log(error);
      }
    });
  }

  getHtmlProductDescription() {
    if (this.product?.description == '' || this.product?.description == null) {
      return 'Chưa có thông tin mô tả sản phẩm.';
    }
    return this.sanitizer.bypassSecurityTrustHtml(this.product?.description);
  }
}

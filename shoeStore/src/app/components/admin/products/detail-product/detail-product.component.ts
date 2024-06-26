import { Component, OnInit } from '@angular/core';
import { DomSanitizer, Title } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';
import { Environment } from 'src/app/environment/environment';
import { ProductModel } from 'src/app/model/product.model';
import { ProductService } from 'src/app/service/product.service';
import { TokenService } from 'src/app/service/token.service';

@Component({
  selector: 'app-detail-product',
  templateUrl: './detail-product.component.html',
  styleUrls: ['./detail-product.component.css']
})
export class DetailProductComponent implements OnInit {
  protected readonly Environment = Environment;
  titleString = '';
  product: any;

  isEmployee: boolean = false;

  totalQuantity: number = 0;

  constructor(
    private productService: ProductService, 
    private tokenService: TokenService,
    private title: Title, 
    private activatedRoute: ActivatedRoute,
    private sanitizer: DomSanitizer) {
  }

  ngOnInit(): void {
    this.isEmployee = this.tokenService.isEmployeeLogin();
    this.getProductById(this.activatedRoute.snapshot.params["id"]);
  }

  getProductById(id: number) {
    this.productService.findById(id).subscribe({
      next: (data: any) => {
        this.product = data;
        this.titleString = this.product.name;
        this.title.setTitle(this.titleString);
      },
      error: (error: any) => {
        if (error.status === 400 && error.error === 'PRODUCT_NOT_FOUND') {
          console.log('Không tìm thấy sản phẩm này');
        }
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

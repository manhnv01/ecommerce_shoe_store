import { Component, OnInit } from '@angular/core';
import { DomSanitizer, Title } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';
import { Environment } from 'src/app/environment/environment';
import { ProductService } from 'src/app/service/product.service';

@Component({
  selector: 'app-user-product-detail',
  templateUrl: './user-product-detail.component.html',
  styleUrls: ['./user-product-detail.component.css']
})
export class UserProductDetailComponent implements OnInit {

  protected readonly Environment = Environment;
  titleString = 'Chi tiết sản phẩm';
  product: any;

  constructor(private productService: ProductService, private title: Title, private activatedRoute: ActivatedRoute,
              private sanitizer: DomSanitizer) {
  }

  ngOnInit(): void {
    this.title.setTitle(this.titleString);
    this.findBySlug(this.activatedRoute.snapshot.params["slug"]);
  }

  findBySlug(slug: string) {
    this.productService.findBySlug(slug).subscribe({
      next: (data: any) => {
        this.product = data;
        console.log(this.product);
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
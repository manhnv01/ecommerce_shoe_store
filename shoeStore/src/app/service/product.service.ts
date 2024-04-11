import { Injectable } from '@angular/core';
import { Environment } from '../environment/environment';
import { CategoryModel } from '../model/category.model';
import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Observable } from 'rxjs/internal/Observable';
import { tap } from 'rxjs/internal/operators/tap';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductModel } from '../model/product.model';
import { ProductColorModel } from '../model/product-color.model';
import { ProductDetailsModel } from '../model/product-details.model';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private api = `${Environment.apiBaseUrl}/api/product`;
  private apiConfig = { headers: this.createHeader() }

  constructor(private http: HttpClient) { }

  findAllByEnabledIsTrue(page: number, size: number, sortDir: string, sortBy: string): Observable<any> {
    const params = new HttpParams()
      .set('size', size.toString())
      .set('page', page.toString())
      .set('sort-direction', sortDir)
      .set('sort-by', sortBy);

    return this.http.get(this.api + '/all', { params });
  }

  // Lấy sản phẩm theo brandSlug
  findAllByEnabledIsTrueAnd_Slug(page: number, size: number, sortDir: string, sortBy: string, brandSlug: string): Observable<any> {
    const params = new HttpParams()
      .set('size', size.toString())
      .set('page', page.toString())
      .set('sort-direction', sortDir)
      .set('sort-by', sortBy);

    return this.http.get(`${this.api}/brand/${brandSlug}`, { params });
  }

  findBySlug(slug: string): Observable<any> {
    return this.http.get<ProductModel>(`${this.api}/slug/${slug}`);
  }

  findAll(page: number, size: number, sortDir: string, sortBy: string, search: string, enabled: string, isZeroQuantity: string): Observable<any> {
    const params = new HttpParams()
      .set('search', search)
      .set('size', size.toString())
      .set('page', page.toString())
      .set('sort-direction', sortDir)
      .set('sort-by', sortBy)
      .set('enabled', enabled)
      .set('isZeroQuantity', isZeroQuantity);

    return this.http.get(this.api, { params });
  }


  findAllOption(page: number, size: number, sortDir: string, sortBy: string): Observable<any> {
    const params = new HttpParams()
      .set('size', size.toString())
      .set('page', page.toString())
      .set('sort-direction', sortDir)
      .set('sort-by', sortBy);

    return this.http.get(this.api, { params });
  }

  similarProduct(categoryId: number, brandId: number) {
    const params = new HttpParams()
      .set('categoryId', categoryId.toString())
      .set('brandId', brandId.toString());
    return this.http.get(`${this.api}/similar-product`, { params });
  }

  newest(): Observable<any> {
    return this.http.get(this.api + '/newest');
  }

  create(product: ProductModel, thumbnailFile: File, imageProductFiles: File[]) {
    const formData = new FormData();
    formData.append('name', product.name);
    formData.append('file', thumbnailFile);
    formData.append('slug', product.slug);
    formData.append('description', product.description);
    formData.append('price', product.price.toString());
    formData.append('categoryId', product.categoryId.toString());
    formData.append('brandId', product.brandId.toString());
    formData.append('enabled', product.enabled.toString());
    for (let i = 0; i < product.productColors.length; i++) {
      formData.append(`productColors[${i}].color`, product.productColors[i].color);
    }
    for (let i = 0; i < imageProductFiles.length; i++) {
      formData.append('files', imageProductFiles[i]);
    }
    // debugger;
    console.log(product);
    return this.http.post(this.api, formData);
  }

  update(product: ProductModel, thumbnailFile: File, imageProductFiles: File[]) {
    const formData = new FormData();
    formData.append('id', product.id.toString());
    formData.append('name', product.name);
    formData.append('file', thumbnailFile);
    formData.append('slug', product.slug);
    formData.append('description', product.description);
    formData.append('price', product.price.toString());
    formData.append('categoryId', product.categoryId.toString());
    formData.append('brandId', product.brandId.toString());
    formData.append('enabled', product.enabled.toString());
    for (let i = 0; i < product.productColors.length; i++) {
      if (product.productColors[i].id !== null) {
        formData.append(`productColors[${i}].id`, product.productColors[i].id.toString());
      }
      formData.append(`productColors[${i}].color`, product.productColors[i].color);
    }
    for (let i = 0; i < imageProductFiles.length; i++) {
      formData.append('files', imageProductFiles[i]);
    }
    console.log("formdata", formData);
    return this.http.put(this.api, formData);
  }


  findById(id: number): Observable<ProductModel> {
    return this.http.get<ProductModel>(`${this.api}/${id}`);
  }

  getAllNonPage(): Observable<any> {
    return this.http.get<any>(`${this.api}/find-all`);
  }

  findProductDetailsById(id: number): Observable<ProductDetailsModel> {
    return this.http.get<ProductDetailsModel>(`${this.api}/product-details/${id}`);
  }


  getTotals(): Observable<any> {
    return this.http.get<any>(this.api + `/totals`);
  }

  changeSwitch(id: number) {
    return this.http.put(`${this.api}/switch/${id}`, null, this.apiConfig);
  };

  updatesStatus(ids: number[], enabled: boolean) {
    const url = `${this.api}/${ids.join(',')}`;
    const options = {
      headers: this.apiConfig.headers,
      params: { enabled: enabled.toString() } // Thêm tham số enabled vào phần params
    };

    return this.http.put(url, null, options);
  }

  deleteProductColor(id: number) {
    return this.http.delete(`${this.api}/colors/${id}`);
  }

  deletes(ids: number[]) {
    const url = `${this.api}/${ids.join(',')}`;
    const options = {
      headers: this.apiConfig.headers,
      body: ids
    };

    return this.http.delete(url, options);
  }

  deleteImage(id: number, imageName: string) {
    return this.http.delete(`${this.api}/${id}/images/${imageName}`);
  }

  private createHeader() {
    return new HttpHeaders({ 'Content-Type': 'application/json' });
  }

  delete(id: number) {
    return this.http.delete(`${this.api}/${id}`,);
  }

  // lọc 
  filter(pageSize: number, pageNumber: any, brands: string[], categories: any[],
    priceMin: number, priceMax: number) {
    let queryParams = new HttpParams();
    queryParams = queryParams.append("size", pageSize);
    queryParams = queryParams.append("page", pageNumber);

    brands.forEach((originName) => {
      queryParams = queryParams.append("brands", originName);
    });
    categories.forEach((brandName) => {
      queryParams = queryParams.append("categories", brandName);
    });

    queryParams = queryParams.append("price-min", priceMin);
    queryParams = queryParams.append("price-max", priceMax);
    return this.http.get(`${this.api}/filter/`, { params: queryParams });
  }

}

import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AdminLayoutComponent } from './components/admin/admin-layout/admin-layout.component';
import { DashboardComponent } from './components/admin/dashboard/dashboard.component';
import { FooterAdminComponent } from './components/admin/footer-admin/footer-admin.component';
import { FooterComponent } from './components/site/footer/footer.component';
import { CartComponent } from './components/site/cart/cart.component';
import { HomeComponent } from './components/site/home/home.component';
import { HeaderComponent } from './components/site/header/header.component';
import { UserLayoutComponent } from './components/site/user-layout/user-layout.component';
import { CategoryComponent } from './components/admin/category/category.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { FormsModule, NG_VALUE_ACCESSOR, ReactiveFormsModule } from '@angular/forms';
import { LoginComponent } from './components/account/login/login.component';
import { RegisterComponent } from './components/site/register/register.component';
import { PageNotFoundComponent } from './components/errors/page-not-found/page-not-found.component';
import { ForbiddenComponent } from './components/errors/forbidden/forbidden.component';
import { InternalServerErrorComponent } from './components/errors/internal-server-error/internal-server-error.component';
import { ToastrModule } from 'ngx-toastr';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { BrowserModule } from '@angular/platform-browser';
import { VerifyComponent } from './components/account/verify/verify.component';
import { CodeInputModule } from 'angular-code-input';
import { ForgotPasswordComponent } from './components/account/forgot-password/forgot-password.component';
import { ResetPasswordComponent } from './components/account/reset-password/reset-password.component';
import { Environment } from './environment/environment';
import { JwtModule } from '@auth0/angular-jwt';
import { SaveProductComponent } from './components/admin/products/save-product/save-product.component';
import { ListProductComponent } from './components/admin/products/list-product/list-product.component';
import {NgxDropzoneModule} from "ngx-dropzone";
import { NgSelectModule } from '@ng-select/ng-select';
import {EditorModule, TINYMCE_SCRIPT_SRC} from "@tinymce/tinymce-angular";
import { TagInputModule } from 'ngx-chips';
import { BrandComponent } from './components/admin/brand/brand.component';
import { DetailProductComponent } from './components/admin/products/detail-product/detail-product.component';
import { SupplierComponent } from './components/admin/supplier/supplier.component';
import { SaveSaleComponent } from './components/admin/sales/save-sale/save-sale.component';
import { ListSaleComponent } from './components/admin/sales/list-sale/list-sale.component';
import { DetailSaleComponent } from './components/admin/sales/detail-sale/detail-sale.component';
import { DatePipe } from '@angular/common';
import { ListReceiptComponent } from './components/admin/receipts/list-receipt/list-receipt.component';
import { SaveReceiptComponent } from './components/admin/receipts/save-receipt/save-receipt.component';
import { DetailReceiptComponent } from './components/admin/receipts/detail-receipt/detail-receipt.component';
import { ListEmployeeComponent } from './components/admin/employees/list-employee/list-employee.component';
import { SaveEmployeeComponent } from './components/admin/employees/save-employee/save-employee.component';
import { DetailEmployeeComponent } from './components/admin/employees/detail-employee/detail-employee.component';
import { UserProductComponent } from './components/site/user-product/user-product.component';
import { UserProductDetailComponent } from './components/site/user-product-detail/user-product-detail.component';
import { AbbreviationPipe } from './pipe/abbreviation.pipe';
import { CurrencyFormatPipe } from './pipe/currency-format.pipe';
import { NgxImageZoomModule } from 'ngx-image-zoom';
import { TokenInterceptor } from './interceptors/token.interceptor';
import { CheckOutComponent } from './components/site/user-orders/check-out/check-out.component';
import { SwiperDirective } from './directive/swiper.directive';
import { register } from 'swiper/element/bundle';
import { ProfileComponent } from './components/site/profile/profile.component';
import { ListCustomerComponent } from './components/admin/customers/list-customer/list-customer.component';
import { DetailCustomerComponent } from './components/admin/customers/detail-customer/detail-customer.component';
import { UserOrderDetailComponent } from './components/site/user-orders/user-order-detail/user-order-detail.component';
import { ListOrderComponent } from './components/admin/orders/list-order/list-order.component';
import { CreateOrderComponent } from './components/admin/orders/create-order/create-order.component';
import { DetailOrderComponent } from './components/admin/orders/detail-order/detail-order.component';
import {MatSliderModule} from '@angular/material/slider';
import { OrderNotificationComponent } from './components/site/user-orders/order-notification/order-notification.component';
import { UserOrderComponent } from './components/site/user-orders/user-order/user-order.component';
import { ListReturnComponent } from './components/admin/return-product/list-return/list-return.component';
import { SaveReturnComponent } from './components/admin/return-product/save-return/save-return.component';
import { DetailReturnComponent } from './components/admin/return-product/detail-return/detail-return.component';
import { UserReturnProductDetailComponent } from './components/site/user-orders/user-return-product-detail/user-return-product-detail.component';
import { ReportOrderComponent } from './components/admin/reports/report-order/report-order.component';
import { ReportReceiptComponent } from './components/admin/reports/report-receipt/report-receipt.component';

register();

@NgModule({
  declarations: [
    AppComponent,
    AdminLayoutComponent,
    DashboardComponent,
    FooterAdminComponent,
    CategoryComponent,
    BrandComponent,
    UserLayoutComponent,
    HeaderComponent,
    HomeComponent,
    CartComponent,
    FooterComponent,
    LoginComponent,
    RegisterComponent,
    PageNotFoundComponent,
    ForbiddenComponent,
    InternalServerErrorComponent,
    VerifyComponent,
    ResetPasswordComponent,
    ForgotPasswordComponent,
    SaveProductComponent,
    ListProductComponent,
    DetailProductComponent,
    SupplierComponent,
    SaveSaleComponent,
    ListSaleComponent,
    DetailSaleComponent,
    ListReceiptComponent,
    SaveReceiptComponent,
    DetailReceiptComponent,
    ListEmployeeComponent,
    SaveEmployeeComponent,
    DetailEmployeeComponent,
    UserProductComponent,
    UserProductDetailComponent,
    CheckOutComponent,
    ListCustomerComponent,
    DetailCustomerComponent,
    UserOrderComponent,
    UserOrderDetailComponent,
    OrderNotificationComponent,
    AbbreviationPipe,
    ProfileComponent,
    CurrencyFormatPipe,
    ListOrderComponent,
    CreateOrderComponent,
    DetailOrderComponent,
    ListReturnComponent,
    SaveReturnComponent,
    DetailReturnComponent,
    UserReturnProductDetailComponent,
    ReportOrderComponent,
    ReportReceiptComponent,
    SwiperDirective
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
    RouterModule,
    TagInputModule,
    NgxDropzoneModule,
    NgSelectModule,
    EditorModule,
    MatSliderModule,
    NgxImageZoomModule,
    ToastrModule.forRoot({
      timeOut: 3000,  // Thời gian hiển thị của thông báo (đơn vị là miligiây)
      positionClass: 'toast-top-right',  // Vị trí của thông báo trên màn hình
      //preventDuplicates: true,  // Ngăn chặn hiển thị các thông báo trùng lặp
      tapToDismiss: true,  // Cho phép bấm vào thông báo để đóng nó
      closeButton: true,  // Hiển thị nút đóng
      extendedTimeOut: 1000,  // Thời gian mở rộng cho thông báo khi di chuyển chuột qua (đơn vị là miligiây)
    }),
    CodeInputModule.forRoot({
      codeLength: 6,
      isCharsCode: false
    }),
    JwtModule.forRoot({
      config: {
        tokenGetter: () => {
          return localStorage.getItem('token');
        },
        allowedDomains: [`${Environment.apiBaseUrl}`], // Các đường dẫn sử dụng token
        disallowedRoutes: [`${Environment.apiBaseUrl}/login`] // Các đường dẫn không sử dụng token
      }
    }),
    BrowserAnimationsModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true
    },
    {
      provide: TINYMCE_SCRIPT_SRC,
      useValue: 'tinymce/tinymce.min.js'
    },
    DatePipe
  ],
  bootstrap: [AppComponent],
  schemas: [
    CUSTOM_ELEMENTS_SCHEMA
  ]
})
export class AppModule { }

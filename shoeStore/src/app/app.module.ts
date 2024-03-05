import { NgModule, forwardRef } from '@angular/core';

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
import { HttpClientModule } from '@angular/common/http';
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
    SupplierComponent
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
      provide: TINYMCE_SCRIPT_SRC,
      useValue: 'tinymce/tinymce.min.js'
    },
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

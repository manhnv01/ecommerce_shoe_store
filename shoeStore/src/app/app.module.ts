import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AdminLayoutComponent } from './components/admin/admin-layout/admin-layout.component';
import { DashboardComponent } from './components/admin/dashboard/dashboard.component';
import { FooterAdminComponent } from './components/admin/footer-admin/footer-admin.component';
import { AddEditProductComponent } from './components/admin/add-edit-product/add-edit-product.component';
import { FooterComponent } from './components/site/footer/footer.component';
import { CartComponent } from './components/site/cart/cart.component';
import { HomeComponent } from './components/site/home/home.component';
import { HeaderComponent } from './components/site/header/header.component';
import { UserLayoutComponent } from './components/site/user-layout/user-layout.component';
import { ProductComponent } from './components/admin/product/product.component';
import { CategoryComponent } from './components/admin/category/category.component';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/site/register/register.component';
import { PageNotFoundComponent } from './components/errors/page-not-found/page-not-found.component';
import { ForbiddenComponent } from './components/errors/forbidden/forbidden.component';
import { InternalServerErrorComponent } from './components/errors/internal-server-error/internal-server-error.component';
import { ToastrModule } from 'ngx-toastr';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { BrowserModule } from '@angular/platform-browser';
import { AlphanumericDirective } from './service/slugify.directive';
import { VerifyComponent } from './components/verify/verify.component';
import { VerifyNotifyComponent } from './components/verify-notify/verify-notify.component';

@NgModule({
  declarations: [
    AppComponent,
    AlphanumericDirective,
    AdminLayoutComponent,
    DashboardComponent,
    FooterAdminComponent,
    CategoryComponent,
    AddEditProductComponent,
    ProductComponent,
    UserLayoutComponent,
    HeaderComponent,
    HomeComponent,
    CartComponent,
    FooterComponent,
    AddEditProductComponent,
    LoginComponent,
    RegisterComponent,
    PageNotFoundComponent,
    ForbiddenComponent,
    InternalServerErrorComponent,
    VerifyComponent,
    VerifyNotifyComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
    RouterModule,
    ToastrModule.forRoot({
      timeOut: 3000,  // Thời gian hiển thị của thông báo (đơn vị là miligiây)
      positionClass: 'toast-top-right',  // Vị trí của thông báo trên màn hình
      //preventDuplicates: true,  // Ngăn chặn hiển thị các thông báo trùng lặp
      tapToDismiss: true,  // Cho phép bấm vào thông báo để đóng nó
      closeButton: true,  // Hiển thị nút đóng
      extendedTimeOut: 1000,  // Thời gian mở rộng cho thông báo khi di chuyển chuột qua (đơn vị là miligiây)
    }),
    BrowserAnimationsModule
  ],
  providers: [
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminLayoutComponent } from './components/admin/admin-layout/admin-layout.component';
import { DashboardComponent } from './components/admin/dashboard/dashboard.component';
import { CategoryComponent } from './components/admin/category/category.component';
import { UserLayoutComponent } from './components/site/user-layout/user-layout.component';
import { HomeComponent } from './components/site/home/home.component';
import { CartComponent } from './components/site/cart/cart.component';
import { LoginComponent } from './components/account/login/login.component';
import { RegisterComponent } from './components/site/register/register.component';
import { PageNotFoundComponent } from './components/errors/page-not-found/page-not-found.component';
import { VerifyComponent } from './components/account/verify/verify.component';
import { ForgotPasswordComponent } from './components/account/forgot-password/forgot-password.component';
import { ResetPasswordComponent } from './components/account/reset-password/reset-password.component';
import { ForbiddenComponent } from './components/errors/forbidden/forbidden.component';
import { InternalServerErrorComponent } from './components/errors/internal-server-error/internal-server-error.component';
import { ListProductComponent } from './components/admin/products/list-product/list-product.component';
import { SaveProductComponent } from './components/admin/products/save-product/save-product.component';
import { BrandComponent } from './components/admin/brand/brand.component';
import { DetailProductComponent } from './components/admin/products/detail-product/detail-product.component';
import { SupplierComponent } from './components/admin/supplier/supplier.component';
import { ListSaleComponent } from './components/admin/sales/list-sale/list-sale.component';
import { SaveSaleComponent } from './components/admin/sales/save-sale/save-sale.component';
import { DetailSaleComponent } from './components/admin/sales/detail-sale/detail-sale.component';
import { ListReceiptComponent } from './components/admin/receipts/list-receipt/list-receipt.component';
import { SaveReceiptComponent } from './components/admin/receipts/save-receipt/save-receipt.component';
import { DetailReceiptComponent } from './components/admin/receipts/detail-receipt/detail-receipt.component';
import { ListEmployeeComponent } from './components/admin/employees/list-employee/list-employee.component';
import { SaveEmployeeComponent } from './components/admin/employees/save-employee/save-employee.component';
import { DetailEmployeeComponent } from './components/admin/employees/detail-employee/detail-employee.component';
import { UserProductComponent } from './components/site/user-product/user-product.component';
import { UserProductDetailComponent } from './components/site/user-product-detail/user-product-detail.component';
import { AdminGuard } from './guard/admin.guard';
import { CheckOutComponent } from './components/site/check-out/check-out.component';
import { ProfileComponent } from './components/site/profile/profile.component';
import { ListCustomerComponent } from './components/admin/customers/list-customer/list-customer.component';
import { DetailCustomerComponent } from './components/admin/customers/detail-customer/detail-customer.component';

const routes: Routes = [

  // Admin
  {
    path: 'admin',
    component: AdminLayoutComponent,
    children: [
      { path: '', redirectTo: '', pathMatch: 'full' },
      { path: '', component: DashboardComponent },
      { path: 'category', component: CategoryComponent},
      { path: 'brand', component: BrandComponent},
      { path: 'supplier', component: SupplierComponent},

      // Product
      {path: 'product', component: ListProductComponent},
      {path: 'product/save', component: SaveProductComponent},
      {path: 'product/save/:id', component: SaveProductComponent},
      {path: 'product/:id', component: DetailProductComponent},

      // Sale
      {path: 'sale', component: ListSaleComponent},
      {path: 'sale/save', component: SaveSaleComponent},
      {path: 'sale/save/:id', component: SaveSaleComponent},
      {path: 'sale/:id', component: DetailSaleComponent},

      // Receipt
      {path: 'receipt', component: ListReceiptComponent},
      {path: 'receipt/save', component: SaveReceiptComponent},
      {path: 'receipt/:id', component: DetailReceiptComponent},

      // Employee
      {path: 'employee', component: ListEmployeeComponent},
      {path: 'employee/save', component: SaveEmployeeComponent},
      {path: 'employee/save/:id', component: SaveEmployeeComponent},
      {path: 'employee/:id', component: DetailEmployeeComponent},

      // Customer
      {path: 'customer', component: ListCustomerComponent},
      {path: 'customer/:id', component: DetailCustomerComponent}
    ],
    canActivate: [AdminGuard], // Thêm guard vào đây
  },

  // Site
  {
    path: '',
    component: UserLayoutComponent,
    children: [
      { path: '', redirectTo: '', pathMatch: 'full' },
      { path: '', component: HomeComponent },
      { path: 'cart', component: CartComponent },
      { path: 'product', component: UserProductComponent },
      { path: 'product/:slug', component: UserProductDetailComponent },
      { path: 'check-out', component: CheckOutComponent},
      { path: 'profile', component: ProfileComponent}
    ]
  },

  // Auth
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'verify', component: VerifyComponent },
  { path: 'forgot-password', component: ForgotPasswordComponent },
  { path: 'reset-password', component: ResetPasswordComponent },
  { path: 'forbidden', component: ForbiddenComponent },
  { path: 'server-error', component: InternalServerErrorComponent },

  // Route wildcard (nếu route không hợp lệ)
  { path: '**', component: PageNotFoundComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

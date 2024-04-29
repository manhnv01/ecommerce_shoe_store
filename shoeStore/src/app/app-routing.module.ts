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
import { CheckOutComponent } from './components/site/user-orders/check-out/check-out.component';
import { ProfileComponent } from './components/site/profile/profile.component';
import { ListCustomerComponent } from './components/admin/customers/list-customer/list-customer.component';
import { DetailCustomerComponent } from './components/admin/customers/detail-customer/detail-customer.component';
import { UserOrderDetailComponent } from './components/site/user-orders/user-order-detail/user-order-detail.component';
import { ListOrderComponent } from './components/admin/orders/list-order/list-order.component';
import { DetailOrderComponent } from './components/admin/orders/detail-order/detail-order.component';
import { CreateOrderComponent } from './components/admin/orders/create-order/create-order.component';
import { OrderNotificationComponent } from './components/site/user-orders/order-notification/order-notification.component';
import { UserGuard } from './guard/user.guard';
import { UserOrderComponent } from './components/site/user-orders/user-order/user-order.component';
import { EmployeeGuard } from './guard/employee.guard';
import { AdminOrEmployeeGuard } from './guard/admin-or-employee.guard';
import { ListReturnComponent } from './components/admin/return-product/list-return/list-return.component';
import { SaveReturnComponent } from './components/admin/return-product/save-return/save-return.component';
import { DetailReturnComponent } from './components/admin/return-product/detail-return/detail-return.component';

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
      { path: 'supplier', component: SupplierComponent, canActivate: [AdminGuard]},

      // Product
      {path: 'product', component: ListProductComponent},
      {path: 'product/save', component: SaveProductComponent, canActivate: [AdminGuard]},
      {path: 'product/save/:id', component: SaveProductComponent, canActivate: [AdminGuard]},
      {path: 'product/:id', component: DetailProductComponent},

      // Sale
      {path: 'sale', component: ListSaleComponent},
      {path: 'sale/save', component: SaveSaleComponent, canActivate: [AdminGuard]},
      {path: 'sale/save/:id', component: SaveSaleComponent, canActivate: [AdminGuard]},
      {path: 'sale/:id', component: DetailSaleComponent},

      // Receipt
      {path: 'receipt', component: ListReceiptComponent},
      {path: 'receipt/save', component: SaveReceiptComponent},
      {path: 'receipt/:id', component: DetailReceiptComponent},

      // Employee
      {path: 'employee', component: ListEmployeeComponent, canActivate: [AdminGuard]},
      {path: 'employee/save', component: SaveEmployeeComponent, canActivate: [AdminGuard]},
      {path: 'employee/save/:id', component: SaveEmployeeComponent, canActivate: [AdminGuard]},
      {path: 'employee/:id', component: DetailEmployeeComponent},
      {path: 'profile/:id', component: DetailEmployeeComponent},

      // Customer
      {path: 'customer', component: ListCustomerComponent},
      {path: 'customer/:id', component: DetailCustomerComponent},

      // Order
      {path: 'order', component: ListOrderComponent},
      {path: 'order/create', component: CreateOrderComponent},
      {path: 'order/:id', component: DetailOrderComponent},

      // Return Product
      {path: 'return-product', component: ListReturnComponent},
      {path: 'return-product/save', component: SaveReturnComponent},
      {path: 'return-product/:id', component: DetailReturnComponent},
    ],
    // thêm 2 guard vào đây là AdminGuard và EmployeeGuard
    canActivate: [AdminOrEmployeeGuard]
  },

  // Site
  {
    path: '',
    component: UserLayoutComponent,
    children: [
      { path: '', redirectTo: '', pathMatch: 'full' },
      { path: '', component: HomeComponent },
      { path: 'cart', component: CartComponent, canActivate: [UserGuard]},
      { path: 'product', component: UserProductComponent },
      { path: 'product/:slug', component: UserProductDetailComponent },
      { path: 'check-out', component: CheckOutComponent, canActivate: [UserGuard]},
      { path: 'profile', component: ProfileComponent, canActivate: [UserGuard]},
      { path: 'order', component: UserOrderComponent, canActivate: [UserGuard]},
      { path: 'order/:id', component: UserOrderDetailComponent, canActivate: [UserGuard]},

      // Các route thông báo khi đặt hàng
      { path: 'order/order-success/:id', component: OrderNotificationComponent, canActivate: [UserGuard]},
      { path: 'order/payment-success/:id', component: OrderNotificationComponent, canActivate: [UserGuard]},
      { path: 'order/payment-failure/:id', component: OrderNotificationComponent, canActivate: [UserGuard]},
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

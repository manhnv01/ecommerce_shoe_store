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

const routes: Routes = [
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
    ]
  },
  {
    path: '',
    component: UserLayoutComponent,
    children: [
      { path: '', redirectTo: '', pathMatch: 'full' },
      { path: '', component: HomeComponent },
      { path: 'cart', component: CartComponent },
    ]
  },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'verify', component: VerifyComponent },
  { path: 'forgot-password', component: ForgotPasswordComponent },
  { path: 'reset-password', component: ResetPasswordComponent },

  // Route wildcard (nếu route không hợp lệ)
  { path: '**', component: PageNotFoundComponent },
  { path: 'forbidden', component: ForbiddenComponent },
  { path: 'server-error', component: InternalServerErrorComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

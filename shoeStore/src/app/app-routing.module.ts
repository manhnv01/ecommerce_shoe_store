import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminLayoutComponent } from './components/admin/admin-layout/admin-layout.component';
import { DashboardComponent } from './components/admin/dashboard/dashboard.component';
import { CategoryComponent } from './components/admin/category/category.component';
import { ProductComponent } from './components/admin/product/product.component';
import { AddEditProductComponent } from './components/admin/add-edit-product/add-edit-product.component';
import { UserLayoutComponent } from './components/site/user-layout/user-layout.component';
import { HomeComponent } from './components/site/home/home.component';
import { CartComponent } from './components/site/cart/cart.component';
import { LoginComponent } from './components/account/login/login.component';
import { RegisterComponent } from './components/site/register/register.component';
import { PageNotFoundComponent } from './components/errors/page-not-found/page-not-found.component';
import { VerifyComponent } from './components/account/verify/verify.component';
import { ForgotPasswordComponent } from './components/account/forgot-password/forgot-password.component';
import { ResetPasswordComponent } from './components/account/reset-password/reset-password.component';

const routes: Routes = [
  {
    path: 'admin',
    component: AdminLayoutComponent,
    data: { breadcrumb: 'admin' },
    children: [
      { path: '', redirectTo: '', pathMatch: 'full' },
      { path: '', component: DashboardComponent },
      { path: 'category', component: CategoryComponent, data: { breadcrumb: 'category' } },
      { path: 'product', component: ProductComponent },
      { path: 'product/add', component: AddEditProductComponent },
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
  { path: '**', component: PageNotFoundComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

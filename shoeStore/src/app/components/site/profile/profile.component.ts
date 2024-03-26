import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { AccountService } from 'src/app/service/account.service';
import { CustomerService } from 'src/app/service/customer.service';
import { Title } from '@angular/platform-browser';
import { ToastrService } from 'ngx-toastr';
import { TokenService } from 'src/app/service/token.service';
import { Environment } from 'src/app/environment/environment';
import { FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  @ViewChild('oldPassword') oldPassword!: ElementRef;
  @ViewChild('newPassword') newPassword!: ElementRef;
  @ViewChild('btnCloseModal') btnCloseModal!: ElementRef;

  cities: any;
  districts: any;
  wards: any;

  isLogin: boolean = false;
  isTokenExpired: boolean = true;

  email: string = '';

  profile: any;

  show: boolean = true;

  baseUrl: string = `${Environment.apiBaseUrl}`;

  constructor(
    private accountService: AccountService,
    private title: Title,
    private tokenService: TokenService,
    private customerService: CustomerService,
    private toastr: ToastrService,
  ) { }

  profileForm: FormGroup = new FormGroup({
    id: new FormControl(null),
    name: new FormControl('', [Validators.required, Validators.minLength(2), Validators.maxLength(30)]),
    gender: new FormControl(null),
    phone: new FormControl('', [Validators.required, Validators.pattern('^[0-9]{10}$')]),
    city: new FormControl(null, [Validators.required]),
    district: new FormControl(null, [Validators.required]),
    ward: new FormControl(null, [Validators.required]),
    addressDetail: new FormControl('', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]),
    birthday: new FormControl(''),
  });

  changePasswordForm: FormGroup = new FormGroup({
    id: new FormControl(null),
    oldPassword: new FormControl('', [Validators.required]),
    newPassword: new FormControl('', [Validators.required, Validators.minLength(6), Validators.maxLength(30)]),
  });

  ngOnInit() {
    this.title.setTitle('Cá nhân');
    this.getJsonDataAddress();

    if (this.tokenService.getToken() !== null) {
      this.isLogin = true;
      this.isTokenExpired = this.tokenService.isTokenExpired();

      if (!this.isTokenExpired) {
        this.email = this.tokenService.getUserName();
      }

      if (this.isLogin && !this.isTokenExpired && this.tokenService.getUserRoles().includes('ROLE_USER')) {
        this.getProfile();
        this.profileForm.patchValue(this.profile);
        console.log(this.profileForm.value);
      }
    }
  }

  onSubmit() {
    this.profileForm.get('id')?.setValue(this.profile.id);
    this.customerService.updateProfile(this.profileForm.value).subscribe({
      next: (response) => {
        console.log(response);
        this.toastr.success('Cập nhật thông tin thành công');
      },
      error: (error) => {
        console.log(error);
        this.toastr.error('Cập nhật thông tin thất bại');
      }
    });
  }

  onChangePassword() {
    this.changePasswordForm.get('id')?.setValue(this.profile.account.id);
    this.accountService.changePassword(this.changePasswordForm.value).subscribe({
      next: (response) => {
        console.log(response);
        this.toastr.success('Đổi mật khẩu thành công');
        this.resetText();
        this.btnCloseModal.nativeElement.click();
      },
      error: (error) => {
        console.log(error);
        if (error.status === 400 && error.error.message === 'INVALID_PASSWORD') {
          this.toastr.error('Mật khẩu cũ không đúng');
        } else if (error.status === 400 && error.error.message === 'ACCOUNT_NOT_FOUND') {
          this.toastr.error('Tài khoản không tồn tại');
        } else
          this.toastr.error('Đổi mật khẩu thất bại');
      }
    });
  }

  togglePassword(){
    this.show = !this.show;
    if(this.show){
      this.oldPassword.nativeElement.setAttribute('type', 'password');
      this.newPassword.nativeElement.setAttribute('type', 'password');
    }
    else {
      this.oldPassword.nativeElement.setAttribute('type', 'text');
      this.newPassword.nativeElement.setAttribute('type', 'text');
    }
  }

  resetText() {
    this.changePasswordForm.reset();
  }

  getJsonDataAddress() {
    this.accountService.getJsonDataAddress().subscribe({
      next: (response) => {
        console.log(response);
        this.cities = response;
      },
      error: (error) => {
        console.log(error);
      }
    });
  }

  getProfile() {
    this.customerService.findByEmail(this.tokenService.getUserName()).subscribe({
      next: (response) => {
        // console.log(response);
        this.profile = response;
      },
      error: (error) => {
        console.log(error);
      }
    });
  }

  getDistrictsControl(): FormControl {
    const cityControl = this.profileForm.get('city') as FormControl;
    cityControl.valueChanges.pipe().subscribe((id: any) => {
      this.cities.forEach((city: any) => {
        if (city.name === id) {
          this.districts = city.districts;
          this.profileForm.get('district')?.setValue(this.districts[0]?.name); // Đảm bảo mảng districts không rỗng trước khi gán giá trị
        }
      });
    });
    return cityControl;
  }

  getWardsControl(): FormControl {
    const districtControl = this.profileForm.get('district') as FormControl;
    districtControl.valueChanges.pipe().subscribe((name: any) => {
      this.districts.forEach((district: any) => {
        if (district.name === name) {
          this.wards = district.wards;
          this.profileForm.get('ward')?.setValue(this.wards[0]?.name); // Đảm bảo mảng wards không rỗng trước khi gán giá trị
        }
      });
    });
    return districtControl;
  }
}

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

  @ViewChild('btnCloseModal') btnCloseModal!: ElementRef;
  show: boolean = true;

  cities: any;
  districts: any;
  wards: any;

  isLogin: boolean = false;
  isTokenExpired: boolean = true;

  email: string = '';

  profile: any;

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
    phone: new FormControl('', [Validators.required, Validators.pattern('^(0)[0-9]{9}$')]),
    city: new FormControl(null, [Validators.required]),
    district: new FormControl(null, [Validators.required]),
    ward: new FormControl(null, [Validators.required]),
    addressDetail: new FormControl('', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]),
  });

  ngOnInit() {
    this.title.setTitle('Cá nhân');
    this.getJsonDataAddress();

    if (this.tokenService.isUserLogin()) {
      // cần bổ sung
    }

    if (this.tokenService.getToken() !== null) {
      this.isLogin = true;
      this.isTokenExpired = this.tokenService.isTokenExpired();

      if (!this.isTokenExpired) {
        this.email = this.tokenService.getUserName();
      }

      if (this.isLogin && !this.isTokenExpired && this.tokenService.getUserRoles().includes('ROLE_USER')) {
        this.getProfile();
        console.log(this.profileForm.value);
      }
    }
  }

  resetText() {
    this.profileForm.reset();
  }

  onSubmit() {
    this.profileForm.get('id')?.setValue(this.profile.id);
    this.customerService.updateProfile(this.profileForm.value).subscribe({
      next: (response) => {
        console.log(response);
        this.toastr.success('Cập nhật thông tin thành công');
        this.getProfile();
        this.btnCloseModal.nativeElement.click();
      },
      error: (error) => {
        console.log(error);
        if (error.status === 400 && error.error === 'DUPLICATE_PHONE') {
          this.toastr.error('Số điện thoại đã được sử dụng');
        }
        else
          this.toastr.error('Cập nhật thông tin thất bại');
      }
    });
  }

  openModal() {
    this.profileForm.patchValue(this.profile);
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

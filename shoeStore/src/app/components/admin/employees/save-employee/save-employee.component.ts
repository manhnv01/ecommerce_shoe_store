import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Environment } from 'src/app/environment/environment';
import { Title } from '@angular/platform-browser';
import { AccountService } from 'src/app/service/account.service';
import { EmployeeService } from 'src/app/service/employee.service';
import { ToastrService } from 'ngx-toastr';
import { ActivatedRoute, Router } from '@angular/router';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-save-employee',
  templateUrl: './save-employee.component.html',
  styleUrls: ['./save-employee.component.css']
})
export class SaveEmployeeComponent implements OnInit {
  protected readonly Environment = Environment;

  cities: any;
  districts: any;
  wards: any;

  duplicateEmail: string = "";

  titleString: string = "";

  maxFileSize: number = 10 * 1024 * 1024;

  selectedImageUrl: string = "";
  selectedImageFile: File = new File([""], "filename");

  btnSave: string = "";

  employeeForm: FormGroup = new FormGroup({
    id: new FormControl(null),
    name: new FormControl('', [Validators.required, Validators.maxLength(30)]),
    avatarFile: new FormControl(''),
    email: new FormControl('', [Validators.required, Validators.email]),
    phone: new FormControl('', [Validators.required, Validators.pattern('^[0-9]{10}$')]),
    gender: new FormControl(null, [Validators.required]),
    birthday: new FormControl('', [Validators.required]),
    city: new FormControl(null, [Validators.required]),
    district: new FormControl(null, [Validators.required]),
    ward: new FormControl(null, [Validators.required]),
    addressDetail: new FormControl('', [Validators.required]),
    status: new FormControl(null, [Validators.required]),
  }
  );

  constructor(
    private title: Title,
    private datePipe: DatePipe,
    private accountService: AccountService,
    private employeeService: EmployeeService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private toastr: ToastrService) {
  }

  ngOnInit() {
    const avatarControl = this.employeeForm.get('avatarFile');
    if (this.activatedRoute.snapshot.params["id"] === undefined) {
      this.btnSave = "Thêm mới";
      this.titleString = "Thêm nhân viên mới";
      avatarControl?.setValidators([Validators.required]);
    } else {
      this.titleString = "Cập nhật thông tin nhân viên";
      this.btnSave = "Cập nhật";
      avatarControl?.setValidators([Validators.nullValidator]);
      this.findById(this.activatedRoute.snapshot.params["id"]);
    }
    avatarControl?.updateValueAndValidity();
    this.title.setTitle(this.titleString);
    this.getJsonDataAddress();
  }

  onSubmit() {
    if (this.employeeForm.invalid) {
      console.log("Form invalid");
      return;
    }
    if (this.activatedRoute.snapshot.params["id"] === undefined) {
      this.create();
    } else {
      this.update();
    }
  }

  create() {
    this.employeeService.create(this.employeeForm.value, this.selectedImageFile).subscribe({
      next: () => {
        this.toastr.success("Thêm nhân viên thành công");
        this.router.navigateByUrl("/admin/employee");
      },
      error: (err: any) => {
        this.handleError(err);
      }
    });
  }

  update() {
    this.employeeService.update(this.employeeForm.value, this.selectedImageFile).subscribe({
      next: () => {
        this.toastr.success("Cập nhật thông tin thành công");
        this.router.navigateByUrl("/admin/employee");
      },
      error: (err: any) => {
        this.handleError(err);
      }
    });
  }

  onFileChange(event: any) {
    const file = event.target.files[0];

    if (file) {
      if (!file.type.includes('image')) {
        this.toastr.error('Không phải file hình ảnh!', 'Thông báo');
        return;
      }
      else if (file.size > this.maxFileSize) {
        this.toastr.error('Dung lượng hình ảnh không được quá 10MB!', 'Thông báo');
        return;
      }
      else {
        this.selectedImageFile = file;
        const reader = new FileReader();
        reader.onload = (e: any) => {
          this.selectedImageUrl = e.target.result;
        };
        reader.readAsDataURL(file);
      }
    }
  }

  private handleError(error: any): void {
    console.log(error);
    if (error.status === 400 && error.error === 'DUPLICATE_EMAIL') {
      this.toastr.error('Email này đã tồn tại!', 'Thông báo');
    } else if (error.status === 400 && error.error === 'IMAGE_NOT_FOUND') {
      this.toastr.error('Hình ảnh không tồn tại!', 'Thông báo');
    } else if (error.status === 400 && error.error === 'FORBIDDEN') {
      this.toastr.error('Không được phép!', 'Thông báo');
    } else if (error.status === 400 && error.error === 'IMAGE_NOT_VALID') {
      this.toastr.error('Không phải file hình ảnh!', 'Thông báo');
    } else if (error.status === 400 && error.error === 'IMAGE_SIZE_TOO_LARGE_10MB') {
      this.toastr.error('Dung lượng hình ảnh không được quá 10MB!', 'Thông báo');
    } else {
      this.toastr.error('Lỗi không xác định.', 'Thông báo');
    }
  }

  findById(id: number) {
    this.employeeService.findById(id).subscribe({
      next: (data: any) => {
        this.employeeForm.patchValue(data);
        this.selectedImageUrl = Environment.apiBaseUrl + '/images/' + data.avatar;
        this.selectedImageFile = new File([""], "filename");
        this.employeeForm.get('status')?.setValue(data.status.toString());
        this.employeeForm.get('email')?.setValue(data.account.email.toString());
      },
      error: (err: any) => {
        this.toastr.error(err.error, "Thất bại");
      }
    });
  }

  getJsonDataAddress() {
    this.accountService.getJsonDataAddress().subscribe({
      next: (response) => {
        this.cities = response;
      },
      error: (error) => {
        console.log(error);
      }
    });
  }

  getDistrictsControl(): FormControl {
    const cityControl = this.employeeForm.get('city') as FormControl;
    cityControl.valueChanges.pipe().subscribe((id: any) => {
      this.cities?.forEach((city: any) => {
        if (city.name === id) {
          this.districts = city.districts;
          this.employeeForm.get('district')?.setValue(this.districts[0]?.name); // Đảm bảo mảng districts không rỗng trước khi gán giá trị
        }
      });
    });
    return cityControl;
  }

  getWardsControl(): FormControl {
    const districtControl = this.employeeForm.get('district') as FormControl;
    districtControl.valueChanges.pipe().subscribe((name: any) => {
      this.districts?.forEach((district: any) => {
        if (district.name === name) {
          this.wards = district.wards;
          this.employeeForm.get('ward')?.setValue(this.wards[0]?.name); // Đảm bảo mảng wards không rỗng trước khi gán giá trị
        }
      });
    });
    return districtControl;
  }
}

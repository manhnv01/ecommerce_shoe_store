export class EmployeeModel {
    id: number;
    name: string;
    phone: string;
    gender: string;
    avatar: string;
    birthday: Date;
    email: string;
    status: string;
    city: string;
    district: string;
    ward: string;
    isAccountNonLocked: boolean;
    addressDetail: string;
    constructor(data: any) {
        this.id = data.id;
        this.name = data.name;
        this.phone = data.phone;
        this.avatar = data.avatar;
        this.birthday = data.birthday
        this.gender = data.gender;
        this.email = data.email;
        this.status = data.status;
        this.city = data.city;
        this.district = data.district;
        this.ward = data.ward;
        this.addressDetail = data.addressDetail;
        this.isAccountNonLocked = data.isAccountNonLocked;
    }
}
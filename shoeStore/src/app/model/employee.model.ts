import { AccountModel } from "./account.model";

export class EmployeeModel {
    id: number;
    name: string;
    phone: string;
    gender: string;
    avatar: string;
    birthday: Date;
    account: AccountModel;
    constructor(data: any) {
        this.id = data.id;
        this.name = data.name;
        this.phone = data.phone;
        this.avatar = data.avatar;
        this.birthday = data.birthday
        this.gender = data.gender;
        this.account = new AccountModel(data.account);
    }
}
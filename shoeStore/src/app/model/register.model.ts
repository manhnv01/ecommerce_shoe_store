export class RegisterModel {
    name: string;
    email: string;
    password: string;
    confirmPassword: string;

    constructor(data: any) {
        this.name = data.name;
        this.email = data.email;
        this.password = data.password;
        this.confirmPassword = data.confirmPassword;
    }
}

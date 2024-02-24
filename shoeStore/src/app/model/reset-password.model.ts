export class ResetPasswordModel {
    email: string;
    code: string;
    newPassword: string;

    constructor(data: any) {
        this.email = data.email;
        this.code = data.code;
        this.newPassword = data.newPassword;
    }
}

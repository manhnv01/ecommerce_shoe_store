export class AccountModel {
    id: number;
    email: string;
    password: string;
    
    constructor(data: any) {
        this.id = data.id;
        this.email = data.email;
        this.password = data.password;
    }
}
export class SupplierModel {
    id: number;
    name: string;
    phone: string;
    email: string;
    address: string;
    
    constructor(data: any) {
        this.id = data.id;
        this.name = data.name;
        this.phone = data.phone;
        this.email = data.email;
        this.address = data.address;
    }
}
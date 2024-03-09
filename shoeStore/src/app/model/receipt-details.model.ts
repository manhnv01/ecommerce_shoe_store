export class ReceiptDetailsModel {
    id: number;
    price: number;
    quantity: number;
    productDetailsId: number;
    
    constructor(data: any) {
        this.id = data.id;
        this.price = data.price;
        this.quantity = data.quantity;
        this.productDetailsId = data.productId;
    }
}
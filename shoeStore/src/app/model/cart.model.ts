import { CartDetailsModel } from "./cart-details.model";

export class CartModel {
    id: number;
    totalProduct: number;
    cartDetails: CartDetailsModel[];
    
    constructor(data: any) {
        this.id = data.id;
        this.totalProduct = data.totalProduct;
        this.cartDetails = data.cartDetails;
    }
}
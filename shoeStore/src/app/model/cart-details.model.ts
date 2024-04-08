import { ProductColorModel } from "./product-color.model";
import { ProductDetailsModel } from "./product-details.model";

export class CartDetailsModel {
    id: number;
    quantity: number;
    productDetailsId: number;
    productSize: string;
    productThumbnail: string;
    productName: string;
    productColor: string;
    productPrice: number;
    totalPrice: number;
    productSlug: string;
    salePrice : number;


    constructor(data: any) {
        this.id = data.id;
        this.quantity = data.quantity;
        this.productDetailsId = data.productDetailsId;
        this.productSize = data.productSize;
        this.productThumbnail = data.productThumbnail;
        this.productName = data.productName;
        this.productColor = data.productColor;
        this.productPrice = data.productPrice;
        this.totalPrice = data.totalPrice;
        this.productSlug = data.productSlug;
        this.salePrice = data.salePrice;
    }
}
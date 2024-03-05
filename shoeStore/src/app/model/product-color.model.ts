import { ProductDetailsModel } from "./product-details.model";


export class ProductColorModel {
    id: number;
    color: string;
    productDetails: ProductDetailsModel[];
    constructor(data: any) {
        this.id = data.id;
        this.color = data.color;
        this.productDetails = data.productDetails;
    }
}
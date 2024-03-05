import { ProductColorModel } from "./product-color.model";

export class ProductModel {
    id: number;
    name: string;
    slug: string;
    enabled: boolean;
    description: string;
    thumbnail: string;
    price: number;
    images: string[];
    brandId: number;
    categoryId: number;
    productColors: ProductColorModel[];
    
    constructor(data: any) {
        this.id = data.id;
        this.name = data.name;
        this.slug = data.slug;
        this.enabled = data.enabled;
        this.description = data.description;
        this.images = data.images;
        this.thumbnail = data.thumbnail;
        this.price = data.price;
        this.brandId = data.brandId;
        this.categoryId = data.categoryId;
        this.productColors = data.productColors;
    }

    static createEmpty(): ProductModel {
        return new ProductModel({
            id: 0,
            name: "",
            slug: "",
            enabled: false,
            description: "",
            thumbnail: "",
            price: 0,
            images: [],
            brandId: 0,
            categoryId: 0,
            size: [],
            productColors: [],
        });
    }
}
export class ProductDetailsModel {
    id: number;
    size: string;
    
    constructor(data: any) {
        this.id = data.id;
        this.size = data.size;
    }
}
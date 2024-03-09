export class SaleModel {
    id: number;
    name: string;
    startDate: Date;
    endDate: Date;
    discount: number;
    productIds: number[];
    
    constructor(data: any) {
        this.id = data.id;
        this.name = data.name;
        this.startDate = data.startDate;
        this.endDate = data.endDate;
        this.discount = data.discount;
        this.productIds = data.productIds;
    }
}
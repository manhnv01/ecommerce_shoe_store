import { ReceiptDetailsModel } from "./receipt-details.model";

export class ReceiptModel {
    id: number;
    employeeId: number;
    supplierId: number;
    receiptDetails: ReceiptDetailsModel[];
    
    constructor(data: any) {
        this.id = data.id;
        this.employeeId = data.employeeId;
        this.supplierId = data.supplierId;
        this.receiptDetails = data.receiptDetails;
    }
}
import { OrderDetailsModel } from "./order-details.model";

export class OrderModel {
    id: number;
    fullname: string;
    address: string;
    phone: string;
    note: string;
    createdDate: Date;
    paymentDate: Date;
    paymentMethod: number;
    paymentStatus: boolean;
    completedDate: Date;
    orderStatus: number; // 0: chờ xác nhận, 1: đã xác nhận, 2: đang giao hàng, 3: đã giao hàng, 4: đã hủy
    confirmDate: any; // ngày xác nhận đơn hàng
    deliveryToShipperDate: any; // ngày giao cho shipper
    deliveryDate: any; // ngày giao hàng
    receiveDate: any; // ngày nhận hàng
    cancelDate: any; // ngày hủy đơn hàng
    cancelReason: any; // lý do hủy đơn hàng
    orderDetails: OrderDetailsModel[];
    totalMoney: number = 0;
  
    constructor(id: number, fullname: string, address: string, phone: string, email: string, note: string, createdDate: Date, paymentDate: Date, paymentMethod: number, paymentStatus: boolean, completeDate: Date, orderStatus: number, orderDetails: OrderDetailsModel[]) {
      this.id = id;
      this.fullname = fullname;
      this.address = address;
      this.phone = phone;
      this.note = note;
      this.createdDate = createdDate;
      this.paymentDate = paymentDate;
      this.paymentMethod = paymentMethod;
      this.paymentStatus = paymentStatus;
      this.completedDate = completeDate;
      this.orderStatus = orderStatus;
      this.orderDetails = orderDetails;
    }
}
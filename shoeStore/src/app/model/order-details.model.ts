export class OrderDetailsModel {
  id: number;
  productDetailsId: number;
  quantity: number;
  price: number;

  productName: string = '';
  productColor: string = '';
  productThumbnail: string = '';
  totalMoney: number = 0;

  constructor(id: number, productDetailsId: number, quantity: number, price: number) {
    this.id = id;
    this.productDetailsId = productDetailsId;
    this.quantity = quantity;
    this.price = price;
  }
}
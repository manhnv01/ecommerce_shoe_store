import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-detail-order',
  templateUrl: './detail-order.component.html',
  styleUrls: ['./detail-order.component.css']
})
export class DetailOrderComponent implements OnInit {

  constructor() { }

  ngOnInit() {
    this.startClock();
  }

  startClock(): void {
    setInterval(() => {
      this.getCurrentTime();
    }, 1000); // Gọi hàm time() mỗi giây (1000 milliseconds)
  }

  getCurrentTime(): string {
    const today = new Date();
    const weekday = ["Chủ Nhật", "Thứ Hai", "Thứ Ba", "Thứ Tư", "Thứ Năm", "Thứ Sáu", "Thứ Bảy"];
    const day = weekday[today.getDay()];
    let dd: any = today.getDate();
    let mm: any = today.getMonth() + 1;
    const yyyy = today.getFullYear();
    let h: any = today.getHours();
    let m: any = today.getMinutes();
    let s: any = today.getSeconds();
    m = this.checkTime(m);
    s = this.checkTime(s);
    let nowTime = h + " giờ " + m + " phút " + s + " giây";
    if (dd < 10) {
      dd = '0' + dd;
    }
    if (mm < 10) {
      mm = '0' + mm;
    }
    const currentDate = day + ', ' + dd + '/' + mm + '/' + yyyy;
    return currentDate + ' | ' + nowTime;
  }

  private checkTime(i: number): string {
    if (i < 10) {
      return "0" + i;
    }
    return i.toString();
  }

}

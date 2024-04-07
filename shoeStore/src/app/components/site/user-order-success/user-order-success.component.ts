import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-user-order-success',
  templateUrl: './user-order-success.component.html',
  styleUrls: ['./user-order-success.component.css']
})
export class UserOrderSuccessComponent implements OnInit {

  orderId: number = 0;

  constructor(private title: Title, private router: Router, private activatedRoute: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.title.setTitle('Đặt hàng thành công');
    this.orderId = this.activatedRoute.snapshot.params['id'];
  }

}

import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Location } from '@angular/common';


@Component({
  selector: 'app-forbidden',
  templateUrl: './forbidden.component.html',
  styleUrls: ['./forbidden.component.css']
})
export class ForbiddenComponent implements OnInit {

  constructor(
    private location: Location,
    private title: Title,
  ) { }

  ngOnInit() {
    this.title.setTitle('403 Forbidden');
  }

  callBack() {
    this.location.back();
    //window.history.back();
  }

}

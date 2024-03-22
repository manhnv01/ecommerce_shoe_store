import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { AccountService } from 'src/app/service/account.service';
import { TokenService } from 'src/app/service/token.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-admin-layout',
  templateUrl: './admin-layout.component.html',
  styleUrls: ['./admin-layout.component.css']
})
export class AdminLayoutComponent implements OnInit {

  constructor(
    private accountService: AccountService,
    private tokenService: TokenService,
    private title: Title,
    private toastr: ToastrService
  ) { }

  ngOnInit() {
  }

  logout(): void {
    this.accountService.logout();
  }

}

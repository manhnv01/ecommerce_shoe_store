import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { AccountService } from 'src/app/service/account.service';
import { TokenService } from 'src/app/service/token.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  constructor(
    private accountService: AccountService,
    private tokenService: TokenService,
    private title: Title
  ) { }

  ngOnInit() {
    this.title.setTitle('Trang chủ');
  }

  logout(): void {
    this.accountService.logout();
  }

  isLogin(): boolean {
    return this.tokenService.isLogin();
  }

  protected readonly window = window;

}

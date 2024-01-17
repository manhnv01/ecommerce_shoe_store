import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CustomerService } from 'src/app/service/customer.service';

@Component({
  selector: 'app-verify',
  templateUrl: './verify.component.html',
  styleUrls: ['./verify.component.css']
})
export class VerifyComponent implements OnInit {

  constructor( private customerService: CustomerService,
    private activatedRoute: ActivatedRoute,
    private router: Router) { }

  ngOnInit() {
    const code2 = this.activatedRoute.snapshot.queryParams['code'];
    this.activatedRoute.queryParams.subscribe((params) => {
      const { 'code': code = code2 } = params;

      this.verify(code);
    });
  }

  verify(code: string): void {
    this.customerService.verificationEmail(code).subscribe({
      next: (response) => {
        console.log(response);
      },
      error: (err) => {
        console.log(err.error.message);
      }
    });
  }
}

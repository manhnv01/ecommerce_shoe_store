import { Component, OnInit } from '@angular/core';
import Chart from 'chart.js/auto';
import { Environment } from 'src/app/environment/environment';
import { OrderService } from 'src/app/service/order.service';
import { ProductService } from 'src/app/service/product.service';
import { ReceiptService } from 'src/app/service/receipt.service';
import { ReportService } from 'src/app/service/report.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  pieChartCategory: any;
  pieChartBrand: any;
  lineChart: any;

  baseUrl = Environment.apiBaseUrl;

  outOfStock: number = 0;
  orderToday: number = 0;
  orderPending: number = 0;

  labelCategory: string[] = [];
  dataCategory: number[] = [];

  labelBrand: string[] = [];
  dataBrand: number[] = [];

  constructor(
    private productService: ProductService,
    private orderService: OrderService,
    private reportService: ReportService) {
  }

  ngOnInit(): void {
    this.countProductOutOfStock();
    this.getTotalOrder();
    this.reportCategory();
    this.reportBrand();

    this.createLineChart();
  }

  countProductOutOfStock() {
    this.outOfStock = 0;
    this.productService.getAllNonPage().subscribe(
      (data: any) => {
        for (let i = 0; i < data.length; i++) {
          // đếm số sản phẩm hêt hàng
          if (data[i].totalQuantity === 0) {
            this.outOfStock++;
          }
        }
      }
    );
  }

  getTotalOrder() {
    this.orderService.getTotalsForAdmin().subscribe(
      (data: any) => {
        this.orderToday = data.today;
        this.orderPending = data.pending;
      }
    );
  }

  createPieChartCategory(labels: string[] = [], data: number[] = []) {
    this.pieChartCategory = new Chart("pieChartCategory", {
      type: 'pie',
      data: {
        labels: labels,
        datasets: [{
          label: ' đã bán',
          data: data,
          borderWidth: 1
        }]
      },
    });
  }

  createPieChartBrand(labels: string[] = [], data: number[] = []) {
    this.pieChartBrand = new Chart("pieChartBrand", {
      type: 'doughnut',
      data: {
        labels: labels,
        datasets: [{
          label: ' đã bán',
          data: data,
          borderWidth: 1
        }]
      },
    });
  }

  createLineChart() {
    this.lineChart = new Chart("lineChart", {
      data: {
        labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
        datasets: [
          {
            type: 'line',
            label: 'Doanh thu',
            data: [100000000, 440000044, 180000000, 444000004, 350000000, 444000004, 254000000, 333000003, 280000010, 440000044, 280000000, 230000000],
          },
          {
            type: 'line',
            label: 'Chi phí',
            data: [100000000, 99000009, 150000000, 99000009, 200000000, 99000009, 180000000, 250000000, 220000000, 99000009, 280000000, 300000000],
          }
        ],
      },
      options: {
        scales: {
          y: {
            beginAtZero: true
          }
        }
      }
    });
  }

  reportCategory() {
    this.reportService.reportCategory().subscribe(
      (data: any) => {
        this.labelCategory = data.map((item: any) => item.categoryName);
        this.dataCategory = data.map((item: any) => item.productCount);
        this.createPieChartCategory(this.labelCategory, this.dataCategory);
        console.log("cate", data);
      }
    );
  }

  reportBrand() {
    this.reportService.reportBrand().subscribe(
      (data: any) => {
        this.labelBrand = data.map((item: any) => item.brandName);
        this.dataBrand = data.map((item: any) => item.productCount);
        this.createPieChartBrand(this.labelBrand, this.dataBrand);
        console.log("brand", data);
      }
    );
  }
}
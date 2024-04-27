import { Component, OnInit } from '@angular/core';
import Chart from 'chart.js/auto';
import { Environment } from 'src/app/environment/environment';
import { OrderService } from 'src/app/service/order.service';
import { ProductService } from 'src/app/service/product.service';
import { ReceiptService } from 'src/app/service/receipt.service';
import { ReportService } from 'src/app/service/report.service';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  pieChartCategory: any;
  pieChartBrand: any;
  lineChart: any;

  years: number[] = [];

  chooseYear: number = new Date().getFullYear();

  yearNow: number = new Date().getFullYear();
  monthNow: string = ('0' + (new Date().getMonth() + 1)).slice(-2); // Đảm bảo có hai chữ số cho tháng

  bestSeller: any;

  baseUrl = Environment.apiBaseUrl;

  outOfStock: number = 0;
  orderToday: number = 0;
  orderPending: number = 0;

  labelCategory: string[] = [];
  dataCategory: number[] = [];

  labelBrand: string[] = [];
  dataBrand: number[] = [];

  constructor(
    private title: Title,
    private activatedRoute: ActivatedRoute,
    private productService: ProductService,
    private orderService: OrderService,
    private reportService: ReportService) {
  }

  ngOnInit(): void {
    this.title.setTitle("Tổng quan");
    this.createYears();
    this.countProductOutOfStock();
    this.getTotalOrder();
    this.reportCategory();
    this.reportBrand();

    this.reportRevenue();
    this.reportCost();

    this.createLineChart();

    this.activatedRoute.queryParams.subscribe((params) => {
      const { month = this.monthNow, year = this.yearNow } = params;

      this.getTop5BestSeller(month, year);
    });
  }

  getTop5BestSeller(month: number, year: number) {
    this.reportService.getTop5BestSeller(month, year).subscribe(
      (data: any) => {
        this.bestSeller = data;
      }
    );
  }

  onYearSelect(event: any) {
    this.chooseYear = event.target.value;
    this.reportRevenue();
    this.reportCost();
  }

  countProductOutOfStock() {
    this.outOfStock = 0;
    this.productService.getAllNonPage().subscribe(
      (data: any) => {
        for (let i = 0; i < data.length; i++) {
          // đếm số sản phẩm hêt hàng
          if (data[i].totalQuantity === 0 && data[i].enabled === true) {
            this.outOfStock++;
          }
        }
      }
    );
  }

  changeDateBestSeller(event: any) {
    const selectedDate: string = event.target.value; // Lấy giá trị của control chọn tháng và năm

    // Phân tích giá trị được chọn thành tháng và năm
    const [year, month] = selectedDate.split('-');

    // Gọi hàm để lấy danh sách top 5 sản phẩm bán chạy nhất cho tháng và năm đã chọn
    this.getTop5BestSeller(parseInt(month), parseInt(year));
  }


  getTotalOrder() {
    this.orderService.getTotalsForAdmin().subscribe(
      (data: any) => {
        this.orderToday = data.today;
        this.orderPending = data.pending;
      }
    );
  }

  createYears() {
    // Tạo 5 năm trước
    for (let i = 5; i > 0; i--) {
      this.years.push(this.yearNow - i);
    }

    // Thêm năm hiện tại
    this.years.push(this.yearNow);

    // Tạo 5 năm sau
    for (let i = 1; i <= 5; i++) {
      this.years.push(this.yearNow + i);
    }
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
        labels: ['Tháng', 'Tháng 1', 'Tháng 2', 'Tháng 3', 'Tháng 4', 'Tháng 5', 'Tháng 6', 'Tháng 7', 'Tháng 8', 'Tháng 9', 'Tháng 10', 'Tháng 11', 'Tháng 12'],
        datasets: [
          {
            type: 'line',
            label: 'Doanh thu: ',
            data: [],
          },
          {
            type: 'line',
            label: 'Chi phí: ',
            data: [],
          }
        ],
      },
      options: {
        scales: {
          y: {
            beginAtZero: true
          },
          x: {
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

  reportRevenue() {
    this.reportService.getRevenueByYear(this.chooseYear).subscribe({
      next: (data: any) => {
        let dataRevenue = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
        data.forEach((item: any) => {
          dataRevenue[item.month] = item.revenue;
        });
        this.lineChart.data.datasets[0].data = dataRevenue;
        this.lineChart.update();
      }
    });
  }


  reportCost() {
    this.reportService.getCostByYear(this.chooseYear).subscribe({
      next: (data: any) => {
        let dataCost = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
        data.forEach((item: any) => {
          dataCost[item.month] = item.cost;
        });
        this.lineChart.data.datasets[1].data = dataCost;
        this.lineChart.update();
      }
    });
  }
}
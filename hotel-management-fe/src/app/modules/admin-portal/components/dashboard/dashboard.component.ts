import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import { ChartConfiguration, ChartData, ChartEvent, ChartType } from 'chart.js';
import { BaseChartDirective } from 'ng2-charts';
import {RentalSlipService} from "../../services/rental-slip.service";
import {CommonUtil} from "../../../../util/CommonUtil";
import html2canvas from "html2canvas";
import jspdf from "jspdf";
import {AuthAdminService} from "../../services/auth-admin.service";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  @ViewChild(BaseChartDirective) chart: BaseChartDirective | undefined;
  @ViewChild('printableAreaBar', {static: false}) elementRefBar!: ElementRef;
  @ViewChild('printableAreaPie', {static: false}) elementRefPie!: ElementRef;
  @ViewChild('downloadTableBar', {static: false}) downloadTableBar!: ElementRef;
  @ViewChild('downloadTablePie', {static: false}) downloadTablePie!: ElementRef;
  dateFrom: Date = new Date(new Date().getFullYear(), 0, 1);
  dateTo: Date = new Date(new Date().getFullYear(),11, 31);
  currentDate = new Date();
  public barChartOptions: ChartConfiguration['options'] = {
    elements: {
      line: {
        tension: 0.4,
      },
    },
    responsive: true,
    // We use these empty structures as placeholders for dynamic theming.
    scales: {
      x: {},
      y: {
        min: 10,
      },
    },
    plugins: {
      legend: {
        display: true,
      },
    },
  };
  public barChartType: ChartType = 'bar';

  public barChartData!: ChartData<'bar'>;

  // Pie
  public pieChartOptions: ChartConfiguration['options'] = {
    responsive: true,
    plugins: {
      legend: {
        display: true,
        position: 'top',
      },
    },
  };
  public pieChartData: ChartData<'pie', number[], string | string[]> = {
    labels: ['Download', 'In Store Sales', 'Mail Sales'],
    datasets: [
      {
        data: [300, 500, 100],
      },
    ],
  };
  public pieChartType: ChartType = 'pie';
  public barChartTableData!: [];
  public pieChartTableData!: [];

  constructor(
    private rentalSlipService: RentalSlipService,
    private authAdminService: AuthAdminService,
  ) { }

  ngOnInit() {
    this.getChartData();
  }

  // events
  public chartClicked({
                        event,
                        active,
                      }: {
    event?: ChartEvent;
    active?: object[];
  }) {
    console.log(event, active);
  }

  getChartData() {
    const request = {
      dateFrom: this.dateFrom,
      dateTo: this.dateTo
    }
    this.rentalSlipService.getReport(request).subscribe(resp => {
      console.log('resp', resp)
      this.barChartData = resp.chartRevenue;
      this.barChartTableData = resp.dataRevenue;
      this.pieChartData = resp.chartRoomRate;
      this.pieChartTableData = resp.dataRoomRate;
    })
  }

  getMonthFrom() {
    console.log(this.dateFrom)
    this.getChartData()
  }

  getMonthTo() {
    const endOfMonth = new Date(this.dateTo.getFullYear(), this.dateTo.getMonth() + 1, 0);
    console.log(endOfMonth);
    this.dateTo = endOfMonth;
    this.getChartData()
  }

  getPrice(price: any) {
    return price && price != 0 ? CommonUtil.formatAmount(price.toString()) : 0;
  }

  getRate(total: any, number: any) {
    return Number(number / total * 100).toFixed(2)
  }

  getDateFrom() {
    return this.dateFrom ? this.dateFrom : new Date(new Date().getFullYear(), 0, 1);
  }

  getDateTo() {
    return this.dateTo ? this.dateTo : new Date(new Date().getFullYear(),11, 31);
  }

  getFullName() {
    return this.authAdminService.getLoginAdminInfor()?.fullName;
  }

  printInvoiceBar() {
    let newWin = (window.frames as any)["print_frame_bar"];
    let clone = this.elementRefBar.nativeElement.cloneNode(true);
    clone.style.visibility = 'visible';
    clone.style.position = 'relative';
    document.body.appendChild(clone);
    let width = clone.offsetWidth;
    let height = clone.offsetHeight;
    console.log('WH', width, height);
    newWin.document.write('<head><link rel="stylesheet" href="http://localhost:4200/styles.css"></head> <body onload="window.print()">' + this.elementRefBar.nativeElement?.innerHTML + '</body>');
    newWin.document.close();
    console.log(newWin)
    document.body.removeChild(clone);
  }

  downloadPDFBar() {
    let newWin = (window.frames as any)["print_frame_bar"];
    let clone = this.elementRefBar.nativeElement.cloneNode(true);
    clone.style.visibility = 'visible';
    clone.style.position = 'relative';
    clone.style.display = 'block';
    document.body.appendChild(clone);
    newWin.document.write('<head><link rel="stylesheet" href="http://localhost:4200/styles.css"></head> <body>' + this.elementRefBar.nativeElement?.innerHTML + '</body>');
    newWin.document.close();
    console.log(newWin.document.body)

    let width = newWin.document.body.offsetWidth;
    let height = newWin.document.body.offsetHeight;
    console.log('WH', width, height);
    const ratePixelToMillimeters = 25.4 / 96;
    const rateMillimetersToPoint = 0.3527777777777777;
    let imgWidth = width * ratePixelToMillimeters;
    let imgHeight = height * ratePixelToMillimeters;
    let pageHeight = (imgHeight > 297) ? imgHeight + 5 : 297;
    let pageWidth = 210;
    let positionX = (pageWidth - imgWidth) / 2;
    let positionY = 2;
    console.log(imgWidth, imgHeight)
    document.body.removeChild(clone);
    html2canvas(newWin.document.body, { scale: 3 }).then(canvas => {
      const contentDataURL = canvas.toDataURL('image/png');
      let pdf = new jspdf('p', 'mm', [(pageHeight / rateMillimetersToPoint), (pageWidth / rateMillimetersToPoint)]);
      let width = pdf.internal.pageSize.getWidth();
      let height = pdf.internal.pageSize.getHeight();
      console.log('Width: ', width, ', Height: ', height);
      pdf.addImage(contentDataURL, 'PNG', positionX, positionY, imgWidth, imgHeight);
      pdf.save(`BCDT_${new Date()}.pdf`);
    });
  }

  printInvoicePie() {
    let newWin = (window.frames as any)["print_frame_pie"];
    let clone = this.elementRefPie.nativeElement.cloneNode(true);
    clone.style.visibility = 'visible';
    clone.style.position = 'relative';
    document.body.appendChild(clone);
    let width = clone.offsetWidth;
    let height = clone.offsetHeight;
    console.log('WH', width, height);
    newWin.document.write('<head><link rel="stylesheet" href="http://localhost:4200/styles.css"></head> <body onload="window.print()">' + this.elementRefPie.nativeElement?.innerHTML + '</body>');
    newWin.document.close();
    console.log(newWin)
    document.body.removeChild(clone);
  }

  downloadPDFPie() {
    let newWin = (window.frames as any)["print_frame_pie"];
    let clone = this.elementRefPie.nativeElement.cloneNode(true);
    clone.style.visibility = 'visible';
    clone.style.position = 'relative';
    clone.style.display = 'block';
    document.body.appendChild(clone);
    newWin.document.write('<head><link rel="stylesheet" href="http://localhost:4200/styles.css"></head> <body>' + this.elementRefPie.nativeElement?.innerHTML + '</body>');
    newWin.document.close();
    console.log(newWin.document.body)

    let width = newWin.document.body.offsetWidth;
    let height = newWin.document.body.offsetHeight;
    console.log('WH', width, height);
    const ratePixelToMillimeters = 25.4 / 96;
    const rateMillimetersToPoint = 0.3527777777777777;
    let imgWidth = width * ratePixelToMillimeters;
    let imgHeight = height * ratePixelToMillimeters;
    let pageHeight = (imgHeight > 297) ? imgHeight + 5 : 297;
    let pageWidth = 210;
    let positionX = (pageWidth - imgWidth) / 2;
    let positionY = 2;
    console.log(imgWidth, imgHeight)
    document.body.removeChild(clone);
    html2canvas(newWin.document.body, { scale: 3 }).then(canvas => {
      const contentDataURL = canvas.toDataURL('image/png');
      let pdf = new jspdf('p', 'mm', [(pageHeight / rateMillimetersToPoint), (pageWidth / rateMillimetersToPoint)]);
      let width = pdf.internal.pageSize.getWidth();
      let height = pdf.internal.pageSize.getHeight();
      console.log('Width: ', width, ', Height: ', height);
      pdf.addImage(contentDataURL, 'PNG', positionX, positionY, imgWidth, imgHeight);
      pdf.save(`BCCST_${new Date()}.pdf`);
    });
  }
}

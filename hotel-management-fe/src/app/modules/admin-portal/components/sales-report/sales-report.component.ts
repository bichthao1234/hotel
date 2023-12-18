import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {BaseChartDirective} from "ng2-charts";
import {ChartConfiguration, ChartData, ChartType} from "chart.js";
import {RentalSlipService} from "../../services/rental-slip.service";
import {CommonUtil} from "../../../../util/CommonUtil";
import html2canvas from "html2canvas";
import jspdf from "jspdf";
import {AuthAdminService} from "../../services/auth-admin.service";

@Component({
  selector: 'app-sales-report',
  templateUrl: './sales-report.component.html',
  styleUrls: ['./sales-report.component.css']
})
export class SalesReportComponent implements OnInit {
  @ViewChild(BaseChartDirective) chart: BaseChartDirective | undefined;
  @ViewChild('printableAreaBar', {static: false}) elementRefBar!: ElementRef;
  @ViewChild('downloadTableBar', {static: false}) downloadTableBar!: ElementRef;
  dateFrom: Date = new Date(new Date().getFullYear(), 0, 1);
  dateTo: Date = new Date(new Date().getFullYear(), 11, 31);
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

  public barChartTableData!: [];

  constructor(private rentalSlipService: RentalSlipService,
              private authAdminService: AuthAdminService) {
  }

  ngOnInit() {
    this.getChartData();
  }

  getChartData() {
    const request = {
      dateFrom: this.dateFrom,
      dateTo: this.dateTo
    }
    this.rentalSlipService.getReport(request).subscribe(resp => {
      this.barChartData = resp.chartRevenue;
      this.barChartTableData = resp.dataRevenue;
    })
  }

  getMonthFrom() {
    this.getChartData()
  }

  getMonthTo() {
    const endOfMonth = new Date(this.dateTo.getFullYear(), this.dateTo.getMonth() + 1, 0);
    this.dateTo = endOfMonth;
    this.getChartData()
  }

  getPrice(price: any) {
    return price && price != 0 ? CommonUtil.formatAmount(price.toString()) : 0;
  }

  getDateFrom() {
    return this.dateFrom ? this.dateFrom : new Date(new Date().getFullYear(), 0, 1);
  }

  getDateTo() {
    return this.dateTo ? this.dateTo : new Date(new Date().getFullYear(), 11, 31);
  }

  getCloneTable() {
    let clone = this.elementRefBar.nativeElement.cloneNode(true);
    let printableArea = clone.querySelector('.printable-area');
    if (printableArea) {
      printableArea.setAttribute('style', 'height: 100% !important');
    }
    clone.style.visibility = 'visible';
    clone.style.position = 'relative';
    clone.setAttribute('style', 'height: fit-content');
    return clone;
  }

  printInvoiceBar() {
    let newWin = (window.frames as any)["print_frame_bar"];
    let clone = this.getCloneTable();
    document.body.appendChild(clone);
    newWin.document.write('<head><link rel="stylesheet" href="http://localhost:4200/styles.css"></head> <body onload="window.print()">' + clone?.innerHTML + '</body>');
    newWin.document.close();
    document.body.removeChild(clone);
  }

  downloadPDFBar() {
    let newWin = (window.frames as any)["print_frame_bar"];
    let clone = this.getCloneTable();
    document.body.appendChild(clone);
    newWin.document.write('<head><link rel="stylesheet" href="http://localhost:4200/styles.css"></head> <body>' + clone?.innerHTML + '</body>');
    newWin.document.close();

    let width = clone.offsetWidth;
    let height = clone.offsetHeight;
    const ratePixelToMillimeters = 25.4 / 96;
    const rateMillimetersToPoint = 0.3527777777777777;
    let imgWidth = (width * ratePixelToMillimeters);
    imgWidth += imgWidth * 0.8;
    let imgHeight = height * ratePixelToMillimeters;
    let pageHeight = (imgHeight > 297) ? imgHeight + 5 : 297;
    let pageWidth = 210;
    let positionX = (pageWidth - imgWidth) / 2;
    let positionY = 0;
    document.body.removeChild(clone);
    html2canvas(newWin.document.body, {scale: 2}).then(canvas => {
      const contentDataURL = canvas.toDataURL('image/png');
      let pdf = new jspdf('p', 'mm', [(pageHeight / rateMillimetersToPoint), (pageWidth / rateMillimetersToPoint)]);
      let width = pdf.internal.pageSize.getWidth();
      let height = pdf.internal.pageSize.getHeight();
      pdf.addImage(contentDataURL, 'PNG', positionX, positionY, imgWidth, imgHeight);
      pdf.save(`BCDT_${new Date()}.pdf`);
    });
  }

  getFullName() {
    return this.authAdminService.getLoginAdminInfor()?.fullName;
  }
}

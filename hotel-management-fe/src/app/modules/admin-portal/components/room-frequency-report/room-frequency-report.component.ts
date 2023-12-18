import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {ChartConfiguration, ChartData, ChartType} from "chart.js";
import {RentalSlipService} from "../../services/rental-slip.service";
import {CommonUtil} from "../../../../util/CommonUtil";
import html2canvas from "html2canvas";
import jspdf from "jspdf";
import {AuthAdminService} from "../../services/auth-admin.service";

@Component({
  selector: 'app-room-frequency-report',
  templateUrl: './room-frequency-report.component.html',
  styleUrls: ['./room-frequency-report.component.css']
})
export class RoomFrequencyReportComponent implements OnInit {
  @ViewChild('printableAreaPie', {static: false}) elementRefPie!: ElementRef;
  dateFrom: Date = new Date(new Date().getFullYear(), 0, 1);
  dateTo: Date = new Date(new Date().getFullYear(), 11, 31);
  currentDate = new Date();
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
  public pieChartTableData!: [];

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
      this.pieChartData = resp.chartRoomRate;
      this.pieChartTableData = resp.dataRoomRate;
    })
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
    return this.dateTo ? this.dateTo : new Date(new Date().getFullYear(), 11, 31);
  }

  getCloneTable() {
    let clone = this.elementRefPie.nativeElement.cloneNode(true);
    let printableArea = clone.querySelector('.printable-area');
    if (printableArea) {
      printableArea.setAttribute('style', 'height: 100% !important');
    }
    clone.style.visibility = 'visible';
    clone.style.position = 'relative';
    clone.setAttribute('style', 'height: fit-content');
    return clone;
  }

  printInvoicePie() {
    let newWin = (window.frames as any)["print_frame_pie"];
    let clone = this.getCloneTable();
    document.body.appendChild(clone);
    let width = clone.offsetWidth;
    let height = clone.offsetHeight;
    console.log('WH', width, height);
    newWin.document.write('<head><link rel="stylesheet" href="http://localhost:4200/styles.css"></head><body onload="window.print()">' + clone?.innerHTML + '</body>');
    newWin.document.close();
    document.body.removeChild(clone);
  }

  downloadPDFPie() {
    let newWin = (window.frames as any)["print_frame_pie"];
    let clone = this.getCloneTable();
    document.body.appendChild(clone);
    newWin.document.write('<head><link rel="stylesheet" href="http://localhost:4200/styles.css"></head><body>' + clone?.innerHTML + '</body>');
    newWin.document.close();
    let width = clone.offsetWidth;
    let height = clone.offsetHeight;
    console.log('WH', width, height);
    const ratePixelToMillimeters = 25.4 / 96;
    const rateMillimetersToPoint = 0.3527777777777777;
    let imgWidth = (width * ratePixelToMillimeters);
    imgWidth += imgWidth * 0.8;
    let imgHeight = height * ratePixelToMillimeters;
    let pageHeight = (imgHeight > 297) ? imgHeight : 297;
    let pageWidth = 210;
    let positionX = (pageWidth - imgWidth) / 2;
    let positionY = 0;
    console.log(imgWidth, imgHeight)
    document.body.removeChild(clone);
    html2canvas(newWin.document.body, {scale: 2}).then(canvas => {
      const contentDataURL = canvas.toDataURL('image/png');
      let pdf = new jspdf('p', 'mm', [(pageHeight / rateMillimetersToPoint), (pageWidth / rateMillimetersToPoint)]);
      pdf.addImage(contentDataURL, 'PNG', positionX, positionY, imgWidth, imgHeight);
      pdf.save(`BCCST_${new Date()}.pdf`);
    });
  }

  getFullName() {
    return this.authAdminService.getLoginAdminInfor()?.fullName;
  }
  getMonthFrom() {
    this.getChartData()
  }

  getMonthTo() {
    const endOfMonth = new Date(this.dateTo.getFullYear(), this.dateTo.getMonth() + 1, 0);
    this.dateTo = endOfMonth;
    this.getChartData()
  }
}

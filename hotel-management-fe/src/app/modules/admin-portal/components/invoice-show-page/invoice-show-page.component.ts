import {Component, ElementRef, Inject, OnInit, ViewChild} from '@angular/core';
import {InvoiceService} from "../../services/invoice.service";
import {CommonUtil} from "../../../../util/CommonUtil";
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import jspdf from 'jspdf';
import html2canvas from 'html2canvas';

@Component({
  selector: 'app-invoice-show-page',
  templateUrl: './invoice-show-page.component.html',
  styleUrls: ['./invoice-show-page.component.css']
})
export class InvoiceShowPageComponent implements OnInit {
  @ViewChild('printableArea', {static: false}) el!: ElementRef;
  invoice: any;

  constructor(private invoiceService: InvoiceService,
              @Inject(MAT_DIALOG_DATA) public invoiceId: any) {
  }

  ngOnInit() {
    this.getInvoice();
    const element = document.getElementById('printableArea');
    if (element) {
      window.scrollTo(0,0);
    }
  }

  getInvoice() {
    this.invoiceService.getInvoiceById(this.invoiceId).subscribe((resp) => {
      if (resp) {
        this.invoice = resp;
      }
    });
  }

  getPrice(price: any) {
    return CommonUtil.formatAmount(price.toString());
  }

  getStatus(status: any) {
    return status === 0 ? 'Chưa thanh toán' : 'Đã thanh toán';
  }

  getCredit(totalPrice: any, promotion: any, deposit: any) {
    const credit = Number(totalPrice) - Number(promotion) - Number(deposit);
    return this.getPrice(credit);
  }
  printInvoice() {
    let newWin = (window.frames as any)["print_frame"];
    newWin.document.write('<head><link rel="stylesheet" href="http://localhost:4200/styles.css"></head> <body onload="window.print()">' + this.el.nativeElement?.innerHTML + '</body>');
    newWin.document.close();
  }

  downloadPDF() {
    let width = this.el.nativeElement.offsetWidth;
    let height = this.el.nativeElement.offsetHeight;
    const ratePixelToMillimeters = 25.4 / 96;
    const rateMillimetersToPoint = 0.3527777777777777;
    let imgWidth = width * ratePixelToMillimeters;
    let imgHeight = height * ratePixelToMillimeters;
    let pageHeight = (imgHeight > 297) ? imgHeight + 5 : 297;
    let pageWidth = 210;
    let positionX = (pageWidth - imgWidth) / 2;
    let positionY = 2;
    console.log(imgWidth, imgHeight)

    html2canvas(this.el.nativeElement).then(canvas => {
      const contentDataURL = canvas.toDataURL('image/png');
      let pdf = new jspdf('p', 'mm', [(pageHeight / rateMillimetersToPoint), (pageWidth / rateMillimetersToPoint)]);
      let width = pdf.internal.pageSize.getWidth();
      let height = pdf.internal.pageSize.getHeight();
      console.log('Width: ', width, ', Height: ', height);
      pdf.addImage(contentDataURL, 'PNG', positionX, positionY, imgWidth, imgHeight);
      pdf.save(`HD${this.invoice.invoiceId}.pdf`);
    });
  }

}

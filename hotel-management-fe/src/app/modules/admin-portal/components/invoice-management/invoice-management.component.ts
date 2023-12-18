import {Component, OnInit} from '@angular/core';
import {InvoiceService} from "../../services/invoice.service";
import {CommonUtil} from "../../../../util/CommonUtil";
import {MatDialog} from "@angular/material/dialog";
import {InvoiceShowPageComponent} from "../invoice-show-page/invoice-show-page.component";

@Component({
  selector: 'app-invoice-management',
  templateUrl: './invoice-management.component.html',
  styleUrls: ['./invoice-management.component.css']
})
export class InvoiceManagementComponent implements OnInit {
  invoiceList: any;

  constructor(private invoiceService: InvoiceService,
              private dialog: MatDialog) {
  }

  ngOnInit() {
    this.getAllInvoice();
  }

  getAllInvoice() {
    this.invoiceService.getAll().subscribe((resp: any) => {
      if (resp) {
        this.invoiceList = resp;
        console.log(this.invoiceList);
      }
    });
  }

  openInvoiceDetail(invoiceId: any) {
    let dialog = this.dialog.open(InvoiceShowPageComponent, {data: invoiceId, autoFocus: false});
  }

  getPriceFormat(price: any) {
    return CommonUtil.formatAmount(price.toString());
  }
}

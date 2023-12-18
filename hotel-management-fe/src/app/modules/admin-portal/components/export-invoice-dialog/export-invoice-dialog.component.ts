import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {CommonUtil} from "../../../../util/CommonUtil";
import {RentalSlipService} from "../../services/rental-slip.service";
import {AuthAdminService} from "../../services/auth-admin.service";
import {SweetAlertCustomizeService} from "../../../../services/sweet-alert-customize.service";
import {InvoiceService} from "../../services/invoice.service";
import {InvoiceShowPageComponent} from "../invoice-show-page/invoice-show-page.component";
import {PaymentDialogComponent} from "../paymen-dialog/payment-dialog.component";

@Component({
  selector: 'app-export-invoice-dialog',
  templateUrl: './export-invoice-dialog.component.html',
  styleUrls: ['./export-invoice-dialog.component.css']
})
export class ExportInvoiceDialogComponent implements OnInit {

  rentalSlipDetails: any;
  surcharges: any[] = [];
  services: any[] = [];
  deposit: number = 0;
  total: number = 0;
  paidAmount: number = 0;
  selectedRentalSlipDetail: any[] = [];
  previewInvoice: any;

  constructor(private dialogRef: MatDialogRef<ExportInvoiceDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private dialog: MatDialog,
              private rentalSlipService: RentalSlipService,
              private authAdminService: AuthAdminService,
              private invoiceService: InvoiceService,
              private sweetAlertCustomizeService: SweetAlertCustomizeService,
  ) {
  }

  ngOnInit() {
    console.log(this.data)
    this.getRentalSlipDetail()
  }

  closeDialog() {
    this.dialogRef.close();
  }

  getRentalSlipDetail() {
    let rentalSlipDetailIdList = [];
    if (this.data.isExportInvoice) {
      rentalSlipDetailIdList = this.data.rentalSlipDetailIds;
    } else if (this.data.isForPayment) {
      rentalSlipDetailIdList = this.data.rentalSlipDetailIds.map((item: any) => item.rentalSlipDetailId);
    }
    let request = {
      rentalSlipDetailIdList: rentalSlipDetailIdList,
      isForPayment: this.data.isForPayment,
      isExportInvoice: this.data.isExportInvoice
    };
    this.rentalSlipService.getRentalSlipDetailWithRentalSlipId(request).subscribe((resp) => {
      if (resp) {
        this.rentalSlipDetails = resp;
        this.rentalSlipDetails.forEach((object: any) => {
          this.surcharges = this.surcharges.concat(object.surcharge);
          this.services = this.services.concat(object.service);
          this.deposit = this.deposit + Number(object.deposit);
          this.paidAmount = this.paidAmount + Number(object.paidAmount);
          this.total = this.total + Number(object.total);
        });
      }
    });
  }

  getPrice(price: any) {
    return CommonUtil.formatAmount(price.toString());
  }

  getStatus(status: any) {
    return status === 0 ? 'Chưa thanh toán' : 'Đã thanh toán';
  }

  handlePayment() {
    const request = {
      rentalSlipDetailIdList: this.data.rentalSlipDetailIds,
      employeeId: this.authAdminService.getLoginAdminInfor()?.id
    }
    this.rentalSlipService.checkOut(request).subscribe((resp: any) => {
        if (resp) {
          this.sweetAlertCustomizeService.showSuccessAlertRightPopup(`Check out thành công!`);
          this.dialogRef.close({status: true});
        }
      },
      error => {
        this.sweetAlertCustomizeService.showErrorAlertRightPopup(error.error.error);
      });
  }

  onChangeSelectedRentalSlipDetail() {
    if (this.selectedRentalSlipDetail.length > 0) {
      this.previewInvoice = {
        total: 0,
        deposit: 0,
        paidAmount: 0
      }
      this.selectedRentalSlipDetail.forEach((object: any) => {
        this.previewInvoice.deposit = this.previewInvoice.deposit + Number(object.deposit);
        this.previewInvoice.paidAmount = this.previewInvoice.paidAmount + Number(object.paidAmount);
        this.previewInvoice.total = this.previewInvoice.total + Number(object.total);
      });
    } else {
      this.previewInvoice = null;
    }
  }

  createInvoice() {
    console.log(this.selectedRentalSlipDetail.length)
    const request = {
      rentalSlipDetailIdList: this.selectedRentalSlipDetail.map(item => item?.rentalSlipDetailId),
      employeeId: this.authAdminService.getLoginAdminInfor()?.id,
    }
    console.log(request)
    this.invoiceService.createInvoice(request).subscribe((resp: any) => {
        if (resp) {
          this.sweetAlertCustomizeService.showSuccessAlertRightPopup(`Tạo hóa đơn thành công!`);
          this.dialogRef.close({status: true});
          this.dialog.open(InvoiceShowPageComponent, {
            data: resp,
            autoFocus: false
          });
        }
      },
      error => {
        this.sweetAlertCustomizeService.showErrorAlertRightPopup(error.error.error);
      });
  }

  openDialog() {
    this.closeDialog();
    this.dialog.open(PaymentDialogComponent, {
      data: this.selectedRentalSlipDetail.map((item: any) => item.rentalSlipDetailId),
      disableClose: true
    })
  }
}

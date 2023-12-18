import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {CommonUtil} from "../../../../util/CommonUtil";
import {RentalSlipService} from "../../services/rental-slip.service";
import {RoomService} from "../../services/room.service";
import {AuthAdminService} from "../../services/auth-admin.service";
import {SweetAlertCustomizeService} from "../../../../services/sweet-alert-customize.service";

@Component({
  selector: 'app-payment-dialog',
  templateUrl: './payment-dialog.component.html',
  styleUrls: ['./payment-dialog.component.css']
})
export class PaymentDialogComponent implements OnInit {

  rentalSlipDetails: any;
  surcharges: any[] = [];
  services: any[] = [];
  deposit: number = 0;
  total: number = 0;
  paidAmount: number = 0;

  constructor(private dialogRef: MatDialogRef<PaymentDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private rentalSlipService: RentalSlipService,
              private authAdminService: AuthAdminService,
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
    this.rentalSlipService.getRentalSlipDetailWithRentalSlipId({
      rentalSlipDetailIdList: this.data,
      isForPayment: true,
      isExportInvoice: false
    }).subscribe((resp) => {
      if (resp) {
        this.rentalSlipDetails = resp;
        this.rentalSlipDetails.forEach((object: any) => {
          this.surcharges = this.surcharges.concat(object.surcharge);
          this.services = this.services.concat(object.service);
          this.deposit = this.deposit + Number(object.deposit);
          this.paidAmount = this.paidAmount + Number(object.paidAmount);
          this.total = this.total + Number(object.total);
        });
        console.log(this.surcharges);
      }
    });
    console.log(this.data)

  }

  getPrice(price: any) {
    return CommonUtil.formatAmount(price.toString());
  }

  getStatus(status: any) {
    return status === 0 ? 'Chưa thanh toán' : 'Đã thanh toán';
  }

  handlePayment() {
    const request = {
      rentalSlipDetailIdList: this.data,
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

  public readonly CommonUtil = CommonUtil;
}

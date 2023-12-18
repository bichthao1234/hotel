import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {CommonUtil} from "../../../../util/CommonUtil";
import {SweetAlertCustomizeService} from "../../../../services/sweet-alert-customize.service";
import {AuthAdminService} from "../../services/auth-admin.service";
import {RentalSlipService} from "../../services/rental-slip.service";
import {
  DetailManifestConvertReservationsComponent
} from "../detail-manifest-conver-reservations/detail-manifest-convert-reservations.component";
import {CustomerAdminService} from "../../services/customer-admin.service";
import {CustomerAddComponent} from "../customer-add/customer-add.component";

@Component({
  selector: 'app-quick-check-in',
  templateUrl: './quick-check-in.component.html',
  styleUrls: ['./quick-check-in.component.css']
})
export class QuickCheckInComponent implements OnInit {
  today = new Date();
  month = this.today.getMonth();
  year = this.today.getFullYear();
  startDate = new Date(this.year, this.month, this.today.getDate());
  endDate = new Date(this.year, this.month, this.today.getDate() + 1);
  private customerIdList: any[] = [];
  representativeCustomer: any;
  customerList: any;
  customerListFilter: any;

  constructor(private dialogRef: MatDialogRef<QuickCheckInComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private sweetAlertCustomizeService: SweetAlertCustomizeService,
              private authAdminService: AuthAdminService,
              private rentalSlipService: RentalSlipService,
              private dialog: MatDialog,
              private customerAdminService: CustomerAdminService) {
  }

  ngOnInit() {
    this.getAllCustomerList();
  }

  closeDialog() {
    this.dialogRef.close();
  }

  getAllCustomerList() {
    this.customerAdminService.getAllCustomers().subscribe((resp: any) => {
      if (resp) {
        this.customerList = resp;
      }
    });
  }

  getDate(event: any) {
    if (CommonUtil.getRangeDate(new Date(event.start), new Date(event.end)) === 0) {
      this.sweetAlertCustomizeService.showWarringAlertRightPopup("Thời gian lưu trú tối thiểu là 1 ngày");
    } else {
      this.startDate = new Date(event.start);
      this.endDate = new Date(event.end);
    }
  }

  getRangeDate() {
    return CommonUtil.getRangeDate(this.startDate, this.endDate);
  }

  getPrice() {
    return this.data.priceAfterPromotion * this.getRangeDate();
  }

  getFormatDate(price: any) {
    return CommonUtil.formatAmount(price.toString());
  }

  accept() {
    if (this.customerIdList.length < 1) {
      this.sweetAlertCustomizeService
        .showWarringAlertRightPopup(`Bạn chưa thêm khách ở`)
      return;
    }
    if (!this.representativeCustomer?.id) {
      this.sweetAlertCustomizeService
        .showWarringAlertRightPopup(`Bạn chưa thêm khách hàng đại diện`)
      return;
    }
    const quickCheckInRequestDto = {
      employeeId: this.authAdminService.getLoginAdminInfor()?.id,
      startDate: this.startDate,
      endDate: this.endDate,
      roomClassId: Number.parseInt(this.data.roomClassId),
      roomId: this.data.id,
      price: this.data.priceAfterPromotion,
      mainCustomerId: this.representativeCustomer.id,
      customerIdList: this.customerIdList.map((item: any) => {
        return item.id;
      })
    }
    this.rentalSlipService.quickCheckIn(quickCheckInRequestDto).subscribe((resp: any) => {
        if (resp) {
          this.sweetAlertCustomizeService.showSuccessAlertRightPopup(`Nhận phòng ${quickCheckInRequestDto.roomId} thành công!`);
          this.dialogRef.close({status: true});
        } else {
          // this.sweetAlertCustomizeService.showErrorAlertRightPopup(`Nhận phòng ${quickCheckInRequestDto.roomId} thành công!`)
        }
      },
      error => {
        this.sweetAlertCustomizeService.showErrorAlertRightPopup(error.error.error);
      });
  }

  openAddManifest() {
    this.dialog.open(DetailManifestConvertReservationsComponent, {
      data: {
        customerInput: this.customerIdList
      }
    });
  }

  searchRepresentativeCustomer(event: any) {
    let filtered: any[] = [];
    let query = event.query;

    for (let i = 0; i < this.customerList.length; i++) {
      let customer = this.customerList[i];
      if (customer.id.toLowerCase().indexOf(query.toLowerCase()) == 0) {
        filtered.push(customer);
      }
    }
    this.customerListFilter = filtered;
  }

  openAddNewCustomer() {
    let dialog = this.dialog.open(CustomerAddComponent);
    dialog.afterClosed().subscribe((data: any) => {
      console.log(data)
      if (data.status) {
        this.getAllCustomerList();
        this.representativeCustomer = data.customerInfor;
      }
    });
  }
}

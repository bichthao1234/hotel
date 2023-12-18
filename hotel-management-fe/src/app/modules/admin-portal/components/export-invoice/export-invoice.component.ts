import {Component, OnInit} from '@angular/core';
import {InvoiceService} from "../../services/invoice.service";
import {CommonUtil} from "../../../../util/CommonUtil";
import {MatDialog} from "@angular/material/dialog";
import {InvoiceShowPageComponent} from "../invoice-show-page/invoice-show-page.component";
import {GetReservationListRequestDto} from "../../models/GetReservationListRequestDto";
import {FormControl, Validators} from "@angular/forms";
import {ReservationService} from "../../services/reservation.service";
import {Router} from "@angular/router";
import {AuthAdminService} from "../../services/auth-admin.service";
import {SweetAlertCustomizeService} from "../../../../services/sweet-alert-customize.service";
import {DetailReservationsComponent} from "../detail-reservations/detail-reservations.component";
import {ReservationEditRangeDateComponent} from "../reservation-edit-range-date/reservation-edit-range-date.component";
import {RouterFEConstant} from "../../../../constant/RouterFEConstant";
import {RentalSlipService} from "../../services/rental-slip.service";
import {PaymentDialogComponent} from "../paymen-dialog/payment-dialog.component";
import {ExportInvoiceDialogComponent} from "../export-invoice-dialog/export-invoice-dialog.component";
import {CustomerAdminService} from "../../services/customer-admin.service";

@Component({
  selector: 'app-export-invoice',
  templateUrl: './export-invoice.component.html',
  styleUrls: ['./export-invoice.component.css']
})
export class ExportInvoiceComponent implements OnInit {
  rentalSlips: any;
  customerId = new FormControl(null, [Validators.required]);
  isBtnCustomerId = false;
  chooseCustomer: any;
  customerListFilter: any;
  customerList: any;

  constructor(private rentalSlipServiceService: RentalSlipService,
              private customerAdminService: CustomerAdminService,
              private router: Router,
              public dialog: MatDialog) {
  }

  ngOnInit() {
    this.customerId.valueChanges.subscribe(() => {
      if (this.customerId.value === '') {
        this.customerId.setValue(null);
        this.isBtnCustomerId = false;
      }
    });
    this.getAllRentalSlip();
    this.getAllCustomerList();
  }

  getAllRentalSlip() {
    let customerId = this.chooseCustomer ? this.chooseCustomer.id : null;
    this.rentalSlipServiceService.getAlRentalSlipForExportInvoice(customerId).subscribe((resp) => {
      if (resp) {
        this.rentalSlips = resp;
        console.log(this.rentalSlips);
      }
    });
  }

  getAllCustomerList() {
    this.customerAdminService.getAllCustomers().subscribe((resp: any) => {
      if (resp) {
        this.customerList = resp;
      }
    });
  }

  searchReservationCustomId() {
    this.isBtnCustomerId = true;
    if (this.customerId.valid) {
      this.getAllRentalSlip();
    }
  }

  getPrice(price: any) {
    return price ? CommonUtil.formatAmount(price.toString()) : 0;
  }

  getClassOfRoomStatus(room: any, rentalSlipStatus: boolean) {
    if (rentalSlipStatus) {
      return 'status-available-room';
    }
    switch (room.roomStatus.id) {
      case 'RS00001': {
        return 'status-available-room';
      }
      case 'RS00002': {
        return 'status-rented';
      }
      case 'RS00003': {
        return 'status-overtime'
      }
      case 'RS00004': {
        return 'status-cancel'
      }
      default: {
        return '';
      }
    }
  }

  openDialog(rentalSlip: any) {
    this.dialog.open(ExportInvoiceDialogComponent, {
      data: {
        rentalSlipDetailIds: rentalSlip.detailIdList,
        isForPayment: false,
        isExportInvoice: true
      },
      disableClose: true})
  }

  navigateToCommonRentalSlip(rentalSlip: any) {
    this.router.navigate(
      [RouterFEConstant.ADMIN_PORTAL.path,
        RouterFEConstant.ADMIN_PORTAL_COMMON_RENTAL_SLIP.path,
        rentalSlip.id]
    ).then(() => {
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

  handleInput(event: any) {
    const value = event.target.value;
    if (!/^\d*$/.test(value)) {
      event.target.value = value.replace(/[^0-9]/g, '');
    }
  }

  resetCustomer() {
    this.chooseCustomer = null;
    this.getAllRentalSlip();
  }
}

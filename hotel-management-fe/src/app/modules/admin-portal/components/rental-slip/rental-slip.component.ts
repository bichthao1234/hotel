import {Component, OnInit} from '@angular/core';
import {RentalSlipService} from "../../services/rental-slip.service";
import {CommonUtil} from "../../../../util/CommonUtil";
import {Router} from "@angular/router";
import {RouterFEConstant} from "../../../../constant/RouterFEConstant";
import {PaymentDialogComponent} from "../paymen-dialog/payment-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {CustomerAdminService} from "../../services/customer-admin.service";

@Component({
  selector: 'app-rental-slip',
  templateUrl: './rental-slip.component.html',
  styleUrls: ['./rental-slip.component.css']
})
export class RentalSlipComponent implements OnInit {
  rentalSlips: any;
  chooseCustomer: any;
  customerListFilter: any;
  customerList: any;

  constructor(private rentalSlipServiceService: RentalSlipService,
              private router: Router,
              public dialog: MatDialog,
              private customerAdminService: CustomerAdminService) {
  }

  ngOnInit() {
    this.getAllRentalSlip();
    this.getAllCustomerList();
  }
  getAllCustomerList() {
    this.customerAdminService.getAllCustomers().subscribe((resp: any) => {
      if (resp) {
        this.customerList = resp;
      }
    });
  }
  getAllRentalSlip() {
    console.log(this.chooseCustomer)
    let customerId = {customerId: this.chooseCustomer?.id ?? null};
    console.log(customerId)
    this.rentalSlipServiceService.getAlRentalSlip(customerId).subscribe((resp) => {
      if (resp) {
        this.rentalSlips = resp;
        console.log(this.rentalSlips);
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
  getPrice(price: any) {
    return price ? CommonUtil.formatAmount(price.toString()) : null;
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
    this.dialog.open(PaymentDialogComponent, {data: rentalSlip.detailIdList, disableClose: true})
  }

  navigateToCommonRentalSlip(rentalSlip: any) {
    this.router.navigate(
      [RouterFEConstant.ADMIN_PORTAL.path,
        RouterFEConstant.ADMIN_PORTAL_COMMON_RENTAL_SLIP.path,
        rentalSlip.id]
    ).then(() => {
    });
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

import {Component, Inject, OnInit} from '@angular/core';
import {CustomerAdminService} from "../../services/customer-admin.service";
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {CustomerAddComponent} from "../customer-add/customer-add.component";

@Component({
  selector: 'app-detail-manifest-convert-reservations',
  templateUrl: './detail-manifest-convert-reservations.component.html',
  styleUrls: ['./detail-manifest-convert-reservations.component.css']
})
export class DetailManifestConvertReservationsComponent implements OnInit {
  customerList: any;
  addCustomerList: any[] = [];
  customerInput: any;
  orderRooms: any;

  constructor(private customerAdminService: CustomerAdminService,
              private dialogRef: MatDialogRef<DetailManifestConvertReservationsComponent>,
              private dialog: MatDialog,
              @Inject(MAT_DIALOG_DATA) public data: any) {
  }

  ngOnInit() {
    this.customerInput = this.data.customerInput;
    this.orderRooms = this.data.orderRooms;
    if (this.customerInput) {
      this.addCustomerList = this.customerInput;
    }
    this.getCustomerList();
  }

  getCustomerList() {
    this.customerAdminService.getAllCustomers().subscribe((resp) => {
      if (resp) {
        console.log(resp);
        this.customerList = resp;
      }
    });
  }

  selectCustomer(customer: any) {
    const index = this.addCustomerList.findIndex((item: any) => {
      return item.id === customer.id;
    });
    if (index !== -1) {
      this.addCustomerList.splice(index, 1);
    } else {
      this.addCustomerList.push(customer);
    }
  }

  closeDialog() {
    this.dialogRef.close();
  }

  removeCustomerAdd(customerAddItem: any) {
    const index = this.addCustomerList.findIndex((item: any) => {
      return item.id === customerAddItem.id;
    });
    this.addCustomerList.splice(index, 1);
  }

  addNewCustomerDialog() {
    let dialog = this.dialog.open(CustomerAddComponent);
    dialog.afterClosed().subscribe((resp) => {
      console.log(resp);
      if (resp?.status) {
        this.getCustomerList();
      }
    })
  }

  acceptCustomerList() {
    this.dialogRef.close({customerList: this.addCustomerList});
  }
}

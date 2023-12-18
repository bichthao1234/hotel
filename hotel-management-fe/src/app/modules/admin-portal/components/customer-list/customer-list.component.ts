import {Component, Input, OnInit} from '@angular/core';
import {CustomerAdminService} from "../../services/customer-admin.service";
import {MatTableDataSource} from "@angular/material/table";
import {Customer} from "../../models/customer";
import {AuthAdminService} from "../../services/auth-admin.service";
import {AdminInforLogin} from "../../models/AdminInforLogin";

@Component({
  selector: 'app-customer-list',
  templateUrl: './customer-list.component.html',
  styleUrls: ['./customer-list.component.css']
})
export class CustomerListComponent implements OnInit {

  @Input() customers!: Customer[];
  adminInforLogin: AdminInforLogin | null | undefined;
  displayedColumns: string[] = ['id', 'firstName', 'lastName', 'address', 'phoneNumber', 'email', 'taxNumber'];
  columnHeaders: { [key: string]: string } = {
    id: 'Customer ID',
    firstName: 'First Name',
    lastName: 'Last Name',
    address: 'Address',
    phoneNumber: 'Phone Number',
    email: 'Email',
    taxNumber: 'Tax Number',
  };
  dataSource!: MatTableDataSource<Customer>;

  constructor(private authAdminService:AuthAdminService) { }

  ngOnInit() {
    this.adminInforLogin = this.authAdminService.getLoginAdminInfor();
    // this.getAllCustomer(this.token);
    this.dataSource = new MatTableDataSource(this.customers);
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

}

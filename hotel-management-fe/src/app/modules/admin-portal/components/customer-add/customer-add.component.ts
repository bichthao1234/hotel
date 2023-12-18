import {Component, OnInit} from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {CustomerAdminService} from "../../services/customer-admin.service";
import {SweetAlertCustomizeService} from "../../../../services/sweet-alert-customize.service";

@Component({
  selector: 'app-customer-add',
  templateUrl: './customer-add.component.html',
  styleUrls: ['./customer-add.component.css']
})
export class CustomerAddComponent implements OnInit {
  customerForm: FormGroup = new FormGroup({
    id: new FormControl('', [Validators.required]),
    firstName: new FormControl('', [Validators.required]),
    lastName: new FormControl('', [Validators.required]),
    email: new FormControl(''),
    phoneNumber: new FormControl(''),
    taxNumber: new FormControl(''),
    address: new FormControl(''),
  });
  isSubmit = false;

  constructor(private dialogRef: MatDialogRef<CustomerAddComponent>,
              private customerAdminService: CustomerAdminService,
              private sweetAlertCustomizeService: SweetAlertCustomizeService) {
  }

  ngOnInit() {
  }

  initialCustomerForm() {
    this.customerForm = new FormGroup({
      id: new FormControl('', [Validators.required]),
      firstName: new FormControl('', [Validators.required]),
      lastName: new FormControl('', [Validators.required]),
      email: new FormControl(''),
      phoneNumber: new FormControl(''),
      taxNumber: new FormControl(''),
      address: new FormControl(''),
    });
  }

  close() {
    this.dialogRef.close();
  }

  saveCustomer() {
    this.isSubmit = true;
    if (this.customerForm.valid) {
      this.customerAdminService.createCustomer(this.customerForm.getRawValue()).subscribe((resp) => {
          if (resp) {
            this.sweetAlertCustomizeService.showSuccessAlertRightPopup("Thêm khách hàng thành công!");
            this.dialogRef.close({status: true,
            customerInfor: this.customerForm.getRawValue()});
          }
        },
        (error) => {
          console.log(error);
        });
    }
  }

}

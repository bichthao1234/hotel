import {Component, OnInit} from '@angular/core';
import {DataBindingService} from "../../../../services/data-binding.service";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {CustomerAdminService} from "../../../admin-portal/services/customer-admin.service";
import {RoomClassificationService} from "../../services/room-classification.service";
import {SweetAlertCustomizeService} from "../../../../services/sweet-alert-customize.service";
import {CommonUtil} from "../../../../util/CommonUtil";
import {PaymentService} from "../../services/payment.service";
import {MatDialog} from "@angular/material/dialog";
import {ModalPaymentComponent} from "../modal-payment/modal-payment.component";

@Component({
  selector: 'app-payment-page',
  templateUrl: './payment-page.component.html',
  styleUrls: ['./payment-page.component.css']
})
export class PaymentPageComponent implements OnInit {
  detailRoomNums!: any[];
  totalPrice: any;
  customerForm: FormGroup = new FormGroup({});
  customer: any;
  isCheck = false;
  isClickCheck = false;
  checkInOutDate: any;
  startDate!: Date;
  endDate!: Date;
  depositsForm: FormControl = new FormControl();

  constructor(private dataBindingService: DataBindingService,
              private customerAdminService: CustomerAdminService,
              private roomClassificationService: RoomClassificationService,
              private sweetAlertCustomizeService: SweetAlertCustomizeService,
              private paymentService: PaymentService,
              private dialog: MatDialog) {
  }

  ngOnInit() {
    this.dataBindingService.sendData({isMainPage: true});
    const deNumJson = sessionStorage.getItem('detailRoomNums');
    if (deNumJson) {
      this.detailRoomNums = JSON.parse(deNumJson);
    }
    this.totalPrice = sessionStorage.getItem('totalPrice');
    this.checkInOutDate = sessionStorage.getItem('checkInOutDate')
    const sD = sessionStorage.getItem('startDate');
    if (sD) {
      this.startDate = new Date(sD);
    }
    const eD = sessionStorage.getItem('endDate');
    if (eD) {
      this.endDate = new Date(eD);
    }
    this.initialCustomerForm();
    this.initialDeposits();
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
    })
  }

  initialCustomerFormThenCheckFalse(id: any) {
    this.customerForm = new FormGroup({
      id: new FormControl(id, [Validators.required]),
      firstName: new FormControl('', [Validators.required]),
      lastName: new FormControl('', [Validators.required]),
      email: new FormControl(''),
      phoneNumber: new FormControl(''),
      taxNumber: new FormControl(''),
      address: new FormControl(''),
    })
  }

  initialCustomerFormThenCheckTrue(customer: any) {
    this.customerForm = new FormGroup({
      id: new FormControl(customer.id, [Validators.required]),
      firstName: new FormControl({value: customer.firstName, disabled: true}, [Validators.required]),
      lastName: new FormControl({value: customer.lastName, disabled: true}, [Validators.required]),
      email: new FormControl({value: customer.email, disabled: true}),
      phoneNumber: new FormControl({value: customer.phoneNumber, disabled: true}),
      taxNumber: new FormControl({value: customer.taxNumber, disabled: true}),
      address: new FormControl({value: customer.address, disabled: true}),
    });
  }

  checkExistCustomer() {
    const id = this.customerForm.get('id')?.value;
    if (id) {
      this.isClickCheck = true;
      this.customerAdminService.checkCustomerWithId(id).subscribe((resp) => {
        if (resp) {
          this.customer = resp;
          this.isCheck = true;
          this.initialCustomerFormThenCheckTrue(this.customer);
        } else {
          this.isCheck = false;
          this.initialCustomerFormThenCheckFalse(id);
        }
      });
    }
  }

  makeReservation() {
    if (this.customerForm.valid && this.depositsForm.valid) {
      const customerValue = this.customerForm.getRawValue();
      const req = {
        customerId: customerValue.id,
        firstName: customerValue.firstName,
        lastName: customerValue.lastName,
        address: customerValue.address ?? null,
        phoneNumber: customerValue.phoneNumber ?? null,
        email: customerValue.email ?? null,
        taxNumber: customerValue.taxNumber ?? null,
        deposit: this.depositsForm.value,
        startDate: this.startDate ?? null,
        endDate: this.endDate ?? null,
        roomClassList: this.getRoomClassList(),
        numberOfRoomsList: this.getNumOfRoomList(),
      }
      this.dialog.open(ModalPaymentComponent, {data: {req: req, customerValue: customerValue, detailRoomNums: this.detailRoomNums}});
    }
  }

  getMinDeposit() {
    return (this.totalPrice as number) * 0.2;
  }

  getRoomClassList() {
    let rCl = [];
    for (let i of this.detailRoomNums) {
      rCl.push(i.roomClassificationId.id);
    }
    return rCl;
  }

  getNumOfRoomList() {
    let nOR = [];
    for (let i of this.detailRoomNums) {
      nOR.push(i.numOfRoom);
    }
    return nOR;
  }

  initialDeposits() {
    const minDeposits = this.getMinDeposit();
    this.depositsForm = new FormControl(minDeposits, [Validators.required, Validators.max(this.totalPrice), Validators.min(minDeposits)]);
  }

  removeFormat() {
    this.depositsForm.setValue(CommonUtil.removeFormat(this.depositsForm.value));
  }

  formatAmount() {
    this.depositsForm.setValue(CommonUtil.formatAmount(this.depositsForm.value));
  }
  getPrice(amount: any) {
    return CommonUtil.formatAmount(amount.toString());
  }
}

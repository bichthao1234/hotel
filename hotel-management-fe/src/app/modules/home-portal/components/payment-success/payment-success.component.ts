import {Component, OnDestroy, OnInit} from '@angular/core';
import {DataBindingService} from "../../../../services/data-binding.service";
import {Router} from "@angular/router";
import {CommonUtil} from "../../../../util/CommonUtil";

@Component({
  selector: 'app-payment-success',
  templateUrl: './payment-success.component.html',
  styleUrls: ['./payment-success.component.css']
})
export class PaymentSuccessComponent implements OnInit, OnDestroy {
  checkInOutDate: any;
  detailRoomNums: any;
  totalPrice: any;
  dataSuccess: any;
  customerValue: any;
  deposit: any;

  constructor(private dataBindingService: DataBindingService,
              private router: Router) {
  }

  ngOnInit() {
    let data = sessionStorage.getItem('dataSuccess');
    this.dataSuccess = data ? JSON.parse(data) : null;
    if (this.dataSuccess) {
      console.log(this.dataSuccess)
      this.detailRoomNums = this.dataSuccess.detailRoomNums;
      this.customerValue = this.dataSuccess.customerValue;
      this.deposit = this.dataSuccess.req.deposit;
    } else {
      this.router.navigate(['not-found'])
    }

    this.checkInOutDate = sessionStorage.getItem('checkInOutDate');
    this.totalPrice = sessionStorage.getItem('totalPrice');
    this.dataBindingService.sendData({isMainPage: true});
  }

  goToSearchPage() {
    this.router.navigate(['/search']).then(r => {
    });
  }

  getPrice(amount: any) {
    return CommonUtil.formatAmount(amount.toString());
  }

  ngOnDestroy() {
    sessionStorage.removeItem('dataSuccess');
  }

  getCheckInDate() {
    return this.dataSuccess ? new Date(this.dataSuccess.req.startDate) : null;
  }
}

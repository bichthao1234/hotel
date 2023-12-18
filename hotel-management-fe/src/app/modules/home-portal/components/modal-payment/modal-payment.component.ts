import {Component, Inject, OnInit} from '@angular/core';
import {ICreateOrderRequest, IPayPalConfig} from "ngx-paypal";
import {CommonUtil} from "../../../../util/CommonUtil";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {PaymentService} from "../../services/payment.service";
import {RoomClassificationService} from "../../services/room-classification.service";
import {SweetAlertCustomizeService} from "../../../../services/sweet-alert-customize.service";
import {ALERT_CONTENT} from "../../../../constant/ALERT_CONTENT";
import {Router} from "@angular/router";
import {DataBindingService} from "../../../../services/data-binding.service";

@Component({
  selector: 'app-modal-payment',
  templateUrl: './modal-payment.component.html',
  styleUrls: ['./modal-payment.component.css']
})
export class ModalPaymentComponent implements OnInit {

  payPalConfig?: IPayPalConfig;
  amount: any;
  usdRate: any;


  constructor(public dialogRef: MatDialogRef<ModalPaymentComponent>,
              private paymentService: PaymentService,
              private roomClassificationService: RoomClassificationService,
              private sweetAlertCustomizeService: SweetAlertCustomizeService,
              private router: Router,
              private dataBindingService: DataBindingService,
              @Inject(MAT_DIALOG_DATA) public data: any) {
  }

  ngOnInit() {
    // Account sandbox
    //email: sb-zevyp27847392@personal.example.com
    //password: IdK7_M;B
    console.log(this.data);
    this.getExchangeRate();
    this.initConfig();
  }

  initConfig() {
    this.payPalConfig = {
      advanced: {
        commit: 'true',
      },
      clientId: String(CommonUtil.CLIENT_ID),
      createOrderOnClient: (data) =>
        <ICreateOrderRequest>{
          intent: 'CAPTURE',
          purchase_units: [
            {
              reference_id: 'default',
              amount: {
                currency_code: 'USD',
                value: this.amount,
                breakdown: {
                  item_total: {
                    currency_code: 'USD',
                    value: this.amount
                  }
                }
              },
              items: [
                {
                  name: "NAME",
                  quantity: '1',
                  unit_amount: {
                    currency_code: 'USD',
                    value: this.amount,
                  },
                },
              ],
            },
          ],
        },
      currency: 'USD',
      onApprove: (data, actions) => {
        console.log(
          'onApprove - transaction was approved, but not authorized',
          data,
          actions
        );
        actions.order.get().then((details: any) => {
          console.log(
            'onApprove - you can get full order details inside onApprove: ',
            details
          );
        });
      },
      onCancel: (data, actions) => {
        console.log('OnCancel', data, actions);
        // this.showCancel = true;
      },
      onClick: (data, actions) => {
        console.log('onClick', data, actions);
        // this.resetStatus();
      },
      onClientAuthorization: (data) => {
        console.log(
          'onClientAuthorization - you should probably inform your server about completed transaction at this point',
          data
        );
        if (data.status === CommonUtil.COMPLETE_PAY_STATUS) {
          setTimeout(() => {
            this.roomClassificationService.makeReservation(this.data.req).subscribe((resp) => {
                if (resp && ('result' in resp) && resp.result) {
                  this.dialogRef.close();
                  this.sweetAlertCustomizeService.showSuccessAlertRightPopup(ALERT_CONTENT.MAKE_RESERVATION_SUCCESSFULLY);
                  sessionStorage.setItem('dataSuccess', JSON.stringify(this.data));
                  this.router.navigate(['/payment-success']).then(() => {
                  });
                }
              },
              (error) => {
                this.sweetAlertCustomizeService.showErrorAlertRightPopup(error.error.error);
              });
          });
        }
        // this.showSuccess = true;
      },
      onError: (err) => {
        console.log('OnError', err);
        // this.showError = true;
      },
      style: {
        label: 'paypal',
        layout: 'vertical',
      },
    };
  }

  getExchangeRate() {
    this.paymentService.getExchangeRate().subscribe((resp) => {
      this.usdRate = resp.items.find((item: any) => item.type === 'USD');
      const rate = Number(this.usdRate.banck);
      this.amount = parseFloat((this.data.req.deposit / rate).toFixed(2));
    });
  }
}

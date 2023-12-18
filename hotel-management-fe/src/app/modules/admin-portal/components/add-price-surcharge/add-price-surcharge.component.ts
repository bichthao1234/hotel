import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {RoomTypeService} from "../../services/room-type.service";
import {SweetAlertCustomizeService} from "../../../../services/sweet-alert-customize.service";
import {DataBindingService} from "../../../../services/data-binding.service";
import {ALERT_CONTENT} from "../../../../constant/ALERT_CONTENT";
import {RoomClassificationService} from "../../../home-portal/services/room-classification.service";
import {AuthAdminService} from "../../services/auth-admin.service";
import {PriceRoomClassDialogComponent} from "../price-room-class-dialog/price-room-class-dialog.component";
import {ServiceService} from "../../services/service.service";
import {PriceServiceDialogComponent} from "../price-service-dialog/price-service-dialog.component";
import {SurchargeService} from "../../services/surcharge.service";
import {PriceSurchargeDialogComponent} from "../price-surcharge-dialog/price-surcharge-dialog.component";

@Component({
  selector: 'add-price-surcharge',
  templateUrl: './add-price-surcharge.component.html',
  styleUrls: ['./add-price-surcharge.component.css']
})
export class AddPriceSurchargeComponent implements OnInit {
  isSubmit = false;
  priceRoomClassForm = new FormGroup({
    price: new FormControl('', [Validators.required]),
    appliedDate: new FormControl('', [Validators.required])
  });

  constructor(public dialogRef: MatDialogRef<AddPriceSurchargeComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private dialog: MatDialog,
              private surchargeService: SurchargeService,
              private sweetAlertCustomizeService: SweetAlertCustomizeService,
              private dataBindingService: DataBindingService,
              private authAdminService: AuthAdminService,
              ) {
  }

  ngOnInit() {
    console.log(this.data)
  }

  getSurchargeWithId() {
    this.surchargeService.getSurchargeWithId(this.data.id).subscribe((resp => {
      if (resp) {
        this.data = resp;
      }
    }))
  }

  cancel() {
    this.dialogRef.close();
    this.dialog.open(PriceServiceDialogComponent, {
      data: this.data,
      disableClose: false
    });
  }

  save() {
    this.isSubmit = true;
    if (this.priceRoomClassForm.valid) {
      const request = {
        price: this.priceRoomClassForm.getRawValue().price,
        appliedDate: this.priceRoomClassForm.getRawValue().appliedDate,
        createdBy: this.authAdminService.getLoginAdminInfor()?.id,
        surchargeId: this.data.id,
      };
      console.log(request)
      this.surchargeService.addNewPrice(request).subscribe((resp) => {
          if (resp && resp.result) {
            this.getSurchargeWithId();
            this.dataBindingService.sendData({
              priceSurchargeAdd: true
            });
            this.sweetAlertCustomizeService.showSuccessAlertRightPopup(ALERT_CONTENT.ADD_PRICE_SURCHARGE_SUCCESSFUL);
            this.dialogRef.close();
            this.dialog.open(PriceSurchargeDialogComponent, {
              data: this.data,
              disableClose: false
            });
          }
        },
        (error) => {
          this.sweetAlertCustomizeService.showErrorAlertRightPopup(error.error);
        });
    }
  }
}

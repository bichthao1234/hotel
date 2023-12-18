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

@Component({
  selector: 'add-price-service',
  templateUrl: './add-price-service.component.html',
  styleUrls: ['./add-price-service.component.css']
})
export class AddPriceServiceComponent implements OnInit {
  isSubmit = false;
  priceRoomClassForm = new FormGroup({
    price: new FormControl('', [Validators.required]),
    appliedDate: new FormControl('', [Validators.required])
  });

  constructor(public dialogRef: MatDialogRef<AddPriceServiceComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private dialog: MatDialog,
              private serviceService: ServiceService,
              private sweetAlertCustomizeService: SweetAlertCustomizeService,
              private dataBindingService: DataBindingService,
              private authAdminService: AuthAdminService,
              ) {
  }

  ngOnInit() {
    console.log(this.data)
  }

  getServiceWithId() {
    this.serviceService.getServiceWithId(this.data.id).subscribe((resp => {
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
        serviceId: this.data.id,
      };
      console.log(request)
      this.serviceService.addNewPrice(request).subscribe((resp) => {
          if (resp && resp.result) {
            this.getServiceWithId();
            this.dataBindingService.sendData({
              priceServiceAdd: true
            });
            this.sweetAlertCustomizeService.showSuccessAlertRightPopup(ALERT_CONTENT.ADD_PRICE_SERVICE_SUCCESSFUL);
            this.dialogRef.close();
            this.dialog.open(PriceServiceDialogComponent, {
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

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

@Component({
  selector: 'add-price-room-class',
  templateUrl: './add-price-room-class.component.html',
  styleUrls: ['./add-price-room-class.component.css']
})
export class AddPriceRoomClassComponent implements OnInit {
  isSubmit = false;
  priceRoomClassForm = new FormGroup({
    price: new FormControl('', [Validators.required]),
    appliedDate: new FormControl('', [Validators.required])
  });

  constructor(public dialogRef: MatDialogRef<AddPriceRoomClassComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private dialog: MatDialog,
              private roomClassificationService: RoomClassificationService,
              private sweetAlertCustomizeService: SweetAlertCustomizeService,
              private dataBindingService: DataBindingService,
              private authAdminService: AuthAdminService,
              ) {
  }

  ngOnInit() {
    console.log(this.data)
  }

  getRoomClassificationWithId() {
    this.roomClassificationService.getRoomClassificationWithId(this.data.id).subscribe((resp => {
      if (resp) {
        this.data = resp;
      }
    }))
  }

  cancel() {
    this.dialogRef.close();
    this.dialog.open(PriceRoomClassDialogComponent, {
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
        roomClassId: this.data.id,
        createdDate: new Date(),
      };
      console.log(request)
      this.roomClassificationService.addNewPrice(request).subscribe((resp) => {
          if (resp && resp.result) {
            this.getRoomClassificationWithId();
            this.dataBindingService.sendData({
              priceRoomClassAdd: true
            });
            this.sweetAlertCustomizeService.showSuccessAlertRightPopup(ALERT_CONTENT.ADD_PRICE_ROOM_CLASS_SUCCESSFUL);
            this.dialogRef.close();
            this.dialog.open(PriceRoomClassDialogComponent, {
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

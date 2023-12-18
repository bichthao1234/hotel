import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";
import {SweetAlertCustomizeService} from "../../../../services/sweet-alert-customize.service";
import {RoomTypeService} from "../../services/room-type.service";
import {RoomKindService} from "../../services/room-kind.service";
import {ConvenienceService} from "../../services/convenience.service";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {AuthAdminService} from "../../services/auth-admin.service";
import {RoomClassificationService} from "../../../home-portal/services/room-classification.service";
import {DataBindingService} from "../../../../services/data-binding.service";
import {ALERT_CONTENT} from "../../../../constant/ALERT_CONTENT";
import {ServiceService} from "../../services/service.service";
import {SurchargeService} from "../../services/surcharge.service";

@Component({
  selector: 'app-add-surcharge',
  templateUrl: './add-surcharge.component.html',
  styleUrls: ['./add-surcharge.component.css']
})
export class AddSurchargeComponent implements OnInit {
  isSubmit = false;
  surchargeForm = new FormGroup({
    name: new FormControl('', [Validators.required]),
    description: new FormControl('', [Validators.required]),
    price: new FormControl('', [Validators.required]),
  });

  constructor(public dialogRef: MatDialogRef<AddSurchargeComponent>,
              private surchargeService: SurchargeService,
              private sweetAlertCustomizeService: SweetAlertCustomizeService,
              private dataBindingService: DataBindingService,
              private authAdminService: AuthAdminService
              ) {
  }

  ngOnInit() {
  }

  cancel() {
    this.dialogRef.close();
  }

  save() {
    this.isSubmit = true;
    if (this.surchargeForm.valid) {
      const request = {
        name: this.surchargeForm.getRawValue().name,
        description: this.surchargeForm.getRawValue().description,
        priceSurcharge: this.surchargeForm.getRawValue().price,
        createdBy: this.authAdminService.getLoginAdminInfor()?.id,
      };
      this.surchargeService.saveSurcharge(request).subscribe((resp) => {
          if (resp && resp.result) {
            this.dataBindingService.sendData({
              surchargeSaveStatus: true
            });
            this.cancel();
            this.sweetAlertCustomizeService.showSuccessAlertRightPopup(ALERT_CONTENT.ADD_SURCHARGE_SUCCESSFULLY);
          }
        },
        (error) => {
          this.sweetAlertCustomizeService.showErrorAlertRightPopup(error.error);
        });
    }
  }
}

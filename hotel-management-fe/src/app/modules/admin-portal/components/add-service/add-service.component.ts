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

@Component({
  selector: 'app-add-service',
  templateUrl: './add-service.component.html',
  styleUrls: ['./add-service.component.css']
})
export class AddServiceComponent implements OnInit {
  isSubmit = false;
  serviceForm = new FormGroup({
    name: new FormControl('', [Validators.required]),
    unit: new FormControl('', [Validators.required]),
    description: new FormControl('', [Validators.required]),
    price: new FormControl('', [Validators.required]),
  });

  constructor(public dialogRef: MatDialogRef<AddServiceComponent>,
              private serviceService: ServiceService,
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
    if (this.serviceForm.valid) {
      const request = {
        name: this.serviceForm.getRawValue().name,
        unit: this.serviceForm.getRawValue().unit,
        description: this.serviceForm.getRawValue().description,
        priceService: this.serviceForm.getRawValue().price,
        createdBy: this.authAdminService.getLoginAdminInfor()?.id,
      };
      this.serviceService.saveService(request).subscribe((resp) => {
          if (resp && resp.result) {
            this.dataBindingService.sendData({
              serviceSaveStatus: true
            });
            this.cancel();
            this.sweetAlertCustomizeService.showSuccessAlertRightPopup(ALERT_CONTENT.ADD_SERVICE_SUCCESSFULLY);
          }
        },
        (error) => {
          this.sweetAlertCustomizeService.showErrorAlertRightPopup(error.error);
        });
    }
  }
}

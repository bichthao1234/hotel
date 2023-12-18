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
import {CommonUtil} from "../../../../util/CommonUtil";
import {PromotionService} from "../../services/promotion.service";

@Component({
  selector: 'app-add-promotion',
  templateUrl: './add-promotion.component.html',
  styleUrls: ['./add-promotion.component.css']
})
export class AddPromotionComponent implements OnInit {
  isSubmit = false;
  promotionForm = new FormGroup({
    description: new FormControl('', [Validators.required]),
  });
  startDate: any;
  endDate: any;

  constructor(public dialogRef: MatDialogRef<AddPromotionComponent>,
              private promotionService: PromotionService,
              private sweetAlertCustomizeService: SweetAlertCustomizeService,
              private dataBindingService: DataBindingService,
              private authAdminService: AuthAdminService
              ) {
  }

  ngOnInit() {
  }

  getDate(event: any) {
    if (CommonUtil.getRangeDate(new Date(event.start), new Date(event.end)) === 0) {
      this.sweetAlertCustomizeService.showWarringAlertRightPopup("Thời gian tối thiểu là 1 ngày");
    } else {
      this.startDate = new Date(event.start);
      this.endDate = new Date(event.end);
    }
  }

  cancel() {
    this.dialogRef.close();
  }

  save() {
    this.isSubmit = true;
    if (this.promotionForm.valid) {
      const request = {
        description: this.promotionForm.getRawValue().description,
        startDate: this.startDate,
        endDate: this.endDate,
      };
      console.log(request)
      this.promotionService.saveService(request).subscribe((resp) => {
          if (resp && resp.result) {
            this.dataBindingService.sendData({
              promotionSaveStatus: true
            });
            this.cancel();
            this.sweetAlertCustomizeService.showSuccessAlertRightPopup(ALERT_CONTENT.ADD_PROMOTION_SUCCESSFULLY);
          }
        },
        (error) => {
          this.sweetAlertCustomizeService.showErrorAlertRightPopup(error.error);
        });
    }
  }
}

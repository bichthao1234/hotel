import {Component, Inject, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {RoomTypeService} from "../../services/room-type.service";
import {SweetAlertCustomizeService} from "../../../../services/sweet-alert-customize.service";
import {DataBindingService} from "../../../../services/data-binding.service";
import {ALERT_CONTENT} from "../../../../constant/ALERT_CONTENT";
import {ServiceService} from "../../services/service.service";
import {CommonUtil} from "../../../../util/CommonUtil";
import {PromotionService} from "../../services/promotion.service";

@Component({
  selector: 'app-edit-promotion',
  templateUrl: './edit-promotion.component.html',
  styleUrls: ['./edit-promotion.component.css']
})
export class EditPromotionComponent implements OnInit {
  promotionForm = new FormGroup(
    {
      id: new FormControl('', [Validators.required]),
      description: new FormControl('', [Validators.required]),
    }
  );
  isSubmit = false;
  isChange = false;
  startDate: any;
  endDate: any;

  constructor(public dialogRef: MatDialogRef<EditPromotionComponent>,
              private promotionService: PromotionService,
              private sweetAlertCustomizeService: SweetAlertCustomizeService,
              private dataBindingService: DataBindingService,
              @Inject(MAT_DIALOG_DATA) public data: any) {
  }

  ngOnInit() {
    this.initialEditForm();
    this.promotionForm.get('description')?.valueChanges.subscribe(value => {
      this.isChange = value !== this.data.description;
    });
  }

  getDate(event: any) {
    if (CommonUtil.getRangeDate(new Date(event.start), new Date(event.end)) === 0) {
      this.sweetAlertCustomizeService.showWarringAlertRightPopup("Thời gian tối thiểu là 1 ngày");
    } else {
      this.startDate = new Date(event.start);
      this.endDate = new Date(event.end);
      this.isChange = this.startDate !== this.data.startDate;
      this.isChange = this.endDate !== this.data.endDate;
    }
  }

  initialEditForm() {
    if(this.data) {
      this.promotionForm = new FormGroup(
        {
          id: new FormControl({value: this.data.id, disabled: true}, [Validators.required]),
          description: new FormControl(this.data.description, [Validators.required]),
        }
      );
      this.startDate = this.data.startDate;
      this.endDate = this.data.endDate;
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
      this.promotionService.editService(request).subscribe((resp) => {
          if (resp && resp.result) {
            this.dataBindingService.sendData({
              promotionEditStatus: true
            });
            this.sweetAlertCustomizeService.showSuccessAlertRightPopup(ALERT_CONTENT.EDIT_PROMOTION_SUCCESSFULLY);
            this.dialogRef.close()
          }
        },
        (error) => {
          this.sweetAlertCustomizeService.showErrorAlertRightPopup(error.error);
        });
    }
  }
}

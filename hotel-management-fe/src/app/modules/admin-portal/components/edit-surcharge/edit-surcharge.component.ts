import {Component, Inject, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {RoomTypeService} from "../../services/room-type.service";
import {SweetAlertCustomizeService} from "../../../../services/sweet-alert-customize.service";
import {DataBindingService} from "../../../../services/data-binding.service";
import {ALERT_CONTENT} from "../../../../constant/ALERT_CONTENT";
import {ServiceService} from "../../services/service.service";
import {SurchargeService} from "../../services/surcharge.service";

@Component({
  selector: 'app-edit-surcharge',
  templateUrl: './edit-surcharge.component.html',
  styleUrls: ['./edit-surcharge.component.css']
})
export class EditSurchargeComponent implements OnInit {
  surchargeFormGroup = new FormGroup(
    {
      id: new FormControl('', [Validators.required]),
      name: new FormControl('', [Validators.required]),
      description: new FormControl('', [Validators.required]),
    }
  );
  isSubmit = false;
  isChange = false;

  constructor(public dialogRef: MatDialogRef<EditSurchargeComponent>,
              private surchargeService: SurchargeService,
              private sweetAlertCustomizeService: SweetAlertCustomizeService,
              private dataBindingService: DataBindingService,
              @Inject(MAT_DIALOG_DATA) public data: any) {
  }

  ngOnInit() {
    this.initialEditForm();
    this.surchargeFormGroup.get('name')?.valueChanges.subscribe(value => {
      this.isChange = value !== this.data.name;
    });
    this.surchargeFormGroup.get('unit')?.valueChanges.subscribe(value => {
      this.isChange = value !== this.data.unit;
    });
    this.surchargeFormGroup.get('description')?.valueChanges.subscribe(value => {
      this.isChange = value !== this.data.description;
    });
  }

  initialEditForm() {
    if(this.data) {
      this.surchargeFormGroup = new FormGroup(
        {
          id: new FormControl({value: this.data.id, disabled: true}, [Validators.required]),
          name: new FormControl(this.data.name, [Validators.required]),
          description: new FormControl(this.data.description, [Validators.required]),
        }
      );
    }
  }
  cancel() {
    this.dialogRef.close();
  }

  save() {
    this.isSubmit = true;
    if (this.surchargeFormGroup.valid) {
      const request = {
        id: this.surchargeFormGroup.getRawValue().id,
        name: this.surchargeFormGroup.getRawValue().name,
        unit: this.surchargeFormGroup.getRawValue().unit,
        description: this.surchargeFormGroup.getRawValue().description
      };
      this.surchargeService.editSurcharge(request).subscribe((resp) => {
          if (resp && resp.result) {
            this.dataBindingService.sendData({
              surchargeEditStatus: true
            });
            this.sweetAlertCustomizeService.showSuccessAlertRightPopup(ALERT_CONTENT.EDIT_SURCHARGE_SUCCESSFULLY);
            this.dialogRef.close()
          }
        },
        (error) => {
          this.sweetAlertCustomizeService.showErrorAlertRightPopup(error.error);
        });
    }
  }
}

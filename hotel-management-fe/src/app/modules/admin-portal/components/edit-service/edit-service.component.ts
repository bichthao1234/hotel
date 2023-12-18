import {Component, Inject, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {RoomTypeService} from "../../services/room-type.service";
import {SweetAlertCustomizeService} from "../../../../services/sweet-alert-customize.service";
import {DataBindingService} from "../../../../services/data-binding.service";
import {ALERT_CONTENT} from "../../../../constant/ALERT_CONTENT";
import {ServiceService} from "../../services/service.service";

@Component({
  selector: 'app-edit-service',
  templateUrl: './edit-service.component.html',
  styleUrls: ['./edit-service.component.css']
})
export class EditServiceComponent implements OnInit {
  serviceFormGroup = new FormGroup(
    {
      id: new FormControl('', [Validators.required]),
      name: new FormControl('', [Validators.required]),
      unit: new FormControl('', [Validators.required]),
      description: new FormControl('', [Validators.required]),
    }
  );
  isSubmit = false;
  isChange = false;

  constructor(public dialogRef: MatDialogRef<EditServiceComponent>,
              private serviceService: ServiceService,
              private sweetAlertCustomizeService: SweetAlertCustomizeService,
              private dataBindingService: DataBindingService,
              @Inject(MAT_DIALOG_DATA) public data: any) {
  }

  ngOnInit() {
    this.initialEditForm();
    this.serviceFormGroup.get('name')?.valueChanges.subscribe(value => {
      this.isChange = value !== this.data.name;
    });
    this.serviceFormGroup.get('unit')?.valueChanges.subscribe(value => {
      this.isChange = value !== this.data.unit;
    });
    this.serviceFormGroup.get('description')?.valueChanges.subscribe(value => {
      this.isChange = value !== this.data.description;
    });
  }

  initialEditForm() {
    if(this.data) {
      this.serviceFormGroup = new FormGroup(
        {
          id: new FormControl({value: this.data.id, disabled: true}, [Validators.required]),
          name: new FormControl(this.data.name, [Validators.required]),
          unit: new FormControl(this.data.unit, [Validators.required]),
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
    if (this.serviceFormGroup.valid) {
      const request = {
        id: this.serviceFormGroup.getRawValue().id,
        name: this.serviceFormGroup.getRawValue().name,
        unit: this.serviceFormGroup.getRawValue().unit,
        description: this.serviceFormGroup.getRawValue().description
      };
      this.serviceService.editService(request).subscribe((resp) => {
          if (resp && resp.result) {
            this.dataBindingService.sendData({
              serviceEditStatus: true
            });
            this.sweetAlertCustomizeService.showSuccessAlertRightPopup(ALERT_CONTENT.EDIT_SERVICE_SUCCESSFULLY);
            this.dialogRef.close()
          }
        },
        (error) => {
          this.sweetAlertCustomizeService.showErrorAlertRightPopup(error.error);
        });
    }
  }
}

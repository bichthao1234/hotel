import {Component, Inject, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {RoomTypeService} from "../../services/room-type.service";
import {SweetAlertCustomizeService} from "../../../../services/sweet-alert-customize.service";
import {DataBindingService} from "../../../../services/data-binding.service";
import {ALERT_CONTENT} from "../../../../constant/ALERT_CONTENT";

@Component({
  selector: 'app-edit-room-type',
  templateUrl: './edit-room-type.component.html',
  styleUrls: ['./edit-room-type.component.css']
})
export class EditRoomTypeComponent implements OnInit {
  roomTypeEditForm = new FormGroup(
    {
      id: new FormControl('', [Validators.required]),
      name: new FormControl('', [Validators.required])
    }
  );
  isSubmit = false;
  isChange = false;

  constructor(public dialogRef: MatDialogRef<EditRoomTypeComponent>,
              private roomTypeService: RoomTypeService,
              private sweetAlertCustomizeService: SweetAlertCustomizeService,
              private dataBindingService: DataBindingService,
              @Inject(MAT_DIALOG_DATA) public data: any) {
  }

  ngOnInit() {
    this.initialEditForm();
    this.roomTypeEditForm.get('name')?.valueChanges.subscribe(value => {
      this.isChange = value !== this.data.name;
    });
  }

  initialEditForm() {
    if(this.data) {
      this.roomTypeEditForm = new FormGroup(
        {
          id: new FormControl({value: this.data.id, disabled: true}, [Validators.required]),
          name: new FormControl(this.data.name, [Validators.required])
        }
      );
    }
  }
  cancel() {
    this.dialogRef.close();
  }

  save() {
    this.isSubmit = true;
    if (this.roomTypeEditForm.valid) {
      const roomType = {
        id: this.roomTypeEditForm.getRawValue().id,
        name: this.roomTypeEditForm.getRawValue().name
      };
      this.roomTypeService.editRoomType(roomType).subscribe((resp) => {
          if (resp && resp.result) {
            this.dataBindingService.sendData({
              roomTypeEditStatus: true
            });
            this.dialogRef.close()
          }
        },
        (error) => {
          this.sweetAlertCustomizeService.showErrorAlertRightPopup(error.error);
        });
    }
  }
}

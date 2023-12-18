import {Component, OnInit} from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {RoomTypeService} from "../../services/room-type.service";
import {SweetAlertCustomizeService} from "../../../../services/sweet-alert-customize.service";
import {DataBindingService} from "../../../../services/data-binding.service";
import {ALERT_CONTENT} from "../../../../constant/ALERT_CONTENT";

@Component({
  selector: 'app-add-room-type',
  templateUrl: './add-room-type.component.html',
  styleUrls: ['./add-room-type.component.css']
})
export class AddRoomTypeComponent implements OnInit {
  isSubmit = false;
  roomTypeForm = new FormGroup({roomTypeName: new FormControl('', [Validators.required])});

  constructor(public dialogRef: MatDialogRef<AddRoomTypeComponent>,
              private roomTypeService: RoomTypeService,
              private sweetAlertCustomizeService: SweetAlertCustomizeService,
              private dataBindingService: DataBindingService) {
  }

  ngOnInit() {
  }

  cancel() {
    this.dialogRef.close();
  }

  save() {
    this.isSubmit = true;
    if (this.roomTypeForm.valid) {
      const roomType = {
        name: this.roomTypeForm.getRawValue().roomTypeName
      };
      this.roomTypeService.saveRoomType(roomType).subscribe((resp) => {
          if (resp && resp.result) {
            this.dataBindingService.sendData({
              roomTypeSaveStatus: true
            });
            this.sweetAlertCustomizeService.showSuccessAlertRightPopup(ALERT_CONTENT.ADD_ROOM_TYPE_SUCCESSFUL);
          }
        },
        (error) => {
          this.sweetAlertCustomizeService.showErrorAlertRightPopup(error.error);
        });
    }
  }
}

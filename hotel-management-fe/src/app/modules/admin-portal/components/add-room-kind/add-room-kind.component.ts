import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {ALERT_CONTENT} from "../../../../constant/ALERT_CONTENT";
import {RoomKindService} from "../../services/room-kind.service";
import {DataBindingService} from "../../../../services/data-binding.service";
import {SweetAlertCustomizeService} from "../../../../services/sweet-alert-customize.service";
import {MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-add-room-kind',
  templateUrl: './add-room-kind.component.html',
  styleUrls: ['./add-room-kind.component.css']
})
export class AddRoomKindComponent implements OnInit {
  roomKindForm = new FormGroup({roomKindName: new FormControl('', [Validators.required])});
  isSubmit = false;

  constructor(private roomKindService: RoomKindService,
              private dataBindingService: DataBindingService,
              private sweetAlertCustomizeService: SweetAlertCustomizeService,
              private dialogRef: MatDialogRef<AddRoomKindComponent>) {
  }

  ngOnInit() {
  }

  cancel() {
    this.dialogRef.close();
  }

  save() {
    this.isSubmit = true;
    if (this.roomKindForm.valid) {
      const roomKind = {
        name: this.roomKindForm.getRawValue().roomKindName
      };
      this.roomKindService.saveRoomKind(roomKind).subscribe((resp) => {
          if (resp && resp.result) {
            this.dataBindingService.sendData({
              roomKindSaveStatus: true
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

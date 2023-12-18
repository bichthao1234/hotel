import {Component, Inject, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {RoomTypeService} from "../../services/room-type.service";
import {SweetAlertCustomizeService} from "../../../../services/sweet-alert-customize.service";
import {DataBindingService} from "../../../../services/data-binding.service";
import {RoomKindService} from "../../services/room-kind.service";

@Component({
  selector: 'app-edit-room-kind',
  templateUrl: './edit-room-kind.component.html',
  styleUrls: ['./edit-room-kind.component.css']
})
export class EditRoomKindComponent implements OnInit {
  roomKindEditForm = new FormGroup({
      id: new FormControl('', [Validators.required]),
      name: new FormControl('', [Validators.required])
    });
  isSubmit = false;
  isChange: any;

  constructor(public dialogRef: MatDialogRef<EditRoomKindComponent>,
              private roomKindService: RoomKindService,
              private sweetAlertCustomizeService: SweetAlertCustomizeService,
              private dataBindingService: DataBindingService,
              @Inject(MAT_DIALOG_DATA) public data: any) {
  }

  ngOnInit() {
    this.initialEditForm();
    this.roomKindEditForm.get('name')?.valueChanges.subscribe(value => {
      this.isChange = value !== this.data.name;
    });
  }
  initialEditForm() {
    if(this.data) {
      this.roomKindEditForm = new FormGroup(
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
    if (this.roomKindEditForm.valid) {
      const roomKind = {
        id: this.roomKindEditForm.getRawValue().id,
        name: this.roomKindEditForm.getRawValue().name
      };
      this.roomKindService.editRoomKind(roomKind).subscribe((resp) => {
          if (resp && resp.result) {
            this.dataBindingService.sendData({
              roomKindEditStatus: true
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

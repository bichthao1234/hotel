import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {RoomClassificationService} from "../../../home-portal/services/room-classification.service";
import {RoomStatusService} from "../../services/room-status.service.";
import {RoomService} from "../../services/room.service";
import {SweetAlertCustomizeService} from "../../../../services/sweet-alert-customize.service";
import {MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-add-room',
  templateUrl: './add-room.component.html',
  styleUrls: ['./add-room.component.css']
})
export class AddRoomComponent implements OnInit {
  isSubmit: any;
  roomForm = new FormGroup({});
  roomClassifications: any;
  status: any;

  constructor(private roomClassificationService: RoomClassificationService,
              private roomStatusService: RoomStatusService,
              private roomService: RoomService,
              private sweetAlertCustomizeService: SweetAlertCustomizeService,
              private dialogRef: MatDialogRef<AddRoomComponent>) {
  }

  ngOnInit() {
    this.getAllRoomClassification();
    this.getAllRoomStatus();
    this.roomForm = new FormGroup({
      id: new FormControl('', [Validators.required, Validators.maxLength(6), Validators.minLength(6)]),
      floor: new FormControl('', [Validators.required, Validators.min(1), Validators.max(99)]),
      roomClassification: new FormControl(this.roomClassifications?.get(0), [Validators.required]),
      status: new FormControl(this.status?.get(0), [Validators.required]),
    });
    this.roomForm.controls['id'].valueChanges.subscribe((value: string) => {
      this.roomForm.controls['id'].setValue(value.toUpperCase(), { emitEvent: false });
      console.log(this.roomForm.controls['id'].value)
    });
  }

  cancel() {
    this.dialogRef.close(true);
  }

  save() {
    this.isSubmit = true;
    let formValue = this.roomForm.getRawValue();
    let request = {
      id: formValue.id,
      floor: formValue.floor,
      roomClassificationId: formValue.roomClassification?.id,
      roomStatusId: formValue.status?.id
    };
    if (this.roomForm.valid) {
      this.roomService.createNewRoom(request).subscribe((resp: any) => {
          if (resp) {
            this.sweetAlertCustomizeService.showSuccessAlertRightPopup("Thêm mới phòng thành công!");
          }
        },
        (error: any) => {
          this.sweetAlertCustomizeService.showErrorAlertRightPopup(error.error.error);
        });
    }
  }

  getFormControl(s: string) {
    return this.roomForm.controls[s] as FormControl;
  }

  getAllRoomClassification() {
    this.roomClassificationService.getAll().subscribe((resp: any) => {
      this.roomClassifications = resp;
      this.roomClassifications.forEach((roomCls: any) => {
        roomCls.filterDescription = `${roomCls.roomKind.name} ${roomCls.roomType.name}`;
      });
      console.log(this.roomClassifications);
    });
  }

  getAllRoomStatus() {
    this.roomStatusService.getAll().subscribe((resp: any) => {
      this.status = resp;
    });
  }
}

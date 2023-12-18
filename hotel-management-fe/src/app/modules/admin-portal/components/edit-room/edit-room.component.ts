import {Component, Inject, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {RoomClassificationService} from "../../../home-portal/services/room-classification.service";
import {RoomStatusService} from "../../services/room-status.service.";
import {RoomService} from "../../services/room.service";
import {SweetAlertCustomizeService} from "../../../../services/sweet-alert-customize.service";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {forkJoin, Observable, switchMap} from "rxjs";

@Component({
  selector: 'app-add-room',
  templateUrl: './edit-room.component.html',
  styleUrls: ['./edit-room.component.css']
})
export class EditRoomComponent implements OnInit {
  isSubmit: any;
  roomForm = new FormGroup({
    id: new FormControl('', [Validators.required, Validators.maxLength(6), Validators.minLength(6)]),
    floor: new FormControl('', [Validators.required, Validators.min(1), Validators.max(99)]),
    roomClassification: new FormControl('' , [Validators.required]),
    status: new FormControl('' , [Validators.required]),
  });
  roomClassifications: any;
  status: any;

  constructor(private roomClassificationService: RoomClassificationService,
              private roomStatusService: RoomStatusService,
              private roomService: RoomService,
              private sweetAlertCustomizeService: SweetAlertCustomizeService,
              private dialogRef: MatDialogRef<EditRoomComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any) {
  }

  ngOnInit() {
    console.log(this.data);
    forkJoin({
      roomStatus: this.getAllRoomStatus(),
      roomClassifications: this.getAllRoomClassification()
    }).pipe(
      switchMap((data: { roomStatus: any; roomClassifications: any }) => {
        this.status = data.roomStatus;
        this.roomClassifications = data.roomClassifications
        let initialStatus = this.status.find((item: any) => item.id === this.data.roomStatusId);
        let initialRoomClassification = this.roomClassifications.find((item: any) => item.id === this.data.roomClassId);
        console.log(initialStatus, initialRoomClassification)
        this.roomClassifications = data.roomClassifications;
        this.roomClassifications.forEach((roomCls: any) => {
          roomCls.filterDescription = `${roomCls.roomKind.name} ${roomCls.roomType.name}`;
        });

        this.roomForm = new FormGroup({
          id: new FormControl(this.data.id, [Validators.required, Validators.maxLength(6), Validators.minLength(6)]),
          floor: new FormControl(this.data.floor, [Validators.required, Validators.min(1), Validators.max(99)]),
          roomClassification: new FormControl(initialRoomClassification, [Validators.required]),
          status: new FormControl(initialStatus, [Validators.required]),
        });
        return new Observable();
      })
    ).subscribe();
  }

  cancel() {
    this.dialogRef.close(true);
  }

  modify() {
    this.isSubmit = true;
    let formValue = this.roomForm.getRawValue();
    let request = {
      id: formValue.id,
      floor: formValue.floor,
      roomClassificationId: formValue.roomClassification?.id,
      roomStatusId: formValue.status?.id
    };
    if (this.roomForm.valid) {
      this.roomService.modifyRoom(request).subscribe((resp: any) => {
          if (resp) {
            this.sweetAlertCustomizeService.showSuccessAlertRightPopup("Chỉnh sửa phòng thành công!");
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
    return this.roomClassificationService.getAll();
  }

  getAllRoomStatus() {
    return this.roomStatusService.getAll();
  }
}

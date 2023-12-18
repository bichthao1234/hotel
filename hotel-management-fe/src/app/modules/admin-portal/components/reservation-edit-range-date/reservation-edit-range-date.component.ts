import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {ReservationService} from "../../services/reservation.service";
import {OrderedRooms} from "../../models/OrderedRooms";
import {RoomClassificationService} from "../../../home-portal/services/room-classification.service";
import {RoomService} from "../../services/room.service";
import {
  DetailManifestConvertReservationsComponent
} from "../detail-manifest-conver-reservations/detail-manifest-convert-reservations.component";
import {SweetAlertCustomizeService} from "../../../../services/sweet-alert-customize.service";
import {RentalSlipService} from "../../services/rental-slip.service";
import {AuthAdminService} from "../../services/auth-admin.service";
import {CommonUtil} from "../../../../util/CommonUtil";

@Component({
  selector: 'app-reservation-edit-range-date',
  templateUrl: './reservation-edit-range-date.component.html',
  styleUrls: ['./reservation-edit-range-date.component.css']
})
export class ReservationEditRangeDateComponent implements OnInit {
  orderedRooms!: any[];
  roomAvailableList: any;
  rentalSlipDetail: any;
  startDate!: Date;
  endDate!: Date;

  constructor(@Inject(MAT_DIALOG_DATA) public data: any,
              private dialog: MatDialog,
              private dialogRef: MatDialogRef<ReservationEditRangeDateComponent>,
              private reservationService: ReservationService,
              private roomClassificationService: RoomClassificationService,
              private roomService: RoomService,
              private sweetAlertCustomizeService: SweetAlertCustomizeService) {
  }

  ngOnInit() {
    console.log(this.data)
    this.startDate = new Date(this.data.startDate)
    this.endDate = new Date(this.data.endDate)
  }

  closeDialog() {
    this.dialogRef.close();
  }

  getDate(event: any) {
    if (CommonUtil.getRangeDate(new Date(event.start), new Date(event.end)) === 0) {
      this.sweetAlertCustomizeService.showWarringAlertRightPopup("Thời gian lưu trú tối thiểu là 1 ngày");
    } else {
      this.startDate = new Date(event.start);
      this.endDate = new Date(event.end);
    }
  }

  changeDateRangeReservation(id: any) {
    const request = {
      reservationId: id,
      startDate: this.startDate,
      endDate: this.endDate,
    }
    this.reservationService.changeDateRangeReservation(request).subscribe(() => {
      this.dialogRef.close({statusOnReload: true});
      this.sweetAlertCustomizeService.showSuccessAlertRightPopup("Cập nhập ngày đến/đi thành công!");
    }, error => {
      console.log(error)
      this.sweetAlertCustomizeService.showErrorAlertRightPopup(error.error.error);
    });
  }

  getPrice(value: number) {
    return CommonUtil.formatAmount(value.toString());
  }
}

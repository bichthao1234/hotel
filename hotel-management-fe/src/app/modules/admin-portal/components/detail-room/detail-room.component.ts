import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {CommonUtil} from "../../../../util/CommonUtil";
import {PaymentDialogComponent} from "../paymen-dialog/payment-dialog.component";
import {RoomService} from "../../services/room.service";
import {RouterFEConstant} from "../../../../constant/RouterFEConstant";
import {ReservationService} from "../../services/reservation.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-detail-room',
  templateUrl: './detail-room.component.html',
  styleUrls: ['./detail-room.component.css']
})
export class DetailRoomComponent implements OnInit {

  rentalDetailId: any;
  rentalSlipId: any;

  constructor(private dialogRef: MatDialogRef<DetailRoomComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any,
              public dialog: MatDialog,
              private roomService: RoomService,
              private reservationService: ReservationService,
              private router: Router,
              ) {
  }

  statusColors: { [key: string]: string } = {
    "RS00001": "#efefef",
    "RS00002": "#70d970",
    "RS00003": "#e59975",
    "RS00004": "yellow",
  };

  ngOnInit() {
    console.log(this.data)
    this.getRentalDetail();
  }

  closeDialog() {
    this.dialogRef.close();
  }

  getBackgroundColor(status: string): string {
    return this.statusColors[status] || "#efefef";
  }

  getRentalDetail() {
    this.roomService.getRoomRentalDetail(this.data.id).subscribe((resp) => {
      if (resp && Array.isArray(resp) && resp.length > 0) {
        this.rentalDetailId = resp[0].rentalSlipId
        this.rentalSlipId = resp[0].id
        console.log(this.rentalDetailId)
      }
    }, (error) => {
      console.log(error)
    });
  }

  public readonly CommonUtil = CommonUtil;

  openPaymentDialog() {
    this.closeDialog();
    this.dialog.open(PaymentDialogComponent, {data: Array.of(this.rentalDetailId), disableClose: true})
  }

  routeToRentalDetail() {
    this.closeDialog();
    this.router.navigate(
      [RouterFEConstant.ADMIN_PORTAL.path,
        RouterFEConstant.ADMIN_PORTAL_COMMON_RENTAL_SLIP.path, this.rentalSlipId]
    ).then(() => {
    });
  }
}

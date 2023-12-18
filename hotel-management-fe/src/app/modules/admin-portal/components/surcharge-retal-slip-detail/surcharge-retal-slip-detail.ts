import {Component, Input, OnInit} from '@angular/core';
import {RoomService} from "../../services/room.service";
import {CommonUtil} from "../../../../util/CommonUtil";
import {ServiceService} from "../../services/service.service";
import {ALERT_CONTENT} from "../../../../constant/ALERT_CONTENT";
import {SweetAlertCustomizeService} from "../../../../services/sweet-alert-customize.service";
import {MatDialogRef} from "@angular/material/dialog";
import {DetailRoomComponent} from "../detail-room/detail-room.component";

@Component({
  selector: 'app-surcharge-retal-slip-detail',
  templateUrl: './surcharge-retal-slip-detail.html',
  styleUrls: ['./surcharge-retal-slip-detail.css']
})
export class SurchargeRetalSlipDetail implements OnInit {
  @Input() roomId: any;
  roomRentalSurcharges: any;

  constructor(
    private roomService: RoomService,
    private serviceService: ServiceService,
    private sweetAlertCustomizeService: SweetAlertCustomizeService,
    private dialogRef: MatDialogRef<DetailRoomComponent>,
  ) {  }

  ngOnInit() {
    this.getRoomRentalSurcharge();
  }

  getRoomRentalSurcharge() {
    this.roomService.getRoomRentalSurcharge(this.roomId).subscribe((resp) => {
      console.log(resp)
      if (resp && Array.isArray(resp) && resp.length > 0) {
        this.roomRentalSurcharges = resp;
      }
    }, (error) => {
      console.log(error)
    });
  }

  getTotal(service: any) {
    return Number(service?.quantity) * Number(service?.price)
  }

  getPrice(value: number) {
    return CommonUtil.formatAmount(value.toString());
  }
}

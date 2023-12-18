import {Component, Input, OnInit} from '@angular/core';
import {RoomService} from "../../services/room.service";
import {CommonUtil} from "../../../../util/CommonUtil";
import {ServiceService} from "../../services/service.service";
import {ALERT_CONTENT} from "../../../../constant/ALERT_CONTENT";
import {SweetAlertCustomizeService} from "../../../../services/sweet-alert-customize.service";
import {MatDialogRef} from "@angular/material/dialog";
import {DetailRoomComponent} from "../detail-room/detail-room.component";

@Component({
  selector: 'app-service-retal-slip-detail',
  templateUrl: './service-retal-slip-detail.html',
  styleUrls: ['./service-retal-slip-detail.css']
})
export class ServiceRetalSlipDetail implements OnInit {
  @Input() roomId: any;
  roomRentalServices: any;

  constructor(
    private roomService: RoomService,
    private serviceService: ServiceService,
    private sweetAlertCustomizeService: SweetAlertCustomizeService,
    private dialogRef: MatDialogRef<DetailRoomComponent>,
  ) {  }

  ngOnInit() {
    this.getRoomRentalService();
  }

  getRoomRentalService() {
    this.roomService.getRoomRentalService(this.roomId).subscribe((resp) => {
      console.log(resp)
      if (resp && Array.isArray(resp) && resp.length > 0) {
        this.roomRentalServices = resp;
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

  paymentHandle(service: any) {
    const request = {
      rentalSlipDetailId: service?.rentalSlipDetailId,
      serviceId: service?.serviceId
    }
    this.serviceService.payForService(request).subscribe((resp) => {
        if (resp && ('result' in resp) && resp.result) {
          this.sweetAlertCustomizeService.showSuccessAlertRightPopup(ALERT_CONTENT.PAY_FOR_SERVICE_SUCCESSFULLY);
          this.dialogRef.close();
        }
      },
      (error) => {
        this.sweetAlertCustomizeService.showErrorAlertRightPopup(error.error.error);
      });
  }
}

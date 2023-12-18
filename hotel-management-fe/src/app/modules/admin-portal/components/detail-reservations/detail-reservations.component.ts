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
  selector: 'app-detail-reservations',
  templateUrl: './detail-reservations.component.html',
  styleUrls: ['./detail-reservations.component.css']
})
export class DetailReservationsComponent implements OnInit {
  orderedRooms!: any[];
  roomAvailableList: any;
  rentalSlipDetail: any;

  constructor(@Inject(MAT_DIALOG_DATA) public data: any,
              private dialog: MatDialog,
              private dialogRef: MatDialogRef<DetailReservationsComponent>,
              private reservationService: ReservationService,
              private roomClassificationService: RoomClassificationService,
              private roomService: RoomService,
              private sweetAlertCustomizeService: SweetAlertCustomizeService,
              private rentalSlipServiceService: RentalSlipService,
              private authAdminService: AuthAdminService) {
  }

  ngOnInit() {
    console.log(this.data)
    if (this.data.status.value === 2) {
      this.getRentalSlip();
    }
    this.getDetailReservations();
  }

  openDialogManifest(i: any) {
    if (!!i.orderItem?.roomId) {
      const detailManifestDialog = this.dialog.open(DetailManifestConvertReservationsComponent,
        {
          data: {
            customerInput: i.orderItem?.customerIdList,
            orderedRooms: this.orderedRooms
          }
        });
      detailManifestDialog.afterClosed().subscribe((data) => {
        if (!!data) {
          this.sweetAlertCustomizeService.showSuccessAlertRightPopup("Thêm khách ở thành công!")
        }
      });
    } else {
      this.sweetAlertCustomizeService.showWarringAlertRightPopup("Vui lòng chọn phòng trước khi thêm khách ở");
    }

  }

  closeDialog() {
    this.dialogRef.close();
  }

  getDetailReservations() {
    this.reservationService.getDetailReservationList(this.data.id).subscribe(async (resp) => {
      this.roomAvailableList = resp.reduce((roomAvailable: any, curr: any) => {
        roomAvailable[curr.roomClassId] = null;
        return roomAvailable;
      }, {});
      for (let key of Object.keys(this.roomAvailableList)) {
        let roomAvailable = await this.roomService.getRoomAvailableList(key).toPromise();
        this.roomAvailableList[key] = roomAvailable;
      }
      await this.convertToOrderRooms(resp);
    });
  }

  async convertToOrderRooms(resp: any) {
    this.orderedRooms = [];
    for (let i of resp) {
      // for (let j = 0; j < i.numberOfRooms; j++) {
        let classification = await this.roomClassificationService.getRoomClassificationWithId(i.roomClassId).toPromise();
        let orderItem: OrderedRooms = {
          roomClassId: i.roomClassId,
          roomId: i.roomId,
          customerIdList: [],
          customerIdsString: null
        }
        this.orderedRooms.push({
          index: resp.indexOf(i),
          roomClassId: i.roomClassId,
          orderItem: orderItem,
          classification: classification,
          status: i.status,
        });
      // }
    }
    console.log(this.orderedRooms)
  }

  getRangeDate() {
    let startDate = new Date(this.data.startDate);
    let endDate = new Date(this.data.endDate);
    let diffInTime = endDate.getTime() - startDate.getTime();
    let diffInDays = diffInTime / (1000 * 3600 * 24);
    return diffInDays;
  }

  addOrderRooms(i: any, event: any) {
    this.orderedRooms = this.orderedRooms.map(item => {
      if (item.index === i.index && item.roomClassId === i.roomClassId) {
        item.orderItem.roomId = event.target.value;
      }
      return item;
    });
  }

  checkInOrderRooms(i: any, room: any) {
    return this.orderedRooms.some(item => item.roomClassId === i.roomClassId && item.orderItem.roomId === room);
  }

  protected readonly Object = Object;

  acceptReservation() {
    const isValidReservation = this.checkThenAcceptReservation();
    if (isValidReservation?.status === 1) {
      this.sweetAlertCustomizeService
        .showWarringAlertRightPopup(`Bạn chưa xếp phòng cho ${isValidReservation.obj?.classification?.roomKind?.name} ${isValidReservation.obj?.classification?.roomType?.name}`)
    }
    if (isValidReservation?.status === -1) {
      this.sweetAlertCustomizeService
        .showWarringAlertRightPopup(`Bạn chưa thêm khách ở cho ${isValidReservation.obj?.classification?.roomKind?.name} ${isValidReservation.obj?.classification?.roomType?.name}`)
    }
    if (isValidReservation?.status == 0) {
      const reqConvertReservationRequestDto = {
        reservationId: this.data.id,
        employeeId: this.authAdminService.getLoginAdminInfor()?.id,
        arrivalDate: new Date(),
        orderedRooms: this.convertToOrderRoomsReq(),
        orderedRoomsString: ''
      }
      this.rentalSlipServiceService.convertFromReservation(reqConvertReservationRequestDto).subscribe((resp) => {
        if(resp) {
          this.dialogRef.close({statusOnReload: true});
          this.sweetAlertCustomizeService.showSuccessAlertRightPopup("Xác nhận phiếu đặt thành công!");
        }
      }, (error) => {
        this.sweetAlertCustomizeService.showErrorAlertRightPopup(error.error.error);
      })
    }
  }

  acceptOneReservation(reservationDetail: any) {
    if (!reservationDetail.orderItem.roomId) {
      this.sweetAlertCustomizeService
        .showWarringAlertRightPopup(`Bạn chưa xếp phòng cho
        ${reservationDetail?.classification?.roomKind?.name} ${reservationDetail?.classification?.roomType?.name}`)
    } else if (!Array.isArray(reservationDetail.orderItem.customerIdList) || reservationDetail.orderItem.customerIdList?.length === 0) {
      this.sweetAlertCustomizeService
        .showWarringAlertRightPopup(`Bạn chưa thêm khách ở cho
        ${reservationDetail?.classification?.roomKind?.name} ${reservationDetail?.classification?.roomType?.name}`)
    } else {
      console.log(reservationDetail)
      const request = {
        reservationId: this.data.id,
        employeeId: this.authAdminService.getLoginAdminInfor()?.id,
        arrivalDate: new Date(),
        orderedRooms: [{
          roomClassId: reservationDetail.orderItem.roomClassId,
          roomId: reservationDetail.orderItem.roomId,
          customerIdList: this.convertCustomerListId(reservationDetail.orderItem.customerIdList),
          customerIdsString: null
        }]
      }
      console.log(request)
      this.rentalSlipServiceService.convertFromReservation(request).subscribe((resp) => {
        if(resp) {
          this.sweetAlertCustomizeService.showSuccessAlertRightPopup("Xác nhận phiếu đặt thành công!");
          this.closeDialog();
        }
      }, (error) => {
        this.sweetAlertCustomizeService.showErrorAlertRightPopup(error.error.error);
      })
    }
  }

  checkThenAcceptReservation() {
    for (let obj of this.orderedRooms) {
      if (!obj.orderItem.roomId) {
        return {
          status: 1,
          obj: obj
        };
      } else if (!Array.isArray(obj.orderItem.customerIdList) || obj.orderItem.customerIdList?.length === 0) {
        return {
          status: -1,
          obj: obj
        };
      }
    }
    return {
      status: 0,
      obj: null
    };
  }

  private convertToOrderRoomsReq() {
    let reqList = [];
    for (let obj of this.orderedRooms) {
      let itemReq = {
        roomClassId: obj.orderItem.roomClassId,
        roomId: obj.orderItem.roomId,
        customerIdList: this.convertCustomerListId(obj.orderItem.customerIdList),
        customerIdsString: null
      }
      reqList.push(itemReq);
    }
    return reqList;
  }

  convertCustomerListId(customerIdList: any[]) {
    return customerIdList.map((item: any) => {
      return item.id;
    });
  }

  getStatus() {
    return CommonUtil.getStatus(this.data);
  }

  getStatusDetail(status: any) {
    switch (status) {
      case 0: return 'Chưa check in'
      case 1: return 'Đã check in'
      default: return  ''
    }
  }

  getRentalSlip() {
    this.rentalSlipServiceService.getRentalSlipDetailWithReservation(this.data.id).subscribe((resp) => {
      if (resp) {
        this.rentalSlipDetail = resp;
        console.log(this.rentalSlipDetail)
      }
    });
  }
}

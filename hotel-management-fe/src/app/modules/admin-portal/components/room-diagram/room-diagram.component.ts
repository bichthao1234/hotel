import {Component, OnInit} from '@angular/core';
import {CommonUtil} from "../../../../util/CommonUtil";
import {GetRoomsRequest} from "../../../../models/GetRoomsRequest";
import {RoomService} from "../../services/room.service";
import {RoomKindService} from '../../services/room-kind.service';
import {RoomTypeService} from '../../services/room-type.service';
import {RoomStatusService} from '../../services/room-status.service.';
import {MatDialog} from "@angular/material/dialog";
import {DetailRoomComponent} from "../detail-room/detail-room.component";
import {QuickCheckInComponent} from "../quick-check-in/quick-check-in.component";
import {SweetAlertCustomizeService} from "../../../../services/sweet-alert-customize.service";

@Component({
  selector: 'app-room-diagram',
  templateUrl: './room-diagram.component.html',
  styleUrls: ['./room-diagram.component.css']
})
export class RoomDiagramComponent implements OnInit {
  roomKinds: any;
  roomTypes: any;
  roomStatus: any;
  rangeValues: number[] = [1000, 10000];
  getRoomsRequestDto!: GetRoomsRequest;
  roomList: any;
  numberOfRooms: any;
  statusBackgroundColors: { [key: string]: string } = {
    "RS00001": "rgba(239,239,239,0.73)",
    "RS00002": "rgba(184,255,184,0.68)",
    "RS00003": "rgba(255,165,139,0.58)",
    "RS00004": "rgba(255,228,43,0.55)",
  };

  statusTextColors: { [key: string]: string } = {
    "RS00001": "#000000",
    "RS00002": "#00b900",
    "RS00003": "#ff1616",
    "RS00004": "#5b5500",
  };

  statusIcon: { [key: string]: string } = {
    "RS00001": "done_outline",
    "RS00002": "supervised_user_circle",
    "RS00003": "contacts",
    "RS00004": "lock",
  };

  constructor(
    private roomService: RoomService,
    private roomKindService: RoomKindService,
    private roomTypeService: RoomTypeService,
    private roomStatusService: RoomStatusService,
    private dialog: MatDialog,
    private sweetAlertCustomizeService: SweetAlertCustomizeService) {
  }

  ngOnInit() {
    this.getRoomsRequestDto = {
      roomKindList: [],
      roomTypeList: [],
      roomStatusList: [],
      priceFrom: null,
      priceTo: null,
      hasPromotion: null,
      floor: null
    };
    this.initialGetRoomReq();
    this.getAllRoomKinds();
    this.getAllRoomTypes();
    this.getAllRoomStatus();
  }

  getPrice(value: number) {
    return CommonUtil.formatAmount(value.toString());
  }

  initialGetRoomReq() {
    this.roomService.getRooms(this.getRoomsRequestDto).subscribe((resp) => {
      if (resp && resp.result && Array.isArray(resp.result) && resp.result.length > 0) {
        this.numberOfRooms = resp.result.length;
        this.roomList = resp.result.reduce((acc: any, cur: any) => {
          let key = cur.floor;
          if (!acc[key]) {
            acc[key] = [];
          }
          acc[key].push(cur);
          return acc;
        }, {});
      } else {
        this.numberOfRooms = 0;
        this.roomList = null;
      }
    });
  }

  getAllRoomTypes() {
    this.roomTypeService.getAll().subscribe((resp => {
      if (resp && Array.isArray(resp) && resp.length > 0) {
        this.roomTypes = resp;
      }
    }))
  }

  getAllRoomKinds() {
    this.roomKindService.getAll().subscribe((resp => {
      if (resp && Array.isArray(resp) && resp.length > 0) {
        this.roomKinds = resp;
      }
    }))
  }

  getAllRoomStatus() {
    this.roomStatusService.getAll().subscribe((resp => {
      if (resp && Array.isArray(resp) && resp.length > 0) {
        this.roomStatus = resp.map((status) => {
          return {
            ...status,
            color: this.getBackgroundColor(status.id)
          };
        });
      }
    }))
  }

  onChangeRequest(event: any, id: any) {
    const updatedRequestDto = {...this.getRoomsRequestDto};
    switch (id) {
      case 'roomKinds':
        updatedRequestDto.roomKindList = event.value.map((item: any) => item.id);
        break;
      case 'roomTypes':
        updatedRequestDto.roomTypeList = event.value.map((item: any) => item.id);
        break;
      case 'roomStatus':
        updatedRequestDto.roomStatusList = event.value.map((item: any) => item.id);
        break;
      default:
        break;
    }
    this.getRoomsRequestDto = updatedRequestDto;
    this.initialGetRoomReq();
  }

  onClickPromotion() {
    this.getRoomsRequestDto.hasPromotion = !this.getRoomsRequestDto.hasPromotion;
    this.initialGetRoomReq();
  }

  onChangePrice(event: any) {
    if (event.values[0] < event.values[1]) {
      this.getRoomsRequestDto.priceFrom = event.values[0];
      this.getRoomsRequestDto.priceTo = event.values[1];
    } else {
      this.getRoomsRequestDto.priceFrom = event.values[1];
      this.getRoomsRequestDto.priceTo = event.values[0];
    }
    this.initialGetRoomReq();
  }

  getBackgroundColor(status: string): string {
    return this.statusBackgroundColors[status] || "#efefef";
  }
  getTextColor(status: string): string {
    return this.statusTextColors[status] || "#efefef";
  }

  getIcon(status: string): string {
    return this.statusIcon[status] || "";
  }

  protected readonly Object = Object;

  getKeys() {
    return Object.keys(this.roomList);
  }

  openDialog(room: any) {
    switch (room.roomStatusId.toString()) {
      case 'RS00002': {
        this.dialog.open(DetailRoomComponent, {data: room});
        break;
      }
      case 'RS00001': {
        let dialog = this.dialog.open(QuickCheckInComponent, {data: room, disableClose: true});
        dialog.afterClosed().subscribe((data) => {
          this.initialGetRoomReq();
          this.getAllRoomKinds();
          this.getAllRoomTypes();
          this.getAllRoomStatus();
        })
        break;
      }
      case 'RS00003': {
        let dialog = this.dialog.open(QuickCheckInComponent, {data: room, disableClose: true});
        dialog.afterClosed().subscribe((data) => {
          this.initialGetRoomReq();
          this.getAllRoomKinds();
          this.getAllRoomTypes();
          this.getAllRoomStatus();
        })
        break;
      }
      default: {
        break;
      }
    }
  }

  hasPromotion(price: any, pricePromotion: any) {
    return pricePromotion !== price;
  }

  protected readonly parseFloat = parseFloat;

  print() {
    window.print()
  }

  getStatusItem(room: any) {
    let roomStatusId = room.roomStatusId;
    let menuItemShow = [
      {
        label: "Phòng trống",
        id: 'RS00001',
        command: () => {
          let statusId = 'RS00001';
          this.changeRoomStatus(room.id, statusId);
        }
      },
      {
        label: "Dơ",
        id: 'RS00003',
        command: () => {
          let idStatus = 'RS00003';
          this.changeRoomStatus(room.id, idStatus);
        }
      },
      {
        label: "Bảo trì",
        id: 'RS00004',
        command: () => {
          let statusId = 'RS00004';
          this.changeRoomStatus(room.id, statusId);
        }
      },
    ];
    return menuItemShow.filter((item: any) => item.id !== roomStatusId);
  }
  changeRoomStatus(roomId: any, statusId: any) {
    this.roomService.changeStatus(roomId, statusId).subscribe((resp: any) => {
      this.initialGetRoomReq();
    }, error => {
      this.sweetAlertCustomizeService.showErrorAlertRightPopup(error.error.error);
    });
  }
}

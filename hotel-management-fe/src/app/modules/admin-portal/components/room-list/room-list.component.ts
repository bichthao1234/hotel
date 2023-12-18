import {Component, OnInit} from '@angular/core';
import {RoomService} from "../../services/room.service";
import {GetRoomsRequest} from "../../../../models/GetRoomsRequest";
import {MatDialog} from "@angular/material/dialog";
import {AddRoomComponent} from "../add-room/add-room.component";
import {EditRoomComponent} from "../edit-room/edit-room.component";

@Component({
  selector: 'app-room-list',
  templateUrl: './room-list.component.html',
  styleUrls: ['./room-list.component.css']
})
export class RoomListComponent implements OnInit {
  roomList: any;
  getRoomsRequestDto!: GetRoomsRequest;
  displayedColumns: string[] = ['id', 'floor', 'roomKind', 'roomType', 'roomStatusName'];
  columnHeaders: { [key: string]: string } = {
    id: 'ID',
    floor: 'Tầng',
    roomKind: 'Loại phòng',
    roomType: 'Kiểu phòng',
    roomStatusName: 'Trạng thái'
  };
  numberOfRooms: any;

  constructor(private roomService: RoomService,
              private dialog: MatDialog) {
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
    this.getAllRoom();
  }

  openEditDialog(room: any) {
    let dialog = this.dialog.open(EditRoomComponent, {data: room ,disableClose: true});
    dialog.afterClosed().subscribe((resp: any) => {
      if (resp) {
        this.getAllRoom();
      }
    });
  }

  getAllRoom() {
    this.roomService.getRooms(this.getRoomsRequestDto).subscribe((resp) => {
      if (resp && resp.result && Array.isArray(resp.result) && resp.result.length > 0) {
        this.numberOfRooms = resp.result.length;
        this.roomList = resp.result;
        console.log(this.roomList);
      } else {
        this.numberOfRooms = 0;
        this.roomList = null;
      }
    });
  }

  openAddRoomDialog() {
    let dialog = this.dialog.open(AddRoomComponent, {disableClose: true});
    dialog.afterClosed().subscribe((resp: any) => {
      if (resp) {
        this.getAllRoom();
      }
    });
  }
}

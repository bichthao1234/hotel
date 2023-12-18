import {Component, OnInit} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {EditRoomKindComponent} from "../edit-room-kind/edit-room-kind.component";
import {AddRoomKindComponent} from "../add-room-kind/add-room-kind.component";
import {RoomKindService} from "../../services/room-kind.service";
import {ALERT_CONTENT} from "../../../../constant/ALERT_CONTENT";
import {DataBindingService} from "../../../../services/data-binding.service";
import {SweetAlertCustomizeService} from "../../../../services/sweet-alert-customize.service";

@Component({
  selector: 'app-room-kind',
  templateUrl: './room-kind.component.html',
  styleUrls: ['./room-kind.component.css']
})
export class RoomKindComponent implements OnInit {
  roomKinds: any;
  displayedColumns: string[] = ['id', 'name'];
  columnHeaders: { [key: string]: string } = {
    id: 'ID',
    name: 'Tên'
  };

  constructor(private dialog: MatDialog,
              private roomKindService: RoomKindService,
              private dataBindingService: DataBindingService,
              private sweetAlertCustomizeService: SweetAlertCustomizeService) {
  }

  ngOnInit() {
    this.getAllRoomKinds();
    this.dataBindingService.getData().subscribe((data) => {
      if (data) {
        if (data.roomKindSaveStatus) {
          this.getAllRoomKinds();
        }
        if (data.roomKindEditStatus) {
          this.getAllRoomKinds();
          this.sweetAlertCustomizeService.showSuccessAlertRightPopup(ALERT_CONTENT.CHANGE_ROOM_TYPE_SUCCESSFUL);
        }
      }
    });
  }

  getAllRoomKinds() {
    this.roomKindService.getAll().subscribe((resp: any) => {
      if (resp && Array.isArray(resp) && resp.length > 0) {
        this.roomKinds = resp;
        console.log(resp)
      }
    });
  }

  addNewRoomKindDialog() {
    const dialogRef = this.dialog.open(AddRoomKindComponent, {disableClose: false});
  }

  openEditDialog(el: any) {
    const dialogRef = this.dialog.open(EditRoomKindComponent, {
      disableClose: false,
      data: el
    });
  }
}

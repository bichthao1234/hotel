import {Component, OnInit} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {AddRoomTypeComponent} from "../add-room-type/add-room-type.component";
import {RoomTypeService} from "../../services/room-type.service";
import {RoomType} from "../../models/roomType";
import {DataBindingService} from "../../../../services/data-binding.service";
import {EditRoomTypeComponent} from "../edit-room-type/edit-room-type.component";
import {ALERT_CONTENT} from "../../../../constant/ALERT_CONTENT";
import {SweetAlertCustomizeService} from "../../../../services/sweet-alert-customize.service";

@Component({
  selector: 'app-room-type',
  templateUrl: './room-type.component.html',
  styleUrls: ['./room-type.component.css']
})
export class RoomTypeComponent implements OnInit {
  roomTypes!: RoomType[];
  displayedColumns: string[] = ['id', 'name'];
  columnHeaders: { [key: string]: string } = {
    id: 'ID',
    name: 'Tên'
  };


  constructor(public dialog: MatDialog,
              private roomTypeService: RoomTypeService,
              private dataBindingService: DataBindingService,
              private sweetAlertCustomizeService: SweetAlertCustomizeService) {
  }

  ngOnInit() {
    this.getAllRoomTypes();
    this.dataBindingService.getData().subscribe((data) => {
      if (data) {
        if (data.roomTypeSaveStatus) {
          this.getAllRoomTypes();
        }
        if (data.roomTypeEditStatus) {
          this.getAllRoomTypes();
          this.sweetAlertCustomizeService.showSuccessAlertRightPopup(ALERT_CONTENT.CHANGE_ROOM_TYPE_SUCCESSFUL);
        }
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

  addNewRoomTypeDialog() {
    const dialogRef = this.dialog.open(AddRoomTypeComponent, {disableClose: true});
  }

  openEditDialog(el: any) {
    const dialogRef = this.dialog.open(EditRoomTypeComponent, {
      disableClose: true,
      data: el
    });
  }
}

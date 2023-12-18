import {Component, OnInit} from '@angular/core';
import {RoomClassificationService} from "../../../home-portal/services/room-classification.service";
import {MatDialog} from "@angular/material/dialog";
import {AddRoomClassificationComponent} from "../add-room-classification/add-room-classification.component";
import {PriceRoomClassDialogComponent} from "../price-room-class-dialog/price-room-class-dialog.component";
import {DataBindingService} from "../../../../services/data-binding.service";
import {EditRoomClassificationComponent} from "../edit-room-classification/edit-room-classification.component";

@Component({
  selector: 'app-room-classification',
  templateUrl: './room-classification.component.html',
  styleUrls: ['./room-classification.component.css']
})
export class RoomClassificationComponent implements OnInit {
  roomClassifications: any;
  displayedColumns: string[] = ['id', 'roomKind', 'roomType', 'guestNum', 'price'];
  columnHeaders: { [key: string]: string } = {
    id: 'ID',
    roomKind: 'Loại phòng',
    roomType: 'Kiểu phòng',
    guestNum: 'Số lượng khách ở',
    price: 'Giá hiện tại',
  };

  constructor(private roomClassificationService: RoomClassificationService,
              private dialog: MatDialog,
              private dataBindingService: DataBindingService,
  ) {
  }

  ngOnInit() {
    this.getAllRoomClassification();
    this.dataBindingService.getData().subscribe((data) => {
      if (data) {
        if (data.priceRoomClassAdd) {
          this.getAllRoomClassification()
        }
      }
    });
  }

  openEditDialog(roomClassification: any) {
    console.log(roomClassification);
    let dialog = this.dialog.open(EditRoomClassificationComponent, {data: roomClassification});
    dialog.afterClosed().subscribe((data: any) => {
      if (data?.result) {
        this.getAllRoomClassification();
      }
    });
  }

  getAllRoomClassification() {
    this.roomClassificationService.getAll().subscribe((resp: any) => {
      if (resp) {
        this.roomClassifications = resp;
        console.log(this.roomClassifications);
      }
    })
  }

  getKeyOfValue(header: string): string {
    return (header === 'roomType' || header === 'roomKind') ? `${header}.name` : header;
  }

  getObject(obj: any, keyFind: string) {
    let keys = keyFind.split('.');
    let current = obj;
    for (let key of keys) {
      if (current[key] !== undefined) {
        current = current[key];
      } else {
        return undefined;
      }
    }
    return current;
  }

  addNew() {
    let dialogAddNew = this.dialog.open(AddRoomClassificationComponent, {
      disableClose: true
    });
  }

  openPriceDialog(roomClassification: any) {
    this.dialog.open(PriceRoomClassDialogComponent, {
      data: roomClassification,
      disableClose: false
    });
  }
}

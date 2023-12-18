import {Component, Input, OnInit} from '@angular/core';
import {DataBindingService} from "../../../../services/data-binding.service";
import {RoomClassificationService} from "../../services/room-classification.service";
import {MatDialog} from "@angular/material/dialog";
import {
  RoomClassificationDetailDialogComponent
} from "../room-classification-detail-dialog/room-classification-detail-dialog.component";
import {CommonUtil} from "../../../../util/CommonUtil";

@Component({
  selector: 'app-detail-search-result-room-class-item',
  templateUrl: './detail-search-result-room-class-item.component.html',
  styleUrls: ['./detail-search-result-room-class-item.component.css']
})
export class DetailSearchResultRoomClassItemComponent implements OnInit {
  @Input() roomClassId: any;
  @Input() numberOfRoom: any;
  @Input() startDate!: Date;
  @Input() carousel!: boolean;
  roomClassification: any;
  roomClassImage: any;
  numRoomList: any;
  price: any;

  constructor(private roomClassificationService: RoomClassificationService,
              private dataBindingService: DataBindingService,
              public dialog: MatDialog) {
  }

  ngOnInit() {
    this.getRoomClassWithId();
    if (this.numberOfRoom) {
      this.numRoomList = Array.from({length: this.numberOfRoom + 1}, (_, i) => i);
    }
    this.getPrice();
    this.getImage();
  }

  getPrice() {
    this.roomClassificationService.getPrice(this.roomClassId, this.startDate).subscribe((resp) => {
      if (resp) {
        this.price = resp;
      }
    });
  }

  getRoomClassWithId() {
    if (this.roomClassId) {
      this.roomClassificationService.getRoomClassificationWithId(this.roomClassId).subscribe((resp) => {
        if (resp) {
          this.roomClassification = resp;
        }
      });
    }
  }

  showNumRoom(i: any) {
    return i < 2 ? i + ' room' : i + ' rooms';
  }

  chooseNumRoom(event: any) {
    const numOfRoom = event.target.value;
    const detailRoomClassToSend = {
      roomClassificationId: this.roomClassification,
      numOfRoom: numOfRoom,
      price: this.price
    }
    this.dataBindingService.sendData({
      detailRoomClassToSend: detailRoomClassToSend
    });

  }

  getImage() {
    this.roomClassificationService.getImage(this.roomClassId).subscribe((resp) => {
      if ('result' in resp) {
        this.roomClassImage = resp.result;
      }
    })
  }

  openRoomClassificationDetail() {
    const reSend = {
      roomClassImage: this.roomClassImage,
      roomClassification: this.roomClassification
    }
    this.dialog.open(RoomClassificationDetailDialogComponent, {data: reSend});
  }

  getShowPrice(amount: any) {
    return CommonUtil.toLocaleAmount(amount) ?? '';
  }
}

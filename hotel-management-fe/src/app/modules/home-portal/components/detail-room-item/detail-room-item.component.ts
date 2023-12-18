import {Component, Input, OnInit} from '@angular/core';
import {CommonUtil} from "../../../../util/CommonUtil";

@Component({
  selector: 'app-detail-room-item',
  templateUrl: './detail-room-item.component.html',
  styleUrls: ['./detail-room-item.component.css']
})
export class DetailRoomItemComponent implements OnInit {
  @Input() detailRoomClass: any;
  price: any;

  constructor() {
  }

  ngOnInit() {
    this.price = this.detailRoomClass.price;
  }

  showNumOfRoom() {
    return `${this.detailRoomClass.numOfRoom} phòng`;
  }

  getShowPrice(amount: any) {
    return CommonUtil.formatAmount(amount.toString()) ?? '';
  }
}

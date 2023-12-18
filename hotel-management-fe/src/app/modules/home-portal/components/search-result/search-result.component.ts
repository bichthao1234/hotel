import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {DataBindingService} from "../../../../services/data-binding.service";
import {RoomClassificationService} from "../../services/room-classification.service";
import {SweetAlertCustomizeService} from "../../../../services/sweet-alert-customize.service";
import {CommonUtil} from "../../../../util/CommonUtil";
import {RoomKindService} from "../../../admin-portal/services/room-kind.service";
import {RoomTypeService} from "../../../admin-portal/services/room-type.service";

@Component({
  selector: 'app-search-result',
  templateUrl: './search-result.component.html',
  styleUrls: ['./search-result.component.css']
})
export class SearchResultComponent implements OnInit {
  isScrolled = false;
  today = new Date();
  endDate!: Date;
  startDate!: Date;
  roomNums!: any;
  roomClassList: any = [];
  detailRoomNums: any[] = [];
  roomClassListShow: any = [];
  roomKinds: any;
  roomKindSelected: any = null;
  roomTypes: any;
  roomTypeSelected: any = null;
  rangeValues: number[] = [10000, 5000000];
  maxPrice = 5000000;

  constructor(private dataBindingService: DataBindingService,
              private router: Router,
              private roomClassificationService: RoomClassificationService,
              private sweetAlertCustomizeService: SweetAlertCustomizeService,
              private roomKindService: RoomKindService,
              private roomTypeService: RoomTypeService) {
  }

  ngOnInit() {
    this.dataBindingService.sendData({isMainPage: false});
    const rNJson = sessionStorage.getItem('roomNum');
    if (rNJson) {
      this.roomNums = rNJson;
    }
    const json = sessionStorage.getItem('respDate') || null;
    if (json) {
      const respDate = JSON.parse(json);
      this.startDate = respDate.start
      this.endDate = respDate.end
    } else {
      this.startDate = new Date(this.today.getFullYear(), this.today.getMonth(), this.today.getDate());
      this.endDate = new Date(this.today.getFullYear(), this.today.getMonth(), this.today.getDate() + 1);
    }
    console.log(this.startDate, this.endDate)
    this.dataBindingService.getData().subscribe((data) => {
      if ('isScrolled' in data) {
        this.isScrolled = data.isScrolled;
      }
      if ('detailRoomClassToSend' in data) {
        this.detailRoomNums = this.addItem(data.detailRoomClassToSend, this.detailRoomNums);
      }
    });
    this.findRoomClass();
    this.getAllRoomKinds();
    this.getAllRoomTypes();
  }

  getAllRoomKinds() {
    this.roomKindService.getAll().subscribe((resp => {
      if (resp && Array.isArray(resp) && resp.length > 0) {
        this.roomKinds = [];
        this.roomKinds = resp;
      }
    }))
  }

  getAllRoomTypes() {
    this.roomTypeService.getAll().subscribe((resp => {
      if (resp && Array.isArray(resp) && resp.length > 0) {
        this.roomTypes = resp;
      }
    }))
  }

  findRoomClass() {
    this.detailRoomNums = [];
    const req = {
      startDate: this.startDate,
      endDate: this.endDate,
      numberOfRoom: null
    }
    this.roomClassificationService.getRoomClassification(req).subscribe((data) => {
      if (data) {
        this.roomClassList = data;
        this.roomClassListShow = this.roomClassList;
        this.maxPrice = this.roomClassList
          .reduce((max: any, room: any) => (room.price > max ? room.price : max), 0) + 100000;
        this.rangeValues[1] = this.maxPrice;
      }
    }, (err) => {
      this.sweetAlertCustomizeService.showErrorAlertRightPopup(err.error.message);
    });
  }

  openPayment() {
    this.goToPaymentPage();
  }

  goToPaymentPage() {
    sessionStorage.setItem('detailRoomNums', JSON.stringify(this.detailRoomNums));
    sessionStorage.setItem('totalPrice', this.getTotalPrice().toString());
    sessionStorage.setItem('checkInOutDate', `${this.getDateStartDate()} - ${this.getDateEndDate()}`);
    sessionStorage.setItem('startDate', this.startDate.toString());
    sessionStorage.setItem('endDate', this.endDate.toString());
    this.router.navigate(['/payment']).then(() => {
    });
  }
  getPrice(value: number) {
    return CommonUtil.formatAmount(value.toString());
  }
  addItem(item: any, list: any[]): any[] {
    const existingItem = list.find(i => i.roomClassificationId.id === item.roomClassificationId.id);

    if (existingItem) {
      if (existingItem.numOfRoom !== item.numOfRoom) {
        const index = list.indexOf(existingItem);
        if (item.numOfRoom === "0") {
          list.splice(index, 1);
        } else {
          list[index] = item;
        }
      }
    } else if (item.numOfRoom !== "0") {
      list.push(item);
    }
    return list;
  }

  getDate(date: Date) {
    return `${date.getDate()}/${date.getMonth()}/${date.getFullYear()}`
  }

  getDateStartDate() {
    const startDate = new Date(this.startDate);
    return `${startDate.getDate()}/${startDate.getMonth() + 1}/${startDate.getFullYear()}`
  }

  getDateEndDate() {
    const endDate = new Date(this.endDate);
    return `${endDate.getDate()}/${endDate.getMonth() + 1}/${endDate.getFullYear()}`
  }

  getTotalPrice() {
    let sum = 0;
    for (let i of this.detailRoomNums) {
      sum += (i.price.priceAfterPromotion * i.numOfRoom);
    }
    const numDate = this.getRangeDate(new Date(this.startDate), new Date(this.endDate));
    return sum * numDate;
  }

  getRangeDate(startDate: Date, endDate: Date) {
    let diffInTime = endDate.getTime() - startDate.getTime();
    let diffInDays = diffInTime / (1000 * 3600 * 24);
    return diffInDays;
  }

  showTotalPrice() {
    return CommonUtil.toLocaleAmount(this.getTotalPrice());
  }

  checkDetailRoomNums() {
    return this.detailRoomNums ? (this.detailRoomNums.length > 0 ? true : false) : false;
  }

  checkSumRoomNums() {

    return Math.sign(this.getTotalRoom() - this.roomNums);
  }

  setDateSelected(event: any) {
    if (this.getRangeDate(new Date(event.start), new Date(event.end)) === 0) {
      this.sweetAlertCustomizeService.showWarringAlertRightPopup("Thời gian lưu trú tối thiểu là 1 ngày");
    } else {
      this.startDate = new Date(event.start);
      this.endDate = new Date(event.end);
    }
  }

  getTotalRoom() {
    let roomNums = 0;
    if (this.checkDetailRoomNums()) {
      for (let i of this.detailRoomNums) {
        roomNums += Number(i.numOfRoom);
      }
    }
    return roomNums;
  }

  onChangePrice() {
    this.changeFilter();
  }

  onChangeRoomTypes() {
    this.changeFilter();
  }

  changeFilter() {
    let roomKindId = this.roomKindSelected?.id ?? null;
    let roomTypeId = this.roomTypeSelected?.id ?? null;
    if (!roomKindId && !roomTypeId) {
      this.roomClassListShow = this.roomClassList.filter((item: any) => (this.rangeValues[0] <= item.price && item.price <= this.rangeValues[1]));
    } else {
      this.roomClassListShow = this.roomClassList.filter((item: any) =>
        (roomKindId ? item.roomKindId === roomKindId : true) &&
        (roomTypeId ? item.roomTypeId === roomTypeId : true) &&
        (this.rangeValues[0] <= item.price && item.price <= this.rangeValues[1])
      );
    }
  }
  onChangeRoomKind() {
    this.changeFilter();
  }
}

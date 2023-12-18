import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {CommonUtil} from "../../../../util/CommonUtil";
import {DatePipe} from "@angular/common";
import {AddRoomTypeComponent} from "../add-room-type/add-room-type.component";
import {AddPriceRoomClassComponent} from "../add-price-room-class/add-price-room-class.component";
import {ALERT_CONTENT} from "../../../../constant/ALERT_CONTENT";
import {DataBindingService} from "../../../../services/data-binding.service";
import {RoomClassificationService} from "../../../home-portal/services/room-classification.service";
import {SweetAlertCustomizeService} from "../../../../services/sweet-alert-customize.service";

@Component({
  selector: 'app-price-room-class-dialog',
  templateUrl: './price-room-class-dialog.component.html',
  styleUrls: ['./price-room-class-dialog.component.css']
})
export class PriceRoomClassDialogComponent implements OnInit {
  constructor(
    private dialogRef: MatDialogRef<PriceRoomClassDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private datePipe: DatePipe,
    private dialog: MatDialog,
    private dataBindingService: DataBindingService,
    private roomClassService: RoomClassificationService,
    private sweetAlertCustomizeService: SweetAlertCustomizeService,
  ) { }

  ngOnInit() {
    console.log(this.data)
    this.getRoomClassificationWithId();
    this.dataBindingService.getData().subscribe((data) => {
      if (data) {
        if (data.priceRoomClassAdd) {
          this.getRoomClassificationWithId()
        }
      }
    });
  }

  getRoomClassificationWithId() {
    this.roomClassService.getRoomClassificationWithId(this.data.id).subscribe((resp => {
      if (resp) {
        this.data = resp;
      }
    }))
  }

  close() {
    this.dialogRef.close();
  }

  getPrice(price: any) {
    return price ? CommonUtil.formatAmount(price.toString()) : null;
  }

  getFormatDate(date: any): string {
    return 'Ngày áp dụng: ' + this.datePipe.transform(date, 'dd/MM/yyyy');
  }

  isSmallerThanToday(date: any) {
    return new Date(date).getTime() > new Date().getTime();
  }

  openAddNewDialog() {
    this.close()
    this.dialog.open(AddPriceRoomClassComponent, {
      data: this.data,
      disableClose: true
    });
  }
}

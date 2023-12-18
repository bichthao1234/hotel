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
import {ServiceService} from "../../services/service.service";
import {AddPriceServiceComponent} from "../add-price-service/add-price-service.component";

@Component({
  selector: 'app-price-service-dialog',
  templateUrl: './price-service-dialog.component.html',
  styleUrls: ['./price-service-dialog.component.css']
})
export class PriceServiceDialogComponent implements OnInit {
  constructor(
    private dialogRef: MatDialogRef<PriceServiceDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private datePipe: DatePipe,
    private dialog: MatDialog,
    private dataBindingService: DataBindingService,
    private serviceService: ServiceService,
    private sweetAlertCustomizeService: SweetAlertCustomizeService,
  ) { }

  ngOnInit() {
    console.log(this.data)
    this.getServiceWithId();
    this.dataBindingService.getData().subscribe((data) => {
      if (data) {
        if (data.priceServiceAdd) {
          this.getServiceWithId()
        }
      }
    });
  }

  getServiceWithId() {
    this.serviceService.getServiceWithId(this.data.id).subscribe((resp => {
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
    this.dialog.open(AddPriceServiceComponent, {
      data: this.data,
      disableClose: true
    });
  }
}

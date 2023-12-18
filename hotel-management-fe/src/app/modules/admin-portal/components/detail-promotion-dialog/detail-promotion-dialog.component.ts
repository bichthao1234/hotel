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
import {PromotionService} from "../../services/promotion.service";
import {AddPromotionDetailDialogComponent} from "../add-promotion-detail-dialog/add-promotion-detail-dialog.component";

@Component({
  selector: 'app-detail-promotion-dialog',
  templateUrl: './detail-promotion-dialog.component.html',
  styleUrls: ['./detail-promotion-dialog.component.css']
})
export class DetailPromotionDialogComponent implements OnInit {
  constructor(
    private dialogRef: MatDialogRef<DetailPromotionDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private datePipe: DatePipe,
    private dialog: MatDialog,
    private dataBindingService: DataBindingService,
    private promotionService: PromotionService,
    private sweetAlertCustomizeService: SweetAlertCustomizeService,
  ) { }

  ngOnInit() {
    console.log(this.data)
    this.getPromotionWithId();
    this.dataBindingService.getData().subscribe((data) => {
      if (data) {
        if (data.detailPromotionAdd) {
          this.getPromotionWithId()
        }
      }
    });
  }

  getPromotionWithId() {
    this.promotionService.getWithId(this.data.id).subscribe((resp => {
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
    this.dialog.open(AddPromotionDetailDialogComponent, {
      data: this.data,
      disableClose: true
    });
  }
}

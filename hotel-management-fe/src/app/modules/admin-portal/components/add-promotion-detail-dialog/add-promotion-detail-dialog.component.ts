import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {RoomTypeService} from "../../services/room-type.service";
import {SweetAlertCustomizeService} from "../../../../services/sweet-alert-customize.service";
import {DataBindingService} from "../../../../services/data-binding.service";
import {ALERT_CONTENT} from "../../../../constant/ALERT_CONTENT";
import {RoomClassificationService} from "../../../home-portal/services/room-classification.service";
import {AuthAdminService} from "../../services/auth-admin.service";
import {PriceRoomClassDialogComponent} from "../price-room-class-dialog/price-room-class-dialog.component";
import {ServiceService} from "../../services/service.service";
import {PriceServiceDialogComponent} from "../price-service-dialog/price-service-dialog.component";
import {DetailPromotionDialogComponent} from "../detail-promotion-dialog/detail-promotion-dialog.component";
import {PromotionService} from "../../services/promotion.service";

@Component({
  selector: 'add-promotion-detail-dialog',
  templateUrl: './add-promotion-detail-dialog.component.html',
  styleUrls: ['./add-promotion-detail-dialog.component.css']
})
export class AddPromotionDetailDialogComponent implements OnInit {
  isSubmit = false;
  isSelectRoomClass = false;
  roomClassList: any;
  chooseRoomClass: any;
  roomClassListFilter: any;

  detailForm = new FormGroup({
    percent: new FormControl('', [Validators.required]),
  });

  constructor(public dialogRef: MatDialogRef<AddPromotionDetailDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private dialog: MatDialog,
              private roomClassService: RoomClassificationService,
              private promotionService: PromotionService,
              private sweetAlertCustomizeService: SweetAlertCustomizeService,
              private dataBindingService: DataBindingService,
              private authAdminService: AuthAdminService,
              ) {
  }

  ngOnInit() {
    console.log(this.data)
    this.getAllRoomClassList()
  }

  getAllRoomClassList() {
    this.roomClassService.getAll().subscribe((resp: any) => {
      if (resp) {
        this.roomClassList = resp;
        console.log(this.roomClassList)
      }
    });
  }

  searchRoomClass(event: any) {
    let filtered: any[] = [];
    let query = event.query;
    for (let i = 0; i < this.roomClassList.length; i++) {
      let roomClass = this.roomClassList[i];
      if (roomClass.id.toLowerCase().indexOf(query.toLowerCase()) == 0
      || roomClass.roomKind.name.toLowerCase().indexOf(query.toLowerCase()) == 0
      || roomClass.roomType.name.toLowerCase().indexOf(query.toLowerCase()) == 0) {
        filtered.push(roomClass);
      }
    }
    this.roomClassListFilter = filtered;
  }

  cancel() {
    this.dialogRef.close();
    this.dialog.open(DetailPromotionDialogComponent, {
      data: this.data,
      disableClose: false
    });
  }

  save() {
    this.isSubmit = true;
    if (this.chooseRoomClass && this.detailForm.valid) {
      const request = {
        percent: this.detailForm.getRawValue().percent,
        roomClassId: this.chooseRoomClass.id,
        promotionId: this.data.id,
      };
      console.log(request)
      this.promotionService.addNewDetail(request).subscribe((resp) => {
          if (resp && resp.result) {
            this.dataBindingService.sendData({
              detailPromotionAdd: true
            });
            this.sweetAlertCustomizeService.showSuccessAlertRightPopup(ALERT_CONTENT.ADD_DETAIL_PROMOTION_SUCCESSFULLY);
            this.dialogRef.close();
            this.dialog.open(DetailPromotionDialogComponent, {
              data: this.data,
              disableClose: false
            });
          }
        },
        (error) => {
          this.sweetAlertCustomizeService.showErrorAlertRightPopup(error.error);
        });
    }
  }

  selectRoomClass() {
    this.isSelectRoomClass = true;
    console.log(this.chooseRoomClass)
  }

  resetChooseRoomClass() {
    this.isSelectRoomClass = false;
    this.chooseRoomClass = null;
  }
}

import {Component, OnInit} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {AddRoomClassificationComponent} from "../add-room-classification/add-room-classification.component";
import {DataBindingService} from "../../../../services/data-binding.service";
import {ServiceService} from "../../services/service.service";
import {PriceServiceDialogComponent} from "../price-service-dialog/price-service-dialog.component";
import {AddServiceComponent} from "../add-service/add-service.component";
import {EditRoomTypeComponent} from "../edit-room-type/edit-room-type.component";
import {EditServiceComponent} from "../edit-service/edit-service.component";
import {SweetAlertCustomizeService} from "../../../../services/sweet-alert-customize.service";
import {ALERT_CONTENT} from "../../../../constant/ALERT_CONTENT";
import {PromotionService} from "../../services/promotion.service";
import {DatePipe} from "@angular/common";
import {AddPromotionComponent} from "../add-promotion/add-promotion.component";
import {EditPromotionComponent} from "../edit-promotion/edit-promotion.component";
import {DetailPromotionDialogComponent} from "../detail-promotion-dialog/detail-promotion-dialog.component";

@Component({
  selector: 'app-promotion-management',
  templateUrl: './promotion-management.component.html',
  styleUrls: ['./promotion-management.component.css']
})
export class PromotionManagementComponent implements OnInit {
  promotions: any;
  displayedColumns: string[] = ['id', 'description', 'startDate', 'endDate'];
  columnHeaders: { [key: string]: string } = {
    id: 'ID',
    description: 'Mô tả',
    startDate: 'Ngày bắt đầu',
    endDate: 'Ngày kết thúc',
  };

  constructor(private promotionService: PromotionService,
              private dialog: MatDialog,
              private dataBindingService: DataBindingService,
              private sweetAlertCustomizeService: SweetAlertCustomizeService,
              private datePipe: DatePipe,
  ) {
  }

  ngOnInit() {
    this.getAllPromotions();
    this.dataBindingService.getData().subscribe((data) => {
      if (data) {
        if (data.promotionSaveStatus) {
          this.getAllPromotions()
        }
        if (data.promotionEditStatus) {
          this.getAllPromotions()
        }
        if (data.detailPromotionAdd) {
          this.getAllPromotions()
        }
        if (data.promotionDelete) {
          this.getAllPromotions()
        }
      }
    });
  }

  openEditDialog(promotion: any) {
    const dialogRef = this.dialog.open(EditPromotionComponent, {
      disableClose: true,
      data: promotion
    });
  }

  getAllPromotions() {
    this.promotionService.getAllPromotions().subscribe((resp: any) => {
      if (resp) {
        this.promotions = resp;
        console.log(this.promotions);
      }
    })
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
    return keyFind.includes('Date') ? this.datePipe.transform(current, 'dd/MM/yyyy') : current;
  }

  addNew() {
    let dialogAddNew = this.dialog.open(AddPromotionComponent, {
      disableClose: true
    });
  }

  openPriceDialog(promotion: any) {
    this.dialog.open(DetailPromotionDialogComponent, {
      data: promotion,
      disableClose: false
    });
  }

  clickDelete(promotion: any) {
    if (!promotion.canDelete) {
      this.sweetAlertCustomizeService.showErrorAlertRightPopup('Không thể xóa dịch vụ này!');
    } else {
      this.promotionService.deletePromotion(promotion.id).subscribe((resp) => {
          if (resp && resp.result) {
            this.dataBindingService.sendData({
              promotionDelete: true
            });
            this.sweetAlertCustomizeService.showSuccessAlertRightPopup('Đã xóa dịch vụ thành công!');
          }
        },
        (error) => {
          this.sweetAlertCustomizeService.showErrorAlertRightPopup(error.error);
        });
    }
  }
}

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
import {SurchargeService} from "../../services/surcharge.service";
import {AddSurchargeComponent} from "../add-surcharge/add-surcharge.component";
import {PriceSurchargeDialogComponent} from "../price-surcharge-dialog/price-surcharge-dialog.component";
import {EditSurchargeComponent} from "../edit-surcharge/edit-surcharge.component";

@Component({
  selector: 'app-surcharge-management',
  templateUrl: './surcharge-management.component.html',
  styleUrls: ['./surcharge-management.component.css']
})
export class SurchargeManagementComponent implements OnInit {
  services: any;
  displayedColumns: string[] = ['id', 'name', 'description', 'priceSurcharge'];
  columnHeaders: { [key: string]: string } = {
    id: 'ID',
    name: 'Tên phụ thu',
    description: 'Mô tả',
    priceSurcharge: 'Giá hiện tại',
  };

  constructor(private surchargeService: SurchargeService,
              private dialog: MatDialog,
              private dataBindingService: DataBindingService,
              private sweetAlertCustomizeService: SweetAlertCustomizeService,
  ) {
  }

  ngOnInit() {
    this.getAllSurcharge();
    this.dataBindingService.getData().subscribe((data) => {
      if (data) {
        if (data.priceSurchargeAdd) {
          this.getAllSurcharge()
        }
        if (data.surchargeSaveStatus) {
          this.getAllSurcharge()
        }
        if (data.surchargeEditStatus) {
          this.getAllSurcharge()
        }
        if (data.surchargeDeleteStatus) {
          this.getAllSurcharge()
        }
      }
    });
  }

  openEditDialog(service: any) {
    const dialogRef = this.dialog.open(EditSurchargeComponent, {
      disableClose: true,
      data: service
    });
  }

  getAllSurcharge() {
    this.surchargeService.getAll().subscribe((resp: any) => {
      if (resp) {
        this.services = resp;
        console.log(this.services);
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
    return current;
  }

  addNew() {
    let dialogAddNew = this.dialog.open(AddSurchargeComponent, {
      disableClose: true
    });
  }

  openPriceDialog(service: any) {
    this.dialog.open(PriceSurchargeDialogComponent, {
      data: service,
      disableClose: false
    });
  }

  clickDelete(service: any) {
    if (!service.canDelete) {
      this.sweetAlertCustomizeService.showErrorAlertRightPopup('Không thể xóa dịch vụ này!');
    } else {
      this.surchargeService.deleteSurcharge(service.id).subscribe((resp) => {
          if (resp && resp.result) {
            this.dataBindingService.sendData({
              surchargeDeleteStatus: true
            });
            this.sweetAlertCustomizeService.showSuccessAlertRightPopup('Đã xóa phụ thu thành công!');
          }
        },
        (error) => {
          this.sweetAlertCustomizeService.showErrorAlertRightPopup(error.error);
        });
    }
  }
}

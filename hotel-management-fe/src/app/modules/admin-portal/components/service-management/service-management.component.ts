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

@Component({
  selector: 'app-service-management',
  templateUrl: './service-management.component.html',
  styleUrls: ['./service-management.component.css']
})
export class ServiceManagementComponent implements OnInit {
  services: any;
  displayedColumns: string[] = ['id', 'name', 'unit', 'description', 'priceService'];
  columnHeaders: { [key: string]: string } = {
    id: 'ID',
    name: 'Tên dịch vụ',
    unit: 'Đơn vị tính',
    description: 'Mô tả',
    priceService: 'Giá hiện tại',
  };

  constructor(private serviceService: ServiceService,
              private dialog: MatDialog,
              private dataBindingService: DataBindingService,
              private sweetAlertCustomizeService: SweetAlertCustomizeService,
  ) {
  }

  ngOnInit() {
    this.getAllServices();
    this.dataBindingService.getData().subscribe((data) => {
      if (data) {
        if (data.priceServiceAdd) {
          this.getAllServices()
        }
        if (data.serviceSaveStatus) {
          this.getAllServices()
        }
        if (data.serviceEditStatus) {
          this.getAllServices()
        }
        if (data.serviceDeleteStatus) {
          this.getAllServices()
        }
      }
    });
  }

  openEditDialog(service: any) {
    const dialogRef = this.dialog.open(EditServiceComponent, {
      disableClose: true,
      data: service
    });
  }

  getAllServices() {
    this.serviceService.getAllServices().subscribe((resp: any) => {
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
    let dialogAddNew = this.dialog.open(AddServiceComponent, {
      disableClose: true
    });
  }

  openPriceDialog(service: any) {
    this.dialog.open(PriceServiceDialogComponent, {
      data: service,
      disableClose: false
    });
  }

  clickDelete(service: any) {
    if (!service.canDelete) {
      this.sweetAlertCustomizeService.showErrorAlertRightPopup('Không thể xóa dịch vụ này!');
    } else {
      this.serviceService.deleteService(service.id).subscribe((resp) => {
          if (resp && resp.result) {
            this.dataBindingService.sendData({
              serviceDeleteStatus: true
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

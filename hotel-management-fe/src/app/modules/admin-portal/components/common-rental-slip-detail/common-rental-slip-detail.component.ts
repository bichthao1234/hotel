import {Component, OnInit} from '@angular/core';
import {ServiceService} from "../../services/service.service";
import {CommonUtil} from "../../../../util/CommonUtil";
import {ActivatedRoute, Router} from "@angular/router";
import {RentalSlipService} from "../../services/rental-slip.service";
import {MatTabChangeEvent} from "@angular/material/tabs";
import {SurchargeService} from "../../services/surcharge.service";
import {SweetAlertCustomizeService} from "../../../../services/sweet-alert-customize.service";
import {AuthAdminService} from "../../services/auth-admin.service";
import {RouterFEConstant} from "../../../../constant/RouterFEConstant";
import {ExportInvoiceDialogComponent} from "../export-invoice-dialog/export-invoice-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {forkJoin, of, switchMap} from "rxjs";

@Component({
  selector: 'app-common-rental-slip-detail',
  templateUrl: './common-rental-slip-detail.component.html',
  styleUrls: ['./common-rental-slip-detail.component.css']
})
export class CommonRentalSlipDetailComponent implements OnInit {
  services: any;
  surcharges: any;
  serviceAndSurchargeInRentalSlips: any;
  data: any;
  rentalSlipId: any;
  rentalSlipDetails: any;
  rentalSlipDetailIds: any;
  roomShow: any;
  showService = true;
  showSurcharge = true;
  deposit: any;
  isAllPaid: any;

  constructor(private serviceService: ServiceService,
              private rentalSlipService: RentalSlipService,
              private route: ActivatedRoute,
              private surchargeService: SurchargeService,
              private sweetAlertCustomizeService: SweetAlertCustomizeService,
              private authAdminService: AuthAdminService,
              private router: Router,
              public dialog: MatDialog,
  ) {
  }

  ngOnInit() {
    this.rentalSlipId = this.route.snapshot.paramMap.get('rentalSlipId');
    forkJoin({
      services: this.getAllServices(),
      surcharges: this.getAllSurcharge(),
      rentalSlipDetail: this.getRentalSlipDetail()
    }).pipe(
      switchMap((responses: any) => {
        this.services = responses.services;
        this.surcharges = responses.surcharges;

        this.rentalSlipDetails = responses.rentalSlipDetail;
        this.deposit = this.rentalSlipDetails[0].deposit;
        this.roomShow = this.rentalSlipDetails[0].roomId;
        this.isAllPaid = this.rentalSlipDetails.every((detail: any) => detail.status === true);
        this.rentalSlipDetailIds = this.rentalSlipDetails.filter((item: any) => !item.status);

        console.log(this.rentalSlipDetailIds)
        this.convertServiceAndSurcharge(this.rentalSlipDetails);

        return of(null);
      })
    ).subscribe();
  }

  getAllServices() {
    return this.serviceService.getAllServices();
  }

  getRentalSlipDetail() {
    return this.rentalSlipService.getRentalSlipDetailListWithRentalSlipId(this.rentalSlipId);
  }

  getPrice(price: any) {
    return price ? CommonUtil.formatAmount(price.toString()) : 0;
  }

  getAllSurcharge() {
    return this.surchargeService.getAll();
  }

  getPriceOfRoom(roomPrice: any, arrivalDate: any, departureDate: any) {
    return Number(this.getDateRange(arrivalDate, departureDate)) * roomPrice;
  }

  getDateRange(from: any, to: any) {
    let rangeDate = CommonUtil.getRangeDate(new Date(from), new Date(to));
    return rangeDate < 1 ? 1 : Number(rangeDate).toFixed(0);
  }

  getPriceFormat(priceOfRoom: any) {
    return CommonUtil.formatAmount(priceOfRoom.toString());
  }

  convertServiceAndSurcharge(rentalSlipDetails: any) {
    this.serviceAndSurchargeInRentalSlips = [];
    for (let rentalSlipDetail of rentalSlipDetails) {
      this.serviceAndSurchargeInRentalSlips.push({
        roomId: rentalSlipDetail.roomId,
        service: rentalSlipDetail.service,
        surcharge: rentalSlipDetail.surcharge
      });
    }
    for (let sR of this.serviceAndSurchargeInRentalSlips) {
      for (let service of sR.service) {
        service.price = this.getPriceService(service.service.id);
      }
      for (let surcharge of sR.surcharge) {
        surcharge.price = this.getPriceSurcharge(surcharge.surcharge.id);
      }
    }
  }

  getServiceWithRoomId(roomId: any) {
    let rentalSlip = this.serviceAndSurchargeInRentalSlips.find((slip: any) => {
      return slip.roomId === roomId;
    });
    return (!rentalSlip.service || !Array.isArray(rentalSlip.service) || rentalSlip.service.length === 0) ? null : rentalSlip.service;
  }

  getSurchargeWithRoomId(roomId: any) {
    let rentalSlip = this.serviceAndSurchargeInRentalSlips.find((slip: any) => {
      return slip.roomId === roomId;
    });
    return (!rentalSlip.surcharge || !Array.isArray(rentalSlip.surcharge) || rentalSlip.surcharge.length === 0) ? null : rentalSlip.surcharge;
  }

  getPriceService(id: any) {
    return this.services?.find((service: any) => {
      return service.id === id;
    })?.priceService;
  }

  getPriceSurcharge(id: any) {
    return this.surcharges?.find((surcharge: any) => {
      return surcharge.id === id;
    })?.priceSurcharge;
  }

  changeQuantityService(roomId: any, id: any, event: any) {
    const quantity = event.target.value;
    let rentalSlip = this.serviceAndSurchargeInRentalSlips.find((slip: any) => slip.roomId === roomId);
    if (rentalSlip) {
      let serviceItem = rentalSlip.service.find((item: any) => item.service.id === id);
      if (serviceItem) {
        serviceItem.quantity = Number.parseInt(quantity.toString());
      } else {
        console.log(`Không tìm thấy service với id: ${id}`);
      }
    } else {
      console.log(`Không tìm thấy rental slip với roomId: ${roomId}`);
    }
  }

  changePaymentStatusService(roomId: any, id: any) {
    let rentalSlip = this.serviceAndSurchargeInRentalSlips.find((slip: any) => slip.roomId === roomId);
    if (rentalSlip) {
      let serviceItem = rentalSlip.service.find((item: any) => item.service.id === id);
      if (serviceItem) {
        serviceItem.status = 1;
      } else {
        console.log(`Không tìm thấy service với id: ${id}`);
      }
    } else {
      console.log(`Không tìm thấy rental slip với roomId: ${roomId}`);
    }
  }

  changePaymentStatusSurcharge(roomId: any, id: any) {
    let rentalSlip = this.serviceAndSurchargeInRentalSlips.find((slip: any) => slip.roomId === roomId);
    if (rentalSlip) {
      let surchargeItem = rentalSlip.surcharge.find((item: any) => item.surcharge.id === id);
      if (surchargeItem) {
        surchargeItem.status = 1;
      } else {
        console.log(`Không tìm thấy surcharge với id: ${id}`);
      }
    } else {
      console.log(`Không tìm thấy rental slip với roomId: ${roomId}`);
    }
  }

  changeQuantitySurcharge(roomId: any, id: any, event: any) {
    const quantity = event.target.value;
    let rentalSlip = this.serviceAndSurchargeInRentalSlips.find((slip: any) => slip.roomId === roomId);
    if (rentalSlip) {
      let surchargeItem = rentalSlip.surcharge.find((item: any) => item.surcharge.id === id);
      if (surchargeItem) {
        surchargeItem.quantity = Number.parseInt(quantity.toString());
      } else {
        console.log(`Không tìm thấy surcharge với id: ${id}`);
      }
    } else {
      console.log(`Không tìm thấy rental slip với roomId: ${roomId}`);
    }
  }

  roomTabChange(event: MatTabChangeEvent) {
    this.roomShow = event.tab.textLabel;
  }

  addService(service: any) {
    let rentalSlip = this.serviceAndSurchargeInRentalSlips.find((slip: any) => slip.roomId === this.roomShow);
    if (rentalSlip) {
      let serviceItem = rentalSlip.service.find((item: any) => item.service.id === service.id);
      if (serviceItem) {
        if (serviceItem.status === 0) {
          serviceItem.quantity += 1;
        } else {
          return;
        }
      } else {
        rentalSlip.service.push({
          service: {
            "id": service.id,
            "name": service.name,
            "description": service.description,
            "unit": service.unit
          },
          quantity: 1,
          price: service.priceService,
          status: 0,
          usingDate: new Date().toISOString()
        });
      }
    } else {
      console.log(`Không tìm thấy rental slip với roomId: ${this.roomShow}`);
    }
  }

  addSurcharge(surcharge: any) {
    let rentalSlip = this.serviceAndSurchargeInRentalSlips.find((slip: any) => slip.roomId === this.roomShow);
    if (rentalSlip) {
      let surchargeItem = rentalSlip.surcharge.find((item: any) => item.surcharge.id === surcharge.id);
      if (surchargeItem) {
        if (surchargeItem.status === 0) {
          surchargeItem.quantity += 1;
        } else {
          return;
        }
      } else {
        rentalSlip.surcharge.push({
          surcharge: {
            "id": surcharge.id,
            "name": surcharge.name,
            "description": surcharge.description,
          },
          quantity: 1,
          price: surcharge.priceSurcharge,
          status: 0,
        });
      }
    } else {
      console.log(`Không tìm thấy rental slip với roomId: ${this.roomShow}`);
    }
  }

  removeService(roomId: string, serviceId: string) {
    let rentalSlip = this.serviceAndSurchargeInRentalSlips.find((slip: any) => slip.roomId === roomId);
    if (rentalSlip) {
      let serviceIndex = rentalSlip.service.findIndex((item: any) => item.service.id === serviceId);
      if (serviceIndex !== -1) {
        rentalSlip.service.splice(serviceIndex, 1);
      }
    }
  }

  removeSurcharge(roomId: string, surchargeId: string) {
    let rentalSlip = this.serviceAndSurchargeInRentalSlips.find((slip: any) => slip.roomId === roomId);
    if (rentalSlip) {
      let surchargeIndex = rentalSlip.surcharge.findIndex((item: any) => item.surcharge.id === surchargeId);
      if (surchargeIndex !== -1) {
        rentalSlip.surcharge.splice(surchargeIndex, 1);
      }
    }
  }

  getTotalPrice() {
    let totalPrice = 0;
    let totalDeposit = 0;
    if (this.rentalSlipDetails && this.surcharges && this.services && this.serviceAndSurchargeInRentalSlips) {
      for (let rentalSlipDetail of this.rentalSlipDetails) {
        if (!rentalSlipDetail.status) {
          totalPrice += rentalSlipDetail.totalRoomPrice
          totalDeposit += rentalSlipDetail.deposit
        }
      }
      for (let service of this.serviceAndSurchargeInRentalSlips) {
        for (let item of service.service) {
          if (item.status === 0) {
            totalPrice += item.quantity * item.price;
          }
        }
      }
      for (let surcharge of this.serviceAndSurchargeInRentalSlips) {
        for (let item of surcharge.surcharge) {
          if (item.status === 0) {
            totalPrice += item.quantity * item.price;
          }
        }
      }
    }
    return totalPrice - totalDeposit;
  }

  changeShowService() {
    this.showService = !this.showService;
  }

  changeSurcharge() {
    this.showSurcharge = !this.showSurcharge;
  }

  isDisplay() {
    return !!this.services && !!this.serviceAndSurchargeInRentalSlips && !!this.surcharges && !!this.rentalSlipDetails && (this.deposit !== null || this.deposit !== undefined);
  }

  paymentServiceDetail(rentalSlipDetail: any, service: any) {
    service.status = 1;
    let serviceDetailDto = this.setServiceDetailDto(rentalSlipDetail, service);
    this.serviceService.saveServiceDetailWithRentalSlipDetail(serviceDetailDto).subscribe((resp: any) => {
      if (resp) {
        this.sweetAlertCustomizeService.showSuccessAlertRightPopup('Thanh toán dịch vụ thành công!');
        this.changePaymentStatusService(rentalSlipDetail.roomId, service.service.id);
      }
    }, (error: any) => {
      this.sweetAlertCustomizeService.showErrorAlertRightPopup(error.error.error);
    });
  }

  paymentSurchargeDetail(rentalSlipDetail: any, surcharge: any) {
    surcharge.status = 1;
    let surchargeDetailDto = this.setSurchargeDetailDto(rentalSlipDetail, surcharge);
    this.surchargeService.saveSurchargeDetailWithRentalSlipDetail(surchargeDetailDto).subscribe((resp: any) => {
      if (resp) {
        this.sweetAlertCustomizeService.showSuccessAlertRightPopup('Thanh toán phụ thu thành công!');
        this.changePaymentStatusSurcharge(rentalSlipDetail.roomId, surcharge.surcharge.id);
      }
    }, (error: any) => {
      this.sweetAlertCustomizeService.showErrorAlertRightPopup(error.error.error);
    });
  }

  saveRentalSlipDetail() {
    console.log(this.rentalSlipDetails);
    let rentalSlipDetailSaveDtos = [];
    for (let rentalSlipDetail of this.rentalSlipDetails) {
      let serviceDetailDtos = [];
      let surchargeDetailDtos = [];
      for (let service of rentalSlipDetail.service) {
        if (service.status === 1) {
          continue;
        } else {
          serviceDetailDtos.push(this.setServiceDetailDto(rentalSlipDetail, service));
        }
      }
      for (let surcharge of rentalSlipDetail.surcharge) {
        if (surcharge.status === 1) {
          continue;
        } else {
          surchargeDetailDtos.push(this.setSurchargeDetailDto(rentalSlipDetail, surcharge));
        }
      }
      let rentalSlipDetailSaveDto = {
        rentalSlipDetailId: rentalSlipDetail.rentalSlipDetailId,
        serviceDetailDtos: serviceDetailDtos,
        surchargeDetailDtos: surchargeDetailDtos
      }
      rentalSlipDetailSaveDtos.push(rentalSlipDetailSaveDto);
    }

    console.log(rentalSlipDetailSaveDtos);
    this.rentalSlipService.saveRentalSlipDetail(rentalSlipDetailSaveDtos).subscribe((resp: any) => {
      if (resp) {
        this.sweetAlertCustomizeService.showSuccessAlertRightPopup('Cập nhật thông tin thành công');
      }
    }, (error: any) => {
      this.sweetAlertCustomizeService.showErrorAlertRightPopup(error.error.error);
    });
  }

  setSurchargeDetailDto(rentalSlipDetail: any, surcharge: any) {
    return {
      surcharge: surcharge.surcharge,
      rentalSlipDetail: {id: rentalSlipDetail.rentalSlipDetailId},
      quantity: surcharge.quantity,
      price: surcharge.price,
      status: surcharge.status,
    }
  }

  setServiceDetailDto(rentalSlipDetail: any, service: any) {
    return {
      service: service.service,
      rentalSlipDetail: {id: rentalSlipDetail.rentalSlipDetailId},
      quantity: service.quantity,
      price: service.price,
      status: service.status,
      note: '',
      usingDate: new Date()
    }
  }

  public readonly CommonUtil = CommonUtil;

  checkRoomStatus() {
    if (this.isDisplay()) {
      let roomStatus = this.rentalSlipDetails.find((rentalDetail: any) => rentalDetail.roomId === this.roomShow)?.roomStatus;
      //nếu phòng đang trống hoặc dơ thì không cho chọn dịch vụ thêm vào chi tiết phiếu thuê
      return roomStatus?.['id'] === 'RS00001' || roomStatus?.['id'] === 'RS00003';
    } else {
      return false;
    }
  }

  handlePayment() {
    const request = {
      rentalSlipDetailIdList: this.rentalSlipDetails.map((item: any) => item?.rentalSlipDetailId),
      employeeId: this.authAdminService.getLoginAdminInfor()?.id
    }
    this.rentalSlipService.checkOut(request).subscribe((resp: any) => {
        if (resp) {
          this.sweetAlertCustomizeService.showSuccessAlertRightPopup(`Check out thành công!`);
          this.router.navigate(
            [RouterFEConstant.ADMIN_PORTAL.path,
              RouterFEConstant.ADMIN_PORTAL_ROOM_DIAGRAM.path]
          ).then(() => {
          });
        }
      },
      error => {
        this.sweetAlertCustomizeService.showErrorAlertRightPopup(error.error.error);
      });
  }

  openDialog() {
    this.dialog.open(ExportInvoiceDialogComponent, {
      data: {
        rentalSlipDetailIds: this.rentalSlipDetailIds,
        isForPayment: true,
        isExportInvoice: false
      },
      disableClose: true
    })
  }
}

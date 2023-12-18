import {Component, OnInit} from '@angular/core';
import {ReservationService} from "../../services/reservation.service";
import {GetReservationListRequestDto} from "../../models/GetReservationListRequestDto";
import {FormControl, Validators} from "@angular/forms";
import {MatDialog} from "@angular/material/dialog";
import {DetailReservationsComponent} from "../detail-reservations/detail-reservations.component";
import {RouterFEConstant} from "../../../../constant/RouterFEConstant";
import {Router} from "@angular/router";
import {ReservationEditRangeDateComponent} from "../reservation-edit-range-date/reservation-edit-range-date.component";
import {AuthAdminService} from "../../services/auth-admin.service";
import {SweetAlertCustomizeService} from "../../../../services/sweet-alert-customize.service";
import {CustomerAdminService} from "../../services/customer-admin.service";

@Component({
  selector: 'app-reservation',
  templateUrl: './reservation.component.html',
  styleUrls: ['./reservation.component.css']
})
export class ReservationComponent implements OnInit {
  reservationStatus = [
    {id: 1, name: 'Đã đặt'},
    {id: 0, name: 'Đã hủy'},
    {id: 2, name: 'Đã thuê'}
  ];
  isToday = false;
  getReservationListRequestDto!: GetReservationListRequestDto;
  startDateFrom: any;
  startDateTo: any;
  createdDateFrom: any;
  createdDateTo: any;
  customerId = new FormControl(null, [Validators.required]);
  reservations: any;
  reservationsShow: any;
  isBtnCustomerId = false;
  selectedStatus: any;
  chooseCustomer: any;
  customerListFilter: any;
  customerList: any;

  constructor(private reservationService: ReservationService,
              private dialog: MatDialog,
              private router: Router,
              private authAdminService: AuthAdminService,
              private sweetAlertCustomizeService: SweetAlertCustomizeService,
              private customerAdminService: CustomerAdminService,
              ) {
  }

  ngOnInit() {
    this.getReservationList();
    this.getAllCustomerList();
    // this.customerId.valueChanges.subscribe(() => {
    //   if (this.customerId.value === '') {
    //     this.customerId.setValue(null);
    //     this.isBtnCustomerId = false;
    //     this.getReservationList();
    //   }
    // });
  }

  getAllCustomerList() {
    this.customerAdminService.getAllCustomers().subscribe((resp: any) => {
      if (resp) {
        this.customerList = resp;
      }
    });
  }

  openReservationDetailDialog(reservationItem: any) {
    let dialog = this.dialog.open(DetailReservationsComponent, {
      data: reservationItem
    });
    dialog.afterClosed().subscribe((data: any) => {
      if (data.statusOnReload) {
        this.ngOnInit();
      }
    });
  }

  openReservationEditRangeDateComponent(reservationItem: any) {
    let dialog = this.dialog.open(ReservationEditRangeDateComponent, {
      data: reservationItem
    });
    dialog.afterClosed().subscribe((data: any) => {
      if (data.statusOnReload) {
        this.ngOnInit();
      }
    });
  }

  setReservationListRequestDto() {
    const req: GetReservationListRequestDto = {
      startDateFrom: this.startDateFrom,
      startDateTo: this.startDateTo,
      createdDateFrom: this.createdDateFrom,
      createdDateTo: this.createdDateTo,
      customerId: this.chooseCustomer?.id,
      status: this.selectedStatus?.name
    };
    return req;
  }


  getReservationList() {
    this.getReservationListRequestDto = this.setReservationListRequestDto();
    this.reservationService.getReservationList(this.getReservationListRequestDto).subscribe((resp) => {
      if (resp) {
        this.reservations = resp;
        if (this.isToday) {
          this.reservationsShow = this.showReservationsToday(this.reservations);
        } else {
          this.reservationsShow = this.reservations;
        }
      }
    });
  }

  getStartDate(event: any) {
    if (!event.start || !event.end) {
      this.startDateFrom = null;
      this.startDateTo = null;
    } else {
      this.startDateFrom = new Date(event.start);
      this.startDateTo = new Date(event.end);
    }
    this.getReservationList();
  }

  getCreatedDate(event: any) {
    if (!event.start || !event.end) {
      this.createdDateFrom = null;
      this.createdDateTo = null;
    } else {
      this.createdDateFrom = new Date(event.start);
      this.createdDateTo = new Date(event.end);
    }
    this.getReservationList();
  }

  changeDateInput() {
    if (this.isToday) {
      this.startDateFrom = null;
      this.startDateTo = null;
      this.createdDateFrom = null;
      this.createdDateTo = null;
    }
    this.getReservationList();
  }

  showReservationsToday(reservations: any) {
    let today = new Date();
    today.setHours(0, 0, 0, 0);

    let reservationsShow = reservations.filter((item: any) => {
      let startDate = new Date(item.startDate);
      startDate.setHours(0, 0, 0, 0);
      return startDate.getTime() === today.getTime();
    });
    return reservationsShow;
  }

  searchReservationCustomId() {
    this.isBtnCustomerId = true;
    if (this.customerId.valid) {
      this.getReservationList();
    }
  }

  getClassOfStatus(status: any) {
    switch (status.value) {
      case 0: {
        return 'status-cancel';
      }
      case 1: {
        return 'status-reserved';
      }
      case 2: {
        return 'status-rented';
      }
      case 3: {
        return 'status-overtime';
      }
      default: {
        return '';
      }
    }
  }

  checkStatus(reservation: any, status: any) {
    if (typeof status === "number") {
      return reservation?.status?.value === status
    }
    if (typeof status === "object" || "array") {
      return reservation?.status?.value in status
    }
    return false
  }

  routeToRentalDetail(id: any) {
    this.reservationService.getRentalSlipId(id).subscribe(resp => {
      const rentalSlipId = resp.response;
      this.router.navigate(
        [RouterFEConstant.ADMIN_PORTAL.path,
          RouterFEConstant.ADMIN_PORTAL_COMMON_RENTAL_SLIP.path, rentalSlipId]
      ).then(() => {
      });
    });
  }

  cancelReservation(reservation: any) {
    const employeeId = this.authAdminService.getLoginAdminInfor()?.id
    this.reservationService.cancelReservation(reservation.id, employeeId).subscribe(() => {
      this.ngOnInit();
    }, error => {
      console.log(error)
      this.sweetAlertCustomizeService.showErrorAlertRightPopup(error.error.error);
    });
  }

  handleClick(reservation: any) {
    if (reservation?.status?.value === 0) {
      this.openReservationDetailDialog(reservation)
    }
    if (reservation?.status?.value === 2) {
      this.routeToRentalDetail(reservation?.id)
    }
  }

  searchRepresentativeCustomer(event: any) {
    let filtered: any[] = [];
    let query = event.query;
    for (let i = 0; i < this.customerList.length; i++) {
      let customer = this.customerList[i];
      if (customer.id.toLowerCase().indexOf(query.toLowerCase()) == 0) {
        filtered.push(customer);
      }
    }
    this.customerListFilter = filtered;
  }

  handleInput(event: any) {
    const value = event.target.value;
    if (!/^\d*$/.test(value)) {
      event.target.value = value.replace(/[^0-9]/g, '');
    }
  }

  resetCustomer() {
    this.chooseCustomer = null;
    this.getReservationList();
  }
}

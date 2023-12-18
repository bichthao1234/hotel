import {Injectable} from '@angular/core';
import {HttpDAOService} from "../../../services/http-dao.service";
import {APIConstant} from "../../../constant/api-constant";

@Injectable({
  providedIn: 'root'
})
export class ReservationService {

  constructor(private httpDAOService: HttpDAOService) {
  }

  getReservationList(req: any) {
    return this.httpDAOService.doPost(APIConstant.GET_RESERVATION_LIST, req);
  }

  getDetailReservationList(reservationId: any) {
    return this.httpDAOService.doGet(APIConstant.GET_DETAIL_RESERVATION_LIST,
      {reservationId: reservationId});
  }

  getRentalSlipId(reservationId: any) {
    return this.httpDAOService.doGet(APIConstant.GET_DETAIL_RENTAL_BY_RESERVATION,
      {reservationId: reservationId});
  }

  cancelReservation(reservationId: any, employeeId: any) {
    return this.httpDAOService.doPut(APIConstant.CANCEL_RESERVATION,
      {}, {reservationId: reservationId, employeeId: employeeId});
  }

  changeDateRangeReservation(req: any) {
    return this.httpDAOService.doPut(APIConstant.CHANGE_DATE_RANGE_RESERVATION, req);
  }
}

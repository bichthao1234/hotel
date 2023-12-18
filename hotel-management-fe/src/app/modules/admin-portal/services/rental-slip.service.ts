import {Injectable} from '@angular/core';
import {HttpDAOService} from "../../../services/http-dao.service";
import {APIConstant} from "../../../constant/api-constant";

@Injectable({
  providedIn: 'root'
})
export class RentalSlipService {

  constructor(private httpDAOService: HttpDAOService) {
  }

  convertFromReservation(req: any) {
    return this.httpDAOService.doPost(APIConstant.CONVERT_FROM_RESERVATION, req);
  }

  getRentalSlipDetailWithReservation(reservationId: any) {
    return this.httpDAOService.doGet(APIConstant.GET_RENTAL_SLIP_DETAIL_WITH_RESERVATION, {reservationId: reservationId})
  }

  getAlRentalSlip(customerId: any) {
    return this.httpDAOService.doPost(APIConstant.GET_ALL_RENTAL_SLIP, customerId);
  }

  getAlRentalSlipForExportInvoice(customerId: any) {
    return this.httpDAOService.doGet(APIConstant.GET_ALL_RENTAL_SLIP_FOR_EXPORT_INVOICE, {customerId: customerId});
  }

  getRentalSlipDetailListWithRentalSlipId(rentalSlipId: any) {
    return this.httpDAOService.doGet(APIConstant.GET_RENTAL_SLIP_DETAIL_WITH_RENTAL_SLIP_ID,
      {rentalSlipId: rentalSlipId});
  }

  getRentalSlipDetailWithRentalSlipId(getListRentalDetailRequestDto: any) {
    return this.httpDAOService.doPost(APIConstant.GET_RENTAL_SLIP_DETAIL_WITH_RENTAL_SLIP_DETAIL_ID,
      getListRentalDetailRequestDto);
  }

  quickCheckIn(quickCheckInRequestDto: any) {
    return this.httpDAOService.doPost(APIConstant.QUICK_CHECK_IN, quickCheckInRequestDto)
  }

  saveRentalSlipDetail(rentalSlipDetailSaveDto: any) {
    return this.httpDAOService.doPost(APIConstant.SAVE_RENTAL_SLIP_DETAIL, rentalSlipDetailSaveDto);
  }

  checkOut(req: any) {
    return this.httpDAOService.doPost(APIConstant.CHECK_OUT_RENTAL_SLIP, req);
  }

  getReport(req: any) {
    return this.httpDAOService.doPost(APIConstant.GET_REPORT, req);
  }
}

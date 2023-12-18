import {Injectable} from '@angular/core';
import {HttpDAOService} from "../../../services/http-dao.service";
import {APIConstant} from "../../../constant/api-constant";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class SurchargeService {

  constructor(private httpDAOService: HttpDAOService) {
  }

  getAll() {
    return this.httpDAOService.doGet(APIConstant.GET_ALL_SURCHARGE);
  }

  getSurchargeWithId(id: any) {
    return this.httpDAOService.doGet(APIConstant.GET_SURCHARGE_WITH_ID, {id: id});
  }

  saveSurchargeDetailWithRentalSlipDetail(surchargeDetailDto: any) {
    return this.httpDAOService.doPost(APIConstant.SAVE_SURCHARGE_DETAIL_WITH_RENTAL_SLIP_DETAIL, surchargeDetailDto);
  }

  addNewPrice(req: any) {
    return this.httpDAOService.doPost(APIConstant.ADD_NEW_SURCHARGE_PRICE, req)
  }

  saveSurcharge(service: any): Observable<any> {
    return this.httpDAOService.doPost(APIConstant.CREATE_NEW_SURCHARGE, service);
  }

  editSurcharge(service: any) {
    return this.httpDAOService.doPut(APIConstant.EDIT_NEW_SURCHARGE, service);
  }

  deleteSurcharge(id: any) {
    return this.httpDAOService.doPut(APIConstant.DELETE_NEW_SURCHARGE, {}, {id: id})
  }
}

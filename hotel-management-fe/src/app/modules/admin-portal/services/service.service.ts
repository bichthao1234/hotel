import { Injectable } from '@angular/core';
import {HttpDAOService} from "../../../services/http-dao.service";
import {APIConstant} from "../../../constant/api-constant";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ServiceService {

  constructor(private httpDAOService: HttpDAOService) { }

  getAllServices() {
    return this.httpDAOService.doGet(APIConstant.GET_ALL_SERVICES);
  }

  getServiceWithId(id: any) {
    return this.httpDAOService.doGet(APIConstant.GET_SERVICE_WITH_ID, {id: id});
  }

  payForService(req: any) {
    return this.httpDAOService.doPut(APIConstant.PAY_FOR_SERVICE, req);
  }

  saveServiceDetailWithRentalSlipDetail(serviceDetailDto:any) {
    return this.httpDAOService.doPost(APIConstant.SAVE_SERVICE_DETAIL_WITH_RENTAL_SLIP_DETAIL, serviceDetailDto);
  }

  addNewPrice(req: any) {
    return this.httpDAOService.doPost(APIConstant.ADD_NEW_SERVICE_PRICE, req)
  }

  saveService(service: any): Observable<any> {
    return this.httpDAOService.doPost(APIConstant.CREATE_NEW_SERVICE, service);
  }

  editService(service: any) {
    return this.httpDAOService.doPut(APIConstant.EDIT_NEW_SERVICE, service);
  }

  deleteService(id: any) {
    return this.httpDAOService.doPut(APIConstant.DELETE_NEW_SERVICE, {}, {id: id})
  }
}

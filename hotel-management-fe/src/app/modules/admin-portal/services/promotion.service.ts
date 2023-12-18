import { Injectable } from '@angular/core';
import {HttpDAOService} from "../../../services/http-dao.service";
import {APIConstant} from "../../../constant/api-constant";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class PromotionService {

  constructor(private httpDAOService: HttpDAOService) { }

  getAllPromotions() {
    return this.httpDAOService.doGet(APIConstant.GET_ALL_PROMOTIONS);
  }

  getWithId(id: any) {
    return this.httpDAOService.doGet(APIConstant.GET_PROMOTION_WITH_ID, {id: id});
  }

  saveService(request: any) {
    return this.httpDAOService.doPost(APIConstant.CREATE_NEW_PROMOTION, request);
  }

  editService(request: any) {
    return this.httpDAOService.doPut(APIConstant.EDIT_PROMOTION, request);
  }

  addNewDetail(request: any) {
    return this.httpDAOService.doPost(APIConstant.ADD_NEW_PROMOTION_DETAIL, request);
  }

  deletePromotion(id: any) {
    return this.httpDAOService.doPut(APIConstant.DELETE_NEW_PROMOTION, {}, {id: id})
  }
}

import { Injectable } from '@angular/core';
import {HttpDAOService} from "../../../services/http-dao.service";
import {Observable} from "rxjs";
import {APIConstant} from "../../../constant/api-constant";

@Injectable({
  providedIn: 'root'
})
export class ConvenienceService {

  constructor(private httpDAOService: HttpDAOService) { }

  getAll(): Observable<any> {
    return this.httpDAOService.doGet(APIConstant.GET_ALL_CONVENIENCE);
  }
  saveConvenience(convenience: any): Observable<any> {
    return this.httpDAOService.doPost(APIConstant.ADD_NEW_CONVENIENCE, convenience);
  }
  editConvenience(convenience: any): Observable<any> {
    return this.httpDAOService.doPost(APIConstant.EDIT_CONVENIENCE, convenience);
  }

  getByName(name: any): Observable<any> {
    return this.httpDAOService.doPost(APIConstant.GET_CONVENIENCE_BY_NAME, name);
  }
}

import { Injectable } from '@angular/core';
import {HttpDAOService} from "../../../services/http-dao.service";
import {Observable} from "rxjs";
import {APIConstant} from "../../../constant/api-constant";

@Injectable({
  providedIn: 'root'
})
export class RoomStatusService {

  constructor(private httpDAOService: HttpDAOService) { }

  getAll(): Observable<any> {
    return this.httpDAOService.doGet(APIConstant.GET_ALL_ROOM_STATUS);
  }
  
}

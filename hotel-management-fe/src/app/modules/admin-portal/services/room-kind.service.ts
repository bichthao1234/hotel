import { Injectable } from '@angular/core';
import {HttpDAOService} from "../../../services/http-dao.service";
import {Observable} from "rxjs";
import {APIConstant} from "../../../constant/api-constant";

@Injectable({
  providedIn: 'root'
})
export class RoomKindService {

  constructor(private httpDAOService: HttpDAOService) { }

  getAll(): Observable<any> {
    return this.httpDAOService.doGet(APIConstant.GET_ALL_ROOM_KIND);
  }
  saveRoomKind(roomKind: any): Observable<any> {
    return this.httpDAOService.doPost(APIConstant.ADD_NEW_ROOM_KIND, roomKind);
  }
  editRoomKind(roomType: any): Observable<any> {
    return this.httpDAOService.doPost(APIConstant.EDIT_ROOM_KIND, roomType);
  }

  getByName(name: any): Observable<any> {
    return this.httpDAOService.doPost(APIConstant.GET_ROOM_KIND_BY_NAME, name);
  }
}

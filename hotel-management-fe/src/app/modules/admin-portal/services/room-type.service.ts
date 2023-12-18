import { Injectable } from '@angular/core';
import {HttpDAOService} from "../../../services/http-dao.service";
import {Observable} from "rxjs";
import {APIConstant} from "../../../constant/api-constant";

@Injectable({
  providedIn: 'root'
})
export class RoomTypeService {

  constructor(private httpDAOService: HttpDAOService) { }

  getAll(): Observable<any> {
    return this.httpDAOService.doGet(APIConstant.GET_ALL_ROOM_TYPE);
  }
  saveRoomType(roomType: any): Observable<any> {
    return this.httpDAOService.doPost(APIConstant.ADD_NEW_ROOM_TYPE, roomType);
  }
  editRoomType(roomType: any): Observable<any> {
    return this.httpDAOService.doPost(APIConstant.EDIT_ROOM_TYPE, roomType);
  }

  getByName(name: any): Observable<any> {
    return this.httpDAOService.doPost(APIConstant.GET_ROOM_TYPE_BY_NAME, name);
  }
}

import {Injectable} from '@angular/core';
import {HttpDAOService} from "../../../services/http-dao.service";
import {APIConstant} from "../../../constant/api-constant";

@Injectable({
  providedIn: 'root'
})
export class RoomService {

  constructor(private httpDAOService: HttpDAOService) {
  }

  getRooms(req: any) {
    return this.httpDAOService.doPost(APIConstant.GET_ROOM_LIST, req);
  }

  getRoomRentalDetail(id: any) {
    return this.httpDAOService.doGet(APIConstant.GET_ROOM_RENTAL_DETAIL, {id: id})
  }

  getRoomRentalGuest(id: any) {
    return this.httpDAOService.doGet(APIConstant.GET_ROOM_RENTAL_GUEST, {id: id})
  }

  getRoomRentalService(id: any) {
    return this.httpDAOService.doGet(APIConstant.GET_ROOM_RENTAL_SERVICE, {id: id})
  }

  getRoomRentalSurcharge(id: any) {
    return this.httpDAOService.doGet(APIConstant.GET_ROOM_RENTAL_SURCHARGE, {id: id})
  }

  getRoomAvailableList(roomClassId: any) {
    return this.httpDAOService.doGet(APIConstant.GET_ROOM_AVAILABLE_LIST, {roomClassId: roomClassId});
  }

  changeStatus(roomId: any, statusId: any) {
    return this.httpDAOService.doPost(APIConstant.CHANGE_ROOM_STATUS, {
      roomId: roomId,
      statusId: statusId
    });
  }

  createNewRoom(request: any) {
    return this.httpDAOService.doPost(APIConstant.CREATE_NEW_ROOM, request);
  }

  modifyRoom(request: any) {
    return this.httpDAOService.doPost(APIConstant.MODIFY_ROOM, request);
  }
}

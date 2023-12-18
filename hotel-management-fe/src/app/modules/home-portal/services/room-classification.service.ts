import {Injectable} from '@angular/core';
import {HttpDAOService} from "../../../services/http-dao.service";
import {APIConstant} from "../../../constant/api-constant";

@Injectable({
  providedIn: 'root'
})
export class RoomClassificationService {

  constructor(private httpDAOService: HttpDAOService) {
  }

  getRoomClassification(req: any) {
    return this.httpDAOService.doPost(APIConstant.GET_ROOM_CLASSIFICATION_LIST, req);
  }

  makeReservation(req: any) {
    return this.httpDAOService.doPost(APIConstant.MAKE_RESERVATION, req);
  }

  getRoomClassificationWithId(roomClassId: any) {
    return this.httpDAOService.doGet(APIConstant.GET_ROOM_CLASSIFICATION_WITH_ID, {id: roomClassId});
  }

  getPrice(roomClassId: any, startDate: Date) {
    return this.httpDAOService.doPost(APIConstant.GET_PRICE_ROOM_CLASSIFICATION_WITH_ID, {
      roomClassId: roomClassId,
      startDate: startDate
    });
  }

  getImage(roomClassId: any) {
    return this.httpDAOService.doGet(APIConstant.GET_IMAGE_ROOM_CLASSIFICATION_WITH_ID, {
      roomClassId: roomClassId
    });
  }

  getAll() {
    return this.httpDAOService.doGet(APIConstant.GET_ALL_ROOM_CLASSIFICATION);
  }

  createNewRoomClassification(request: any) {
    return this.httpDAOService.doPost(APIConstant.CREATE_NEW_ROOM_CLASSIFICATION, request);
  }

  editRoomClassification(request: any) {
    return this.httpDAOService.doPost(APIConstant.EDIT_ROOM_CLASSIFICATION, request);
  }

  updateImageRoomClassification(roomClassificationId: any, imageFile: File) {
    const formData: FormData = new FormData();
    formData.append('image', imageFile);
    formData.append('roomClassificationId', roomClassificationId);
    return this.httpDAOService.doPostWithOutApplicationJson(APIConstant.IMAGE_ROOM_CLASSIFICATION, formData);
  }

  getAllPrice() {
    return this.httpDAOService.doGet(APIConstant.GET_ALL_ROOM_CLASSIFICATION_PRICE);
  }

  addNewPrice(req: any) {
    return this.httpDAOService.doPost(APIConstant.ADD_NEW_ROOM_CLASSIFICATION_PRICE, req)
  }
}

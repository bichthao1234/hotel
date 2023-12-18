import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {HttpDAOService} from "../../../services/http-dao.service";
import {APIConstant} from "../../../constant/api-constant";

@Injectable({
  providedIn: 'root'
})
export class CustomerAdminService {
  constructor(private httpClient: HttpClient,
              private httpDAOService: HttpDAOService) {
  }


  getAllCustomers() {
    return this.httpDAOService.doGet(APIConstant.GET_CUSTOMER_LIST);
  }

  checkCustomerWithId(id: any) {
    return this.httpDAOService.doGet(APIConstant.CHECK_CUSTOMER_WITH_ID, {id: id});
  }

  createCustomer(req: any) {
    return this.httpDAOService.doPost(APIConstant.CREATE_CUSTOMER, req);
  }
}

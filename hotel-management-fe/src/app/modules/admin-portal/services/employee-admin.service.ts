import {Injectable} from '@angular/core';
import {HttpHeaders} from "@angular/common/http";
import {HttpDAOService} from "../../../services/http-dao.service";
import {APIConstant} from "../../../constant/api-constant";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class EmployeeAdminService {
  constructor(private httpDAOService: HttpDAOService) {
  }


  getEmployee(username: any): Observable<any> {
    return this.httpDAOService.doGet(APIConstant.GET_EMPLOYEE, {username});
  }
}

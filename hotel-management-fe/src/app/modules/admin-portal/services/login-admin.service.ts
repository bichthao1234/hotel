import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {HttpDAOService} from "../../../services/http-dao.service";
import {APIConstant} from "../../../constant/api-constant";

@Injectable({
  providedIn: 'root'
})
export class LoginAdminService {
  private httpOptions = {
    headers: new HttpHeaders().set(`Content-Type`, `application/json`),
  };
  constructor(private httpDAOService: HttpDAOService) { }
  public loginAdmin(login: any): Observable<any> {
    return this.httpDAOService.doPost(APIConstant.ADMIN_LOGIN, login);
  }
}

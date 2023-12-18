import {Injectable} from '@angular/core';
import {FileUpload} from "../models/file-upload";
import {HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {Customer} from "../models/customer";
import {HttpDAOService} from "../../../services/http-dao.service";
import {APIConstant} from "../../../constant/api-constant";

@Injectable({
  providedIn: 'root'
})
export class ImportCustomerService {
  header = new HttpHeaders();
  httpOptions = {}

  constructor(private httpDAOService: HttpDAOService) {
  }

  importCustomer(customers: Customer[]): Observable<any> {
    return this.httpDAOService.doPost(APIConstant.IMPORT_CUSTOMER_BY_FILE, customers);
  }

  previewFile(file: FileUpload, sheetName: string): Observable<any> {
    var formData = new FormData();
    // @ts-ignore
    formData.append('file', file.file);
    formData.append('sheetName', sheetName);
    return this.httpDAOService.doPostWithOutApplicationJson(APIConstant.PREVIEW_FILE_CUSTOMER, formData);
  }
}

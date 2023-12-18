import { Injectable } from '@angular/core';
import {HttpDAOService} from "../../../services/http-dao.service";
import {APIConstant} from "../../../constant/api-constant";

@Injectable({
  providedIn: 'root'
})
export class InvoiceService {

  constructor(private httpDAOService: HttpDAOService) { }

  getInvoiceById(invoiceId: any) {
    return this.httpDAOService.doGet(APIConstant.GET_INVOICE_BY_ID, {invoiceId: invoiceId});
  }

  getAll() {
    return this.httpDAOService.doGet(APIConstant.GET_ALL_INVOICE);
  }

  createInvoice(req: any) {
    return this.httpDAOService.doPost(APIConstant.CREATE_INVOICE, req);
  }
}

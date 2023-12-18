import {Injectable} from '@angular/core';
import {HttpDAOService} from "../../../services/http-dao.service";
import {APIConstant} from "../../../constant/api-constant";

@Injectable({
  providedIn: 'root'
})
export class PaymentService {
  apiExchangeRate = 'https://www.dongabank.com.vn/exchange/export';

  constructor(private httpDAOService: HttpDAOService) {
  }

  getExchangeRate() {
    return this.httpDAOService.doGet(APIConstant.GET_EXCHANGE_RATE);
  }
}

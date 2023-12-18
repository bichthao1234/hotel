import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {AuthAdminService} from "../modules/admin-portal/services/auth-admin.service";
import {API} from "../models/API";
import {Observable} from "rxjs";
import {environment} from "../../environments/environment";
import {StringUtil} from "../shared/util/StringUtil";

@Injectable({
  providedIn: 'root'
})
export class HttpDAOService {

  private headerApplicationJson = new HttpHeaders({
    'Content-Type': 'application/json',
  });
  private headerMultipart = new HttpHeaders({
    'Content-Type': 'multipart/form-data',
  });
  private headerStream = new HttpHeaders({
    'Content-Type': 'application/octet-stream'
  });
  private headerAuthorization = new HttpHeaders();

  private httpMultiPartOptions = {
    headers: this.headerMultipart
  }
  private httpNormalOptions: any = {
    headers: this.headerAuthorization
  };
  private httpApplicationJsonOptions: any = {
    headers: this.headerApplicationJson
  };
  private httpBlobOptions: any = {
    headers: this.headerStream,
    responseType: 'blob'
  };
  private httpBlobJson: any = {
    headers: this.headerApplicationJson,
    responseType: 'blob'
  };

  constructor(private http: HttpClient, private authAdminService: AuthAdminService) {
  }

  doGet(pathAPI: API, params?: {}): Observable<any> {
    const href = this.doBuilderURLAPI(pathAPI, params);
    return this.http.get<any[]>(href, this.httpNormalOptions);
  }

  doPost(pathAPI: API, body: any, params?: {}): Observable<any> {
    const href = this.doBuilderURLAPI(pathAPI, params);
    return this.http.post<any[]>(href, body, this.httpApplicationJsonOptions);
  }

  doPut(pathAPI: API, body?: any, params?: {}): Observable<any> {
    const href = this.doBuilderURLAPI(pathAPI, params);
    return this.http.put<any[]>(href, body, this.httpApplicationJsonOptions);
  }

  doPostWithOutApplicationJson(pathAPI: API, body: any, params?: {}): Observable<any> {
    const href = this.doBuilderURLAPI(pathAPI, params);
    return this.http.post<any[]>(href, body, this.httpNormalOptions);
  }
  doPostMultiPartFile(pathAPI: API, body: any, params?: {}): Observable<any> {
    const href = this.doBuilderURLAPI(pathAPI, params);
    return this.http.post<any[]>(href, body, this.httpMultiPartOptions);
  }

  public doBuilderURLAPI(pathAPI: API, params?: {}) {
    let apiFull = environment.rootURL;
    apiFull += pathAPI.module;
    let tmpURI = pathAPI.uri;
    if (params) {
      tmpURI = StringUtil.formatString(tmpURI, params);
    }
    return (apiFull + tmpURI);
  }

}

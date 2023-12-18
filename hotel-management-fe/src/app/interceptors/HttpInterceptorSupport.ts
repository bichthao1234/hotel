import {
  HTTP_INTERCEPTORS,
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest
} from "@angular/common/http";
import {catchError, Observable, tap} from "rxjs";
import {LoadingService} from "../services/loading.service";
import {AuthAdminService} from "../modules/admin-portal/services/auth-admin.service";
import {Injectable} from "@angular/core";
import {throwMatMenuRecursiveError} from "@angular/material/menu/menu-errors";
import { throwError} from 'rxjs'

@Injectable()
export class HttpInterceptorSupport implements HttpInterceptor {
  constructor(private loadingService: LoadingService,
              private authAdminService: AuthAdminService) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    this.loadingService.pushProcessing(req.clone());
    const tmpHeaders = req.clone().headers;
    req.clone({
      withCredentials: true,
      headers: tmpHeaders
    });
    if (this.authAdminService.isLoginAdmin()) {
      req = this.injectTokenToHeader(req);
    }
    // Gửi request đã được chỉnh sửa tới server
    return this.injectHttp(req, next).pipe(
      catchError((resp: HttpErrorResponse) => {
        this.loadingService.removeProcessing(resp);
        return throwError(resp)
      }),
      tap(
        (resp) => {
          this.loadingService.removeProcessing(resp);
        }
      )
    );
  }

  private injectTokenToHeader(req: HttpRequest<any>) {
    const headerName = 'Authorization';
    const token = this.authAdminService.getLoginAdminInfor()?.token;
    // Clone request và thêm Authorization header
    if (token != null) {
      req = req.clone({
        headers: req.headers.set(headerName, `Bearer ${token}`)
      });
    }
    return req;
  }

  private injectHttp(req: HttpRequest<any>, next: HttpHandler) {
    return next.handle(req);
  }
}

export const HttpInterceptorProviders = [
  {provide: HTTP_INTERCEPTORS, useClass: HttpInterceptorSupport, multi: true}
];

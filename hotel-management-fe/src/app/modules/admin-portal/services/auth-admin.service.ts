import {Injectable} from '@angular/core';
import {AdminInforLogin} from "../models/AdminInforLogin";

@Injectable({
  providedIn: 'root'
})
export class AuthAdminService {

  constructor() {
  }

  public isLoginAdmin(): boolean {
    return sessionStorage.getItem('loginAdminResp') ? true : false;
  }

  public setLoginAdminInfor(resp: any) {
    const loginAdminResp = {
      id: resp.employeeId,
      username: resp.username,
      token: resp.token,
      refreshToken: resp.refreshToken,
      permissions: resp.permissions,
      fullName: resp.fullName,
      isManager: resp.isManager
    };
    sessionStorage.removeItem('loginAdminResp')
    sessionStorage.setItem('loginAdminResp', JSON.stringify(loginAdminResp));
  }

  public getLoginAdminInfor(): AdminInforLogin | null {
    const loginAdminInforSS = sessionStorage.getItem('loginAdminResp') ?? null;
    if (loginAdminInforSS) {
      return JSON.parse(loginAdminInforSS.toString()) as AdminInforLogin;
    } else {
      return null;
    }
  }

  public getPermissions() {
    let loginAdminInfor = this.getLoginAdminInfor();
    return loginAdminInfor ? loginAdminInfor.permissions : null;
  }

  public isManager() {
    return this.getLoginAdminInfor()?.isManager;
  }

  logOut() {
    sessionStorage.removeItem('loginAdminResp')
  }
}

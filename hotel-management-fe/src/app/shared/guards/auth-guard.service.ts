import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {AuthAdminService} from "../../modules/admin-portal/services/auth-admin.service";
import {RouterFEConstant} from "../../constant/RouterFEConstant";

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(private authAdminService: AuthAdminService,
              private router: Router) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    if (route.url) {
      let checkAdminPortal = false;
      let isLoginAdmin = false;
      for (let url of route.url) {
        if (url.path === RouterFEConstant.ADMIN_PORTAL.path) {
          checkAdminPortal = true;
          isLoginAdmin = this.authAdminService.isLoginAdmin();
        }
      }
      if (checkAdminPortal) {
        if (!isLoginAdmin) {
          this.router.navigate([RouterFEConstant.ADMIN_PORTAL.path, RouterFEConstant.LOGIN.path]).then(r => location.reload());
          return false;
        } else  {
          return true;
        }
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

}

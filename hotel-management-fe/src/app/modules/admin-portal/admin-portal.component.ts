import {Component, OnInit} from '@angular/core';
import {MENU_ADMIN_ITEM} from "./menu-admin";
import {MENU_RECEPTIONIST_ITEM} from "./menu-receptionist";
import {RouterFEConstant} from "../../constant/RouterFEConstant";
import {NavigationEnd, Router} from "@angular/router";
import {AuthAdminService} from "./services/auth-admin.service";

@Component({
  selector: 'app-admin-portal',
  templateUrl: './admin-portal.component.html',
  styleUrls: ['./admin-portal.component.css']
})
export class AdminPortalComponent implements OnInit {
  togglesStatus = true;
  marginLeft = '200px';
  roleShow: any;
  menuItems: any;
  isManager: any;
  toggleAdmin = true;

  constructor(
    private router: Router,
    private authAdminService: AuthAdminService,
  ) {
    this.router.events.subscribe(val => {
      if (val instanceof NavigationEnd) {
        const url = val.url;
        this.checkRole(url);
      }
    });
  }

  ngOnInit() {
    this.isManager = this.authAdminService.isManager();
    this.roleShow = this.isManager ? 'Quản lý' : 'Lễ tân';
    this.checkRole(this.router.url);
  }

  changeToggle() {
    this.togglesStatus = !this.togglesStatus;
    this.marginLeft = this.togglesStatus ? '200px' : '70px';
  }

  checkRole(currentUrl: any) {
    let isAdminRoute = this.isAdminRoute(currentUrl);
    let isReceptionistRoute = this.isReceptionistRoute(currentUrl);
    if (!isAdminRoute && !isReceptionistRoute) {
      if (!this.isManager) {
        this.menuItems = this.getMenuReceptionistItems();
      } else {
        this.menuItems = this.getMenuAdminItems();
      }
    }
    if (isAdminRoute) {
      this.setManagementRole();
    }
    if (isReceptionistRoute) {
      this.setReceptionRole();
    }
  }

  isAdminRoute(currentUrl: any) {
    return MENU_ADMIN_ITEM.some(item => `/admin/${item.path}`.includes(currentUrl));
  }

  isReceptionistRoute(currentUrl: any) {
    return MENU_RECEPTIONIST_ITEM.some(item => `/admin/${item.path}`.includes(currentUrl));
  }

  getMenuAdminItems(): any[] {
    let permissions = this.authAdminService.getPermissions();
    return MENU_ADMIN_ITEM.filter((item: any) => permissions?.includes(item.permission))
  }

  getMenuReceptionistItems(): any[] {
    let permissions = this.authAdminService.getPermissions();
    return MENU_RECEPTIONIST_ITEM.filter((item: any) => permissions?.includes(item.permission))
  }

  setReceptionRole() {
    this.menuItems = this.isManager ? MENU_RECEPTIONIST_ITEM : this.getMenuReceptionistItems();
  }

  setManagementRole() {
    this.menuItems = this.getMenuAdminItems();
  }

  changeRole() {
    if (this.isAdminRoute(this.router.url)) {
      this.router.navigate([RouterFEConstant.ADMIN_PORTAL.path, RouterFEConstant.ADMIN_PORTAL_ROOM_DIAGRAM.path]).then(() => {
      });
    } else {
      this.router.navigate([RouterFEConstant.ADMIN_PORTAL.path, RouterFEConstant.ADMIN_PORTAL_SALES_REPORT.path]).then(() => {
      });
    }
  }

  showRoleChange() {
    if (this.isAdminRoute(this.router.url)) {
      return 'Lễ tân'
    } else {
      return 'Quản lý'
    }
  }
}

import {Component, OnInit} from '@angular/core';
import {LoginAdminService} from "../admin-portal/services/login-admin.service";
import {AuthAdminService} from "../admin-portal/services/auth-admin.service";

@Component({
  selector: 'app-home-portal',
  templateUrl: './home-portal.component.html',
  styleUrls: ['./home-portal.component.css']
})
export class HomePortalComponent implements OnInit {

  constructor(private authAdminService:AuthAdminService) {
  }

  ngOnInit() {
    if (this.authAdminService.getLoginAdminInfor()) {
      this.authAdminService.logOut();
    }

  }

}

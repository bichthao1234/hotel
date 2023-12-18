import {Component, Input, OnInit} from '@angular/core';
import {EmployeeAdminService} from "../../../../modules/admin-portal/services/employee-admin.service";
import {AdminInforLogin} from "../../../../modules/admin-portal/models/AdminInforLogin";
import {AuthAdminService} from "../../../../modules/admin-portal/services/auth-admin.service";

@Component({
  selector: 'app-header-admin',
  templateUrl: './header-admin.component.html',
  styleUrls: ['./header-admin.component.css']
})
export class HeaderAdminComponent implements OnInit {
  @Input() public togglesStatus: boolean | undefined;
  username: any;
  token: any;
  employee: any;
  loginAdminInfor: AdminInforLogin | null | undefined;

  constructor(private employeeAdminService:EmployeeAdminService,
              private authAdminService:AuthAdminService) {
  }

  ngOnInit() {
    this.loginAdminInfor = this.authAdminService.getLoginAdminInfor();
    if (this.loginAdminInfor) {
      this.getEmployee(this.loginAdminInfor.username);
    }
  }


  private getEmployee(username: any) {
    this.employeeAdminService.getEmployee(username).subscribe((resp) => {
      if (resp) {
        this.employee = resp;
      }
    })
  }

  logout() {
    this.authAdminService.logOut();
    location.reload();
  }
}

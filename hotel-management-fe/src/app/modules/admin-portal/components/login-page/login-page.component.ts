import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {LoginAdminService} from "../../services/login-admin.service";
import {Router} from "@angular/router";
import {AdminInforLogin} from "../../models/AdminInforLogin";
import {AuthAdminService} from "../../services/auth-admin.service";
import {RouterFEConstant} from "../../../../constant/RouterFEConstant";
import {SweetAlertCustomizeService} from "../../../../services/sweet-alert-customize.service";

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.css']
})
export class LoginPageComponent implements OnInit {
  loginRequest: any;
  loginAdminResp: AdminInforLogin | undefined;
  isSubmit = false;
  public loginForm: FormGroup = new FormGroup({});

  constructor(private loginAdminService: LoginAdminService,
              private router: Router,
              private authAdminService: AuthAdminService,
              private sweetAlertCustomizeService: SweetAlertCustomizeService) {
  }

  ngOnInit() {
    this.loginForm = new FormGroup({
      username: new FormControl('', [Validators.required]),
      password: new FormControl('', [Validators.required]),
    });
  }

  checkLogin() {
    this.isSubmit = true;
    if (this.loginForm.valid) {
      this.loginRequest = {
        username: this.loginForm.getRawValue().username,
        password: this.loginForm.getRawValue().password
      }
      this.loginAdminService.loginAdmin(this.loginRequest).subscribe((resp) => {
          if (resp && resp.username) {
            this.authAdminService.setLoginAdminInfor(resp);
            if (!resp.isManager) {
              this.router.navigate([RouterFEConstant.ADMIN_PORTAL.path, RouterFEConstant.ADMIN_PORTAL_ROOM_DIAGRAM.path]).then(r => {
                  location.reload();
                },
                error => {
                  this.authAdminService.logOut();
                  location.reload();
                });
            } else {
              this.router.navigate([RouterFEConstant.ADMIN_PORTAL.path, RouterFEConstant.ADMIN_PORTAL_SALES_REPORT.path]).then(r => {
                  location.reload();
                },
                error => {
                  this.authAdminService.logOut();
                  location.reload();
                });
            }
          }
        },
        error => {
          this.sweetAlertCustomizeService.showErrorAlertRightPopup(error?.error);
        })
    }
  }
}

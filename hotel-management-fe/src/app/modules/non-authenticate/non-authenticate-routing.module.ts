import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {NonAuthenticateComponent} from "./non-authenticate.component";
import {LoginPageComponent} from "../admin-portal/components/login-page/login-page.component";
import {RouterFEConstant} from "../../constant/RouterFEConstant";

const routes: Routes = [
  {
    path: RouterFEConstant.ADMIN_PORTAL.path,
    component:NonAuthenticateComponent,
    children: [
      {
        path: RouterFEConstant.LOGIN.path,
        component: LoginPageComponent
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class NonAuthenticateRoutingModule { }

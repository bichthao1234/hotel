import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { NonAuthenticateRoutingModule } from './non-authenticate-routing.module';
import { NonAuthenticateComponent } from './non-authenticate.component';
import {AdminPortalModule} from "../admin-portal/admin-portal.module";


@NgModule({
  declarations: [
    NonAuthenticateComponent
  ],
  imports: [
    CommonModule,
    NonAuthenticateRoutingModule,
    AdminPortalModule
  ]
})
export class NonAuthenticateModule { }

import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {ErrorPageComponent} from './components/error-page/error-page.component';
import {AdminPortalComponent} from './modules/admin-portal/admin-portal.component';
import {AdminPortalModule} from "./modules/admin-portal/admin-portal.module";
import {SharedModule} from "./shared/shared.module";
import {MatSidenavModule} from "@angular/material/sidenav";
import {MatIconModule} from "@angular/material/icon";
import {NonAuthenticateModule} from "./modules/non-authenticate/non-authenticate.module";
import {ReactiveFormsModule} from "@angular/forms";
import {ngxLoadingAnimationTypes, NgxLoadingModule} from "ngx-loading";
import {HttpInterceptorProviders} from "./interceptors/HttpInterceptorSupport";
import {SweetAlert2Module} from "@sweetalert2/ngx-sweetalert2";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {HomePortalModule} from "./modules/home-portal/home-portal.module";
import {DateAdapter, MAT_DATE_FORMATS, MatNativeDateModule} from "@angular/material/core";
import {CUSTOM_DATE_FORMATS, CustomDateAdapter} from "./shared/services/CustomDateAdapter";
import {CurrencyFormatterDirective} from "./directives/CurrencyFormatterDirective";
import {MatTooltipModule} from "@angular/material/tooltip";

@NgModule({
  declarations: [
    AppComponent,
    ErrorPageComponent,
    AdminPortalComponent,
    CurrencyFormatterDirective
  ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        AdminPortalModule,
        HomePortalModule,
        SharedModule,
        MatSidenavModule,
        MatIconModule,
        NonAuthenticateModule,
        ReactiveFormsModule,
        NgxLoadingModule.forRoot({animationType: ngxLoadingAnimationTypes.circleSwish}),
        SweetAlert2Module.forRoot(),
        BrowserAnimationsModule,
        MatNativeDateModule,
        MatTooltipModule
    ],
  providers: [
    HttpInterceptorProviders,
    {provide: DateAdapter, useClass: CustomDateAdapter},
    {provide: MAT_DATE_FORMATS, useValue: CUSTOM_DATE_FORMATS}
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}

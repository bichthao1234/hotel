import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {HeaderAdminComponent} from './components/admin/header-admin/header-admin.component';
import {FooterAdminComponent} from './components/admin/footer-admin/footer-admin.component';
import {SideBarAdminComponent} from './components/admin/side-bar-admin/side-bar-admin.component';
import {MatSidenavModule} from "@angular/material/sidenav";
import {RouterModule} from "@angular/router";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {MatMenuModule} from "@angular/material/menu";
import {MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon";
import {MatListModule} from "@angular/material/list";
import {SideBarCollapseComponent} from './components/admin/side-bar-collapse/side-bar-collapse.component';
import {MatTooltipModule} from "@angular/material/tooltip";
import {SearchComponent} from "../components/search/search.component";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HeaderUserComponent} from "./components/user/header-user/header-user.component";
import {RangeDatePickerComponent} from "./components/range-date-picker/range-date-picker.component";
import {MatInputModule} from "@angular/material/input";
import {MatDatepickerModule} from "@angular/material/datepicker";
import { FooterUserComponent } from './components/user/footer-user/footer-user.component';
import {NullHyphensPipe} from "../pipe/null-hyphens.pipe";

@NgModule({
  declarations: [
    HeaderAdminComponent,
    FooterAdminComponent,
    SideBarAdminComponent,
    SideBarCollapseComponent,
    SearchComponent,
    HeaderUserComponent,
    RangeDatePickerComponent,
    FooterUserComponent,
    NullHyphensPipe,
  ],
  imports: [
    CommonModule,
    MatSidenavModule,
    RouterModule,
    BrowserAnimationsModule,
    MatMenuModule,
    MatButtonModule,
    MatIconModule,
    MatListModule,
    MatTooltipModule,
    ReactiveFormsModule,
    FormsModule,
    MatInputModule,
    MatDatepickerModule,

  ],
  exports: [
    HeaderAdminComponent,
    FooterAdminComponent,
    SideBarAdminComponent,
    SideBarCollapseComponent,
    SearchComponent,
    HeaderUserComponent,
    RangeDatePickerComponent,
    FooterUserComponent,
    NullHyphensPipe,
  ]
})
export class SharedModule {
}

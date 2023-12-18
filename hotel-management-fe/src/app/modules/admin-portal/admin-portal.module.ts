import {NgModule} from '@angular/core';
import {CommonModule, DatePipe} from '@angular/common';
import {AdminPortalRoutingModule} from './admin-portal-routing.module';
import {LoginPageComponent} from './components/login-page/login-page.component';
import {SharedModule} from "../../shared/shared.module";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {MatSidenavModule} from "@angular/material/sidenav";
import {RouterModule} from "@angular/router";
import {MatMenuModule} from "@angular/material/menu";
import {MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import {ImportCustomerComponent} from "./components/import-customer/import-customer.component";
import {DashboardComponent} from './components/dashboard/dashboard.component';
import {CustomerListComponent} from './components/customer-list/customer-list.component';
import {MatTableModule} from "@angular/material/table";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {RoomTabComponent} from './components/room-tab/room-tab.component';
import {MatTabsModule} from "@angular/material/tabs";
import {RoomTypeComponent} from './components/room-type/room-type.component';
import {RoomKindComponent} from './components/room-kind/room-kind.component';
import {RoomClassificationComponent} from './components/room-classification/room-classification.component';
import {RoomListComponent} from './components/room-list/room-list.component';
import {AddRoomTypeComponent} from './components/add-room-type/add-room-type.component';
import {MatDialogModule} from "@angular/material/dialog";
import {MatTooltipModule} from "@angular/material/tooltip";
import {MatDividerModule} from "@angular/material/divider";
import {EditRoomTypeComponent} from './components/edit-room-type/edit-room-type.component';
import {RoomDiagramComponent} from './components/room-diagram/room-diagram.component';
import {MatSliderModule} from "@angular/material/slider";
import {SliderModule} from "primeng/slider";
import {MatSelectModule} from "@angular/material/select";
import {DetailRoomComponent} from './components/detail-room/detail-room.component';
import {PanelModule} from "primeng/panel";
import {RentalSlipDetailComponent} from './components/rental-slip-detail/rental-slip-detail.component';
import {
  CustomerRetalSlipDetailComponent
} from './components/customer-retal-slip-detail/customer-retal-slip-detail.component';
import {ReservationComponent} from './components/reservation/reservation.component';
import {MultiSelectModule} from "primeng/multiselect";
import {TableModule} from "primeng/table";
import {PaginatorModule} from "primeng/paginator";
import {DetailReservationsComponent} from './components/detail-reservations/detail-reservations.component';
import {
  DetailManifestConvertReservationsComponent
} from './components/detail-manifest-conver-reservations/detail-manifest-convert-reservations.component';
import {MatChipsModule} from "@angular/material/chips";
import {ChipModule} from "primeng/chip";
import {CustomerAddComponent} from './components/customer-add/customer-add.component';
import {ServiceRetalSlipDetail} from "./components/service-retal-slip-detail/service-retal-slip-detail";
import { RentalSlipComponent } from './components/rental-slip/rental-slip.component';
import { CommonRentalSlipDetailComponent } from './components/common-rental-slip-detail/common-rental-slip-detail.component';
import {MenuModule} from "primeng/menu";
import {SurchargeRetalSlipDetail} from "./components/surcharge-retal-slip-detail/surcharge-retal-slip-detail";
import { QuickCheckInComponent } from './components/quick-check-in/quick-check-in.component';
import { InvoiceShowPageComponent } from './components/invoice-show-page/invoice-show-page.component';
import {PaymentDialogComponent} from "./components/paymen-dialog/payment-dialog.component";
import { InvoiceManagementComponent } from './components/invoice-management/invoice-management.component';
import { AddRoomKindComponent } from './components/add-room-kind/add-room-kind.component';
import { EditRoomKindComponent } from './components/edit-room-kind/edit-room-kind.component';
import { AddRoomClassificationComponent } from './components/add-room-classification/add-room-classification.component';
import {CalendarModule} from "primeng/calendar";
import {
  ReservationEditRangeDateComponent
} from "./components/reservation-edit-range-date/reservation-edit-range-date.component";
import {ExportInvoiceComponent} from "./components/export-invoice/export-invoice.component";
import {ExportInvoiceDialogComponent} from "./components/export-invoice-dialog/export-invoice-dialog.component";
import {PickListModule} from "primeng/picklist";
import { RentalSlipDetailInExportInvoiceComponent } from './components/rental-slip-detail-in-export-invoice/rental-slip-detail-in-export-invoice.component';
import {AutoCompleteModule} from "primeng/autocomplete";
import {NgChartsModule} from "ng2-charts";
import { SalesReportComponent } from './components/sales-report/sales-report.component';
import { RoomFrequencyReportComponent } from './components/room-frequency-report/room-frequency-report.component';
import {
  EditRoomClassificationComponent
} from "./components/edit-room-classification/edit-room-classification.component";
import {TimelineModule} from "primeng/timeline";
import {CardModule} from "primeng/card";
import { PriceRoomClassDialogComponent } from './components/price-room-class-dialog/price-room-class-dialog.component';
import {AddPriceRoomClassComponent} from "./components/add-price-room-class/add-price-room-class.component";
import {ServiceManagementComponent} from "./components/service-management/service-management.component";
import {PriceServiceDialogComponent} from "./components/price-service-dialog/price-service-dialog.component";
import {AddPriceServiceComponent} from "./components/add-price-service/add-price-service.component";
import {AddServiceComponent} from "./components/add-service/add-service.component";
import {EditServiceComponent} from "./components/edit-service/edit-service.component";
import {PromotionManagementComponent} from "./components/promotion-management/promotion-management.component";
import {AddPromotionComponent} from "./components/add-promotion/add-promotion.component";
import {EditPromotionComponent} from "./components/edit-promotion/edit-promotion.component";
import {DetailPromotionDialogComponent} from "./components/detail-promotion-dialog/detail-promotion-dialog.component";
import {
  AddPromotionDetailDialogComponent
} from "./components/add-promotion-detail-dialog/add-promotion-detail-dialog.component";
import { AddRoomComponent } from './components/add-room/add-room.component';
import {EditRoomComponent} from "./components/edit-room/edit-room.component";
import {SurchargeManagementComponent} from "./components/surcharge-management/surcharge-management.component";
import {AddSurchargeComponent} from "./components/add-surcharge/add-surcharge.component";
import {EditSurchargeComponent} from "./components/edit-surcharge/edit-surcharge.component";
import {PriceSurchargeDialogComponent} from "./components/price-surcharge-dialog/price-surcharge-dialog.component";
import {AddPriceSurchargeComponent} from "./components/add-price-surcharge/add-price-surcharge.component";


@NgModule({
  declarations: [
    LoginPageComponent,
    ImportCustomerComponent,
    DashboardComponent,
    CustomerListComponent,
    RoomTabComponent,
    RoomTypeComponent,
    RoomKindComponent,
    RoomClassificationComponent,
    RoomListComponent,
    AddRoomTypeComponent,
    EditRoomTypeComponent,
    RoomDiagramComponent,
    DetailRoomComponent,
    RentalSlipDetailComponent,
    CustomerRetalSlipDetailComponent,
    ReservationComponent,
    DetailReservationsComponent,
    ReservationEditRangeDateComponent,
    DetailManifestConvertReservationsComponent,
    CustomerAddComponent,
    ServiceRetalSlipDetail,
    SurchargeRetalSlipDetail,
    RentalSlipComponent,
    CommonRentalSlipDetailComponent,
    QuickCheckInComponent,
    InvoiceShowPageComponent,
    PaymentDialogComponent,
    InvoiceManagementComponent,
    AddRoomKindComponent,
    EditRoomKindComponent,
    AddRoomClassificationComponent,
    ExportInvoiceComponent,
    ExportInvoiceDialogComponent,
    RentalSlipDetailInExportInvoiceComponent,
    SalesReportComponent,
    RoomFrequencyReportComponent,
    EditRoomClassificationComponent,
    AddRoomComponent,
    EditRoomComponent,
    PriceRoomClassDialogComponent,
    AddPriceRoomClassComponent,
    ServiceManagementComponent,
    PriceServiceDialogComponent,
    AddPriceServiceComponent,
    AddServiceComponent,
    EditServiceComponent,
    PromotionManagementComponent,
    AddPromotionComponent,
    EditPromotionComponent,
    DetailPromotionDialogComponent,
    AddPromotionDetailDialogComponent,
    SurchargeManagementComponent,
    PriceSurchargeDialogComponent,
    AddPriceSurchargeComponent,
    AddSurchargeComponent,
    EditSurchargeComponent,
  ],
  imports: [
    CommonModule,
    AdminPortalRoutingModule,
    MatSidenavModule,
    RouterModule,
    MatMenuModule,
    MatButtonModule,
    MatIconModule,
    ReactiveFormsModule,
    HttpClientModule,
    FormsModule,
    MatTableModule,
    MatFormFieldModule,
    MatInputModule,
    MatTabsModule,
    BrowserAnimationsModule,
    MatDialogModule,
    MatTooltipModule,
    MatDividerModule,
    SharedModule,
    MatSliderModule,
    SliderModule,
    MatSelectModule,
    PanelModule,
    MultiSelectModule,
    TableModule,
    PaginatorModule,
    MatChipsModule,
    ChipModule,
    MenuModule,
    CalendarModule,
    PickListModule,
    AutoCompleteModule,
    NgChartsModule,
    TimelineModule,
    CardModule,
  ],
  exports: [
    LoginPageComponent
  ],
  providers: [
    DatePipe
  ]
})
export class AdminPortalModule {
}

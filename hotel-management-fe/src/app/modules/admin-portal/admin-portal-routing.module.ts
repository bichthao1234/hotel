import {NgModule} from '@angular/core';
import {RouterModule, Routes} from "@angular/router";
import {AdminPortalComponent} from "./admin-portal.component";
import {ImportCustomerComponent} from "./components/import-customer/import-customer.component";
import {DashboardComponent} from "./components/dashboard/dashboard.component";
import {CustomerListComponent} from "./components/customer-list/customer-list.component";
import {AuthGuard} from "../../shared/guards/auth-guard.service";
import {RouterFEConstant} from "../../constant/RouterFEConstant";
import {RoomTabComponent} from "./components/room-tab/room-tab.component";
import {RoomDiagramComponent} from "./components/room-diagram/room-diagram.component";
import {ReservationComponent} from "./components/reservation/reservation.component";
import {RentalSlipComponent} from "./components/rental-slip/rental-slip.component";
import {
  CommonRentalSlipDetailComponent
} from "./components/common-rental-slip-detail/common-rental-slip-detail.component";
import {InvoiceShowPageComponent} from "./components/invoice-show-page/invoice-show-page.component";
import {InvoiceManagementComponent} from "./components/invoice-management/invoice-management.component";
import {ExportInvoiceComponent} from "./components/export-invoice/export-invoice.component";
import {SalesReportComponent} from "./components/sales-report/sales-report.component";
import {RoomFrequencyReportComponent} from "./components/room-frequency-report/room-frequency-report.component";
import {ServiceManagementComponent} from "./components/service-management/service-management.component";
import {PromotionManagementComponent} from "./components/promotion-management/promotion-management.component";
import {SurchargeManagementComponent} from "./components/surcharge-management/surcharge-management.component";

const routes: Routes = [
  {
    path: RouterFEConstant.ADMIN_PORTAL.path,
    component: AdminPortalComponent,
    children: [
      {
        path: RouterFEConstant.ADMIN_PORTAL_DASHBOARD.path,
        component: DashboardComponent
      },
      {
        path: RouterFEConstant.ADMIN_PORTAL_IMPORT_CUSTOMER.path,
        component: ImportCustomerComponent
      },
      {
        path: RouterFEConstant.ADMIN_PORTAL_CUSTOMER.path,
        component: CustomerListComponent
      },
      {
        path: RouterFEConstant.ADMIN_PORTAL_ROOM_MANAGEMENT.path,
        component: RoomTabComponent
      },
      {
        path: RouterFEConstant.ADMIN_PORTAL_ROOM_DIAGRAM.path,
        component: RoomDiagramComponent
      },
      {
        path: RouterFEConstant.ADMIN_PORTAL_RESERVATION.path,
        component: ReservationComponent
      },
      {
        path: RouterFEConstant.ADMIN_PORTAL_RENTAL_SLIP.path,
        component: RentalSlipComponent
      },
      {
        path: RouterFEConstant.ADMIN_PORTAL_COMMON_RENTAL_SLIP.path + '/:rentalSlipId',
        component: CommonRentalSlipDetailComponent,
      },
      {
        path: RouterFEConstant.ADMIN_PORTAL_INVOICE_MANAGEMENT.path,
        component: InvoiceManagementComponent,
      },
      {
        path: RouterFEConstant.ADMIN_PORTAL_EXPORT_INVOICE.path,
        component: ExportInvoiceComponent,
      },
      {
        path: RouterFEConstant.ADMIN_PORTAL_SALES_REPORT.path,
        component: SalesReportComponent,
      },
      {
        path: RouterFEConstant.ADMIN_PORTAL_ROOM_FREQUENCY_REPORT.path,
        component: RoomFrequencyReportComponent,
      },
      {
        path: RouterFEConstant.ADMIN_PORTAL_SERVICE_MANAGEMENT.path,
        component: ServiceManagementComponent,
      },
      {
        path: RouterFEConstant.ADMIN_PORTAL_SURCHARGE_MANAGEMENT.path,
        component: SurchargeManagementComponent,
      },
      {
        path: RouterFEConstant.ADMIN_PORTAL_PROMOTION_MANAGEMENT.path,
        component: PromotionManagementComponent,
      },
      {
        path: 'test',
        component: InvoiceShowPageComponent
      }
    ],
    canActivate: [AuthGuard]
  }
]

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminPortalRoutingModule {
}

import {IRoute} from "../models/IRoute";

export class RouterFEConstant {
  //Admin
  public static ADMIN_PORTAL: IRoute = {
    path: 'admin',
  }
  public static ADMIN_PORTAL_DASHBOARD: IRoute = {
    path: 'dashboard',
  }
  public static ADMIN_PORTAL_SALES_REPORT: IRoute = {
    path: 'sales-report',
  }
  public static ADMIN_PORTAL_ROOM_FREQUENCY_REPORT: IRoute = {
    path: 'room-frequency-report',
  }
  public static ADMIN_PORTAL_BOOKING: IRoute = {
    path: 'booking',
  }
  public static ADMIN_PORTAL_IMPORT_CUSTOMER: IRoute = {
    path: 'import-customer',
  }
  public static ADMIN_PORTAL_CUSTOMER: IRoute = {
    path: 'customer',
  }
  public static ADMIN_PORTAL_ROOM_MANAGEMENT: IRoute = {
    path: 'room-management',
  }
  public static ADMIN_PORTAL_ROOM_DIAGRAM: IRoute = {
    path: 'room-diagram',
  }

  //Customer
  //non-authenticate
  public static LOGIN: IRoute = {
    path: 'login'
  }
  static ADMIN_PORTAL_RESERVATION:IRoute = {
    path: 'reservation',
  }
  static ADMIN_PORTAL_RENTAL_SLIP:IRoute = {
    path: 'rental-slip',
  }
  static ADMIN_PORTAL_COMMON_RENTAL_SLIP:IRoute = {
    path: 'common-rental-slip',
  }
  static ADMIN_PORTAL_INVOICE_MANAGEMENT:IRoute = {
    path: 'invoice-management',
  }
  static ADMIN_PORTAL_EXPORT_INVOICE:IRoute = {
    path: 'export-invoice',
  }
  public static ADMIN_PORTAL_SERVICE_MANAGEMENT: IRoute = {
    path: 'service-management',
  }
  public static ADMIN_PORTAL_SURCHARGE_MANAGEMENT: IRoute = {
    path: 'surcharge-management',
  }
  public static ADMIN_PORTAL_PROMOTION_MANAGEMENT: IRoute = {
    path: 'promo-management',
  }
}

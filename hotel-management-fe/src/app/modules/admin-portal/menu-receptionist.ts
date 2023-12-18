import {RouterFEConstant} from "../../constant/RouterFEConstant";

export let MENU_RECEPTIONIST_ITEM = [
  {
    path: RouterFEConstant.ADMIN_PORTAL_ROOM_DIAGRAM.path,
    title: 'Sơ đồ phòng',
    icon: 'grid_view',
    permission: 'VIEW_AVAILABILITY'
  },
  {
    path: RouterFEConstant.ADMIN_PORTAL_RESERVATION.path,
    title: 'Phiếu đặt',
    icon: 'event_available',
    permission: 'RESERVATION_MANAGE'
  },
  {
    path: RouterFEConstant.ADMIN_PORTAL_RENTAL_SLIP.path,
    title: 'Phiếu thuê',
    icon: 'receipt_long',
    permission: 'RENTAL_MANAGEMENT'
  },
  {
    path: RouterFEConstant.ADMIN_PORTAL_IMPORT_CUSTOMER.path,
    title: 'Nhập khách',
    icon: 'add_box',
    permission: 'CUSTOMER_IMPORT'
  },
  {
    path: RouterFEConstant.ADMIN_PORTAL_INVOICE_MANAGEMENT.path,
    title: 'Quản lý hoá đơn',
    icon: 'description',
    permission: 'INVOICE_MANAGEMENT'
  },
  {
    path: RouterFEConstant.ADMIN_PORTAL_EXPORT_INVOICE.path,
    title: 'Xuất hoá đơn',
    icon: 'export_notes',
    permission: 'INVOICE_MANAGEMENT'
  },
];

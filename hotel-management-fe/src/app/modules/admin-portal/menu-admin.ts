import {RouterFEConstant} from "../../constant/RouterFEConstant";

export let MENU_ADMIN_ITEM = [
  // {
  //   path: RouterFEConstant.ADMIN_PORTAL_DASHBOARD.path,
  //   title: 'Trang chủ',
  //   icon: 'dashboard'
  // },
  {
    path: RouterFEConstant.ADMIN_PORTAL_SALES_REPORT.path,
    title: 'Báo cáo doanh thu',
    icon: 'bar_chart',
    permission: 'VIEW_REPORT'
  },
  {
    path: RouterFEConstant.ADMIN_PORTAL_ROOM_FREQUENCY_REPORT.path,
    title: 'Báo cáo tần suất phòng',
    icon: 'pie_chart',
    permission: 'VIEW_REPORT'
  },
  {
    path: RouterFEConstant.ADMIN_PORTAL_ROOM_MANAGEMENT.path,
    title: 'Quản lý phòng',
    icon: 'hotel',
    permission: 'SYSTEM_MANAGEMENT'
  },
  {
    path: RouterFEConstant.ADMIN_PORTAL_SERVICE_MANAGEMENT.path,
    title: 'Dịch vụ',
    icon: 'currency_exchange',
    permission: 'SERVICE_MANAGEMENT'
  },
  {
    path: RouterFEConstant.ADMIN_PORTAL_SURCHARGE_MANAGEMENT.path,
    title: 'Phụ thu',
    icon: 'payments',
    permission: 'SURCHARGE_MANAGEMENT'
  },
  {
    path: RouterFEConstant.ADMIN_PORTAL_PROMOTION_MANAGEMENT.path,
    title: 'Khuyến mãi',
    icon: 'app_promo',
    permission: 'PROMOTION_MANAGEMENT'
  },
];

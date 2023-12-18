import {API} from "../models/API";
import {ROUTES} from "./ROUTES";

export class APIConstant {

  //Login

  public static ADMIN_LOGIN: API = {
    module: ROUTES.URI_REST_AUTH,
    uri: '/login'
  };
  //Admin
  public static GET_EMPLOYEE: API = {
    module: ROUTES.URI_REST_EMPLOYEE,
    uri: '/get-employee/{username}'
  };
  public static IMPORT_CUSTOMER_BY_FILE: API = {
    module: ROUTES.URI_REST_CUSTOMER,
    uri: '/import-customer/save'
  };
  public static PREVIEW_FILE_CUSTOMER: API = {
    module: ROUTES.URI_REST_CUSTOMER,
    uri: '/import-customer/preview'
  };
  static GET_ALL_ROOM_TYPE: API = {
    module: ROUTES.URI_REST_ROOM_TYPE,
    uri: '/getAll'
  };
  static ADD_NEW_ROOM_TYPE: API = {
    module: ROUTES.URI_REST_ROOM_TYPE,
    uri: '/create'
  };
  static EDIT_ROOM_TYPE: API = {
    module: ROUTES.URI_REST_ROOM_TYPE,
    uri: '/edit'
  };
  static GET_ROOM_TYPE_BY_NAME: API = {
    module: ROUTES.URI_REST_ROOM_TYPE,
    uri: '/getByName'
  };
  static GET_ALL_ROOM_KIND: API = {
    module: ROUTES.URI_REST_ROOM_KIND,
    uri: '/getAll'
  };
  static ADD_NEW_ROOM_KIND: API = {
    module: ROUTES.URI_REST_ROOM_KIND,
    uri: '/create'
  };
  static EDIT_ROOM_KIND: API = {
    module: ROUTES.URI_REST_ROOM_KIND,
    uri: '/edit'
  };
  static GET_ROOM_KIND_BY_NAME: API = {
    module: ROUTES.URI_REST_ROOM_KIND,
    uri: '/getByName'
  };
  static GET_ALL_ROOM_STATUS: API = {
    module: ROUTES.URI_REST_ROOM_STATUS,
    uri: '/getAll'
  };
  static GET_ROOM_CLASSIFICATION_LIST: API = {
    module: ROUTES.URI_REST_RESERVATION,
    uri: '/check'
  };
  static GET_RESERVATION_LIST: API = {
    module: ROUTES.URI_REST_RESERVATION,
    uri: '/list'
  };
  static CONVERT_FROM_RESERVATION: API = {
    module: ROUTES.URI_REST_RENTAL_SLIP,
    uri: '/convert'
  };
  static GET_DETAIL_RESERVATION_LIST: API = {
    module: ROUTES.URI_REST_RESERVATION,
    uri: '/list/detail/{reservationId}'
  };
  static GET_DETAIL_RENTAL_BY_RESERVATION: API = {
    module: ROUTES.URI_REST_RESERVATION,
    uri: '/list/rental/{reservationId}'
  };
  static CANCEL_RESERVATION: API = {
    module: ROUTES.URI_REST_RESERVATION,
    uri: '/list/cancel?reservationId={reservationId}&employeeId={employeeId}'
  };

  static CHANGE_DATE_RANGE_RESERVATION: API = {
    module: ROUTES.URI_REST_RESERVATION,
    uri: '/list/update-date'
  };

  static MAKE_RESERVATION: API = {
    module: ROUTES.URI_REST_RESERVATION,
    uri: '/book'
  };
  static GET_ROOM_CLASSIFICATION_WITH_ID: API = {
    module: ROUTES.URI_REST_ROOM_CLASSIFICATION,
    uri: '/getById/{id}'
  };
  static GET_PRICE_ROOM_CLASSIFICATION_WITH_ID: API = {
    module: ROUTES.URI_REST_RESERVATION,
    uri: '/price'
  };
  static GET_IMAGE_ROOM_CLASSIFICATION_WITH_ID: API = {
    module: ROUTES.URI_REST_RESERVATION,
    uri: '/image/{roomClassId}'
  };
  static CHECK_CUSTOMER_WITH_ID: API = {
    module: ROUTES.URI_REST_CUSTOMER,
    uri: '/{id}'
  };
  static GET_ALL_ROOM_CLASSIFICATION: API = {
    module: ROUTES.URI_REST_ROOM_CLASSIFICATION,
    uri: '/getAll'
  };
  static CREATE_NEW_ROOM_CLASSIFICATION: API = {
    module: ROUTES.URI_REST_ROOM_CLASSIFICATION,
    uri: '/create-new'
  };
  static EDIT_ROOM_CLASSIFICATION: API = {
    module: ROUTES.URI_REST_ROOM_CLASSIFICATION,
    uri: '/edit-room-classification'
  };
  static IMAGE_ROOM_CLASSIFICATION: API = {
    module: ROUTES.URI_REST_ROOM_CLASSIFICATION,
    uri: '/add-image'
  };
  static GET_ALL_ROOM_CLASSIFICATION_PRICE: API = {
    module: ROUTES.URI_REST_ROOM_CLASSIFICATION,
    uri: '/getAllPrice'
  };
  static ADD_NEW_ROOM_CLASSIFICATION_PRICE: API = {
    module: ROUTES.URI_REST_ROOM_CLASSIFICATION,
    uri: '/add-new-price'
  };
  static GET_EXCHANGE_RATE: API = {
    module: ROUTES.URI_REST_PAYMENT,
    uri: '/get-exchange-rate'
  };
  static GET_ROOM_LIST: API = {
    module: ROUTES.URI_REST_ROOM,
    uri: '/admin/getAllRooms'
  };
  static GET_ROOM_RENTAL_DETAIL: API = {
    module: ROUTES.URI_REST_ROOM,
    uri: '/admin/rental/detail?roomId={id}'
  };
  static GET_ROOM_RENTAL_GUEST: API = {
    module: ROUTES.URI_REST_ROOM,
    uri: '/admin/rental/guest?roomId={id}'
  };
  static GET_ROOM_RENTAL_SERVICE: API = {
    module: ROUTES.URI_REST_ROOM,
    uri: '/admin/rental/service?roomId={id}'
  };
  static GET_ROOM_RENTAL_SURCHARGE: API = {
    module: ROUTES.URI_REST_ROOM,
    uri: '/admin/rental/surcharge?roomId={id}'
  };
  static GET_ROOM_AVAILABLE_LIST: API = {
    module: ROUTES.URI_REST_ROOM,
    uri: '/admin/available/{roomClassId}'
  };
  static GET_CUSTOMER_LIST: API = {
    module: ROUTES.URI_REST_CUSTOMER,
    uri: '/all'
  };
  static CREATE_CUSTOMER: API = {
    module: ROUTES.URI_REST_CUSTOMER,
    uri: '/create'
  };
  static GET_RENTAL_SLIP_DETAIL_WITH_RESERVATION: API = {
    module: ROUTES.URI_REST_RENTAL_SLIP,
    uri: '/getRentalSlipDetailsWithReservation/{reservationId}'
  };
  static GET_ALL_RENTAL_SLIP: API = {
    module: ROUTES.URI_REST_RENTAL_SLIP,
    uri: '/admin/all'
  };

  static GET_ALL_RENTAL_SLIP_FOR_EXPORT_INVOICE: API = {
    module: ROUTES.URI_REST_RENTAL_SLIP,
    uri: '/admin/invoice?customerId={customerId}'
  };
  static GET_RENTAL_SLIP_DETAIL_WITH_RENTAL_SLIP_ID: API = {
    module: ROUTES.URI_REST_RENTAL_SLIP,
    uri: '/admin/detail-list/{rentalSlipId}'
  };
  static GET_RENTAL_SLIP_DETAIL_WITH_RENTAL_SLIP_DETAIL_ID: API = {
    module: ROUTES.URI_REST_RENTAL_SLIP,
    uri: '/admin/detail'
  };
  static GET_ALL_SERVICES: API = {
    module: ROUTES.URI_REST_SERVICE,
    uri: '/get-all'
  };
  static GET_SERVICE_WITH_ID: API = {
    module: ROUTES.URI_REST_SERVICE,
    uri: '/getById/{id}'
  };

  static ADD_NEW_SERVICE_PRICE: API = {
    module: ROUTES.URI_REST_SERVICE,
    uri: '/add-new-price'
  };

  static CREATE_NEW_SERVICE: API = {
    module: ROUTES.URI_REST_SERVICE,
    uri: '/create-new'
  };

  static EDIT_NEW_SERVICE: API = {
    module: ROUTES.URI_REST_SERVICE,
    uri: '/edit'
  };

  static DELETE_NEW_SERVICE: API = {
    module: ROUTES.URI_REST_SERVICE,
    uri: '/delete/{id}'
  };

  static PAY_FOR_SERVICE: API = {
    module: ROUTES.URI_REST_SERVICE,
    uri: '/admin/pay'
  };
  static GET_ALL_SURCHARGE: API = {
    module: ROUTES.URI_REST_SURCHARGE,
    uri: '/admin/get-all'
  };
  static GET_SURCHARGE_WITH_ID: API = {
    module: ROUTES.URI_REST_SURCHARGE,
    uri: '/getById/{id}'
  };

  static ADD_NEW_SURCHARGE_PRICE: API = {
    module: ROUTES.URI_REST_SURCHARGE,
    uri: '/add-new-price'
  };

  static CREATE_NEW_SURCHARGE: API = {
    module: ROUTES.URI_REST_SURCHARGE,
    uri: '/create-new'
  };

  static EDIT_NEW_SURCHARGE: API = {
    module: ROUTES.URI_REST_SURCHARGE,
    uri: '/edit'
  };

  static DELETE_NEW_SURCHARGE: API = {
    module: ROUTES.URI_REST_SURCHARGE,
    uri: '/delete/{id}'
  };
  static QUICK_CHECK_IN: API = {
    module: ROUTES.URI_REST_RENTAL_SLIP,
    uri: '/check-in/quick'
  };
  static SAVE_SERVICE_DETAIL_WITH_RENTAL_SLIP_DETAIL: API = {
    module: ROUTES.URI_REST_SERVICE,
    uri: '/admin/save-service-detail-with-rental-slip-detail'
  };
  static SAVE_SURCHARGE_DETAIL_WITH_RENTAL_SLIP_DETAIL: API = {
    module: ROUTES.URI_REST_SURCHARGE,
    uri: '/admin/save-surcharge-detail-with-rental-slip-detail'
  };
  static SAVE_RENTAL_SLIP_DETAIL: API = {
    module: ROUTES.URI_REST_RENTAL_SLIP,
    uri: '/save-rental-slip-detail'
  };
  static CHECK_OUT_RENTAL_SLIP: API = {
    module: ROUTES.URI_REST_RENTAL_SLIP,
    uri: '/checkout'
  };

  static GET_INVOICE_BY_ID: API = {
    module: ROUTES.URI_REST_INVOICE,
    uri: '/export/{invoiceId}'
  };
  static GET_ALL_INVOICE: API = {
    module: ROUTES.URI_REST_INVOICE,
    uri: '/get-all'
  };
  static CREATE_INVOICE: API = {
    module: ROUTES.URI_REST_INVOICE,
    uri: '/export/create'
  };
  static CHANGE_ROOM_STATUS: API = {
    module: ROUTES.URI_REST_ROOM,
    uri: '/change-room-status'
  };
  static CREATE_NEW_ROOM: API = {
    module: ROUTES.URI_REST_ROOM,
    uri: '/create-new-room'
  };
  static MODIFY_ROOM: API = {
    module: ROUTES.URI_REST_ROOM,
    uri: '/modify-room'
  };
  static GET_REPORT: API = {
    module: ROUTES.URI_REST_RENTAL_SLIP,
    uri: '/report'
  };

  static GET_ALL_CONVENIENCE: API = {
    module: ROUTES.URI_REST_CONVENIENCE,
    uri: '/getAll'
  };
  static ADD_NEW_CONVENIENCE: API = {
    module: ROUTES.URI_REST_CONVENIENCE,
    uri: '/create'
  };
  static EDIT_CONVENIENCE: API = {
    module: ROUTES.URI_REST_CONVENIENCE,
    uri: '/edit'
  };
  static GET_CONVENIENCE_BY_NAME: API = {
    module: ROUTES.URI_REST_CONVENIENCE,
    uri: '/getByName'
  };

  static GET_ALL_PROMOTIONS: API = {
    module: ROUTES.URI_REST_PROMOTION,
    uri: '/get-all'
  };
  static GET_PROMOTION_WITH_ID: API = {
    module: ROUTES.URI_REST_PROMOTION,
    uri: '/getById/{id}'
  };

  static CREATE_NEW_PROMOTION: API = {
    module: ROUTES.URI_REST_PROMOTION,
    uri: '/create-new'
  };

  static EDIT_PROMOTION: API = {
    module: ROUTES.URI_REST_PROMOTION,
    uri: '/edit'
  };

  static ADD_NEW_PROMOTION_DETAIL: API = {
    module: ROUTES.URI_REST_PROMOTION,
    uri: '/add-detail'
  };

  static DELETE_NEW_PROMOTION: API = {
    module: ROUTES.URI_REST_PROMOTION,
    uri: '/delete/{id}'
  };
}

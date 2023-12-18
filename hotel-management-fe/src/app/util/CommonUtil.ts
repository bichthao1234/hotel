import {AuthAdminService} from "../modules/admin-portal/services/auth-admin.service";

export class CommonUtil {
  public static CLIENT_ID: string = "AR_9HjQn1DWCjFqZ2sFTFrg6V0_6StAShFsLIX_uX3q5_GFSDQUDe5TpQurULjBh_wrJD3G1f2BFJENh";
  public static COMPLETE_PAY_STATUS: string = "COMPLETED";
  private static authAdminService: AuthAdminService;

  public static toLocaleAmount(amount: any) {
    return amount.toLocaleString('vi-VN');
  }

  public static formatAmount(value: any) {
    value = value.replace(/\./g, ''); // remove existing separators
    value = Number(value).toLocaleString('vi-VN'); // format the number
    return value;
  }

  public static removeFormat(value: any) {
    value = value.replace(/\./g, ''); // remove separators
    return value;
  }

  public static getRangeDate(startDate: Date, endDate: Date) {
    let diffInTime = endDate.getTime() - startDate.getTime();
    return diffInTime / (1000 * 3600 * 24);
  }

  public static getStatus(reservationItem: any) {
    return reservationItem.status.value === 1 && CommonUtil.getRangeDate(new Date(), new Date(reservationItem.startDate)) > 0;
  }

  public static getClassOfRoomStatus(roomStatus: any) {
    switch (roomStatus.id) {
      case 'RS00001': {
        return 'status-available-room';
      }
      case 'RS00002': {
        return 'status-rented';
      }
      default: {
        return '';
      }
    }
  }
}

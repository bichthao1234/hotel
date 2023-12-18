export interface GetReservationListRequestDto {
  startDateFrom: Date;
  startDateTo: Date;
  createdDateFrom: Date;
  createdDateTo: Date;
  customerId: string;
  [key: string]: any;
}

import {Component, Input, OnInit} from '@angular/core';
import {RoomService} from "../../services/room.service";

@Component({
  selector: 'app-rental-slip-detail',
  templateUrl: './rental-slip-detail.component.html',
  styleUrls: ['./rental-slip-detail.component.css']
})
export class RentalSlipDetailComponent implements OnInit {
  @Input() roomId: any;
  rentalDetail: any;

  constructor(
    private roomService: RoomService
  ) { }

  ngOnInit() {
    this.getRentalDetail();
  }

  getRentalDetail() {
    this.roomService.getRoomRentalDetail(this.roomId).subscribe((resp) => {
      if (resp && Array.isArray(resp) && resp.length > 0) {
        this.rentalDetail = {
          rentalSlipId: resp[0].rentalSlipId,
          arrivalDate: resp[0].arrivalDate,
          arrivalTime: resp[0].arrivalTime,
          status: resp[0].status,
          departureDate: resp[0].departureDate,
          haveSurcharge: resp[0].haveSurcharge,
          haveService: resp[0].haveService,
        }
      }
    }, (error) => {
      console.log(error)
    });
  }

  getStatus(status: any) {
    return status ? 'Đã thanh toán' : 'Chưa thanh toán'
  }

}

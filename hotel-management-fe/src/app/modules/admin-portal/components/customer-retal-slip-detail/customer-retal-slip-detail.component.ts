import {Component, Input, OnInit} from '@angular/core';
import {RoomService} from "../../services/room.service";

@Component({
  selector: 'app-customer-retal-slip-detail',
  templateUrl: './customer-retal-slip-detail.component.html',
  styleUrls: ['./customer-retal-slip-detail.component.css']
})
export class CustomerRetalSlipDetailComponent implements OnInit {
  @Input() roomId: any;
  roomRentalGuests: any;

  constructor(
    private roomService: RoomService
  ) {  }

  ngOnInit() {
    this.getRoomRentalGuest();
  }

  getRoomRentalGuest() {
    this.roomService.getRoomRentalGuest(this.roomId).subscribe((resp) => {
      console.log(resp)
      if (resp && Array.isArray(resp) && resp.length > 0) {
        this.roomRentalGuests = resp;
      }
    }, (error) => {
      console.log(error)
    });
  }

}

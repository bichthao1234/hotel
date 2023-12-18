import {Component, OnInit} from '@angular/core';
import {RoomClassificationService} from "../../services/room-classification.service";

@Component({
  selector: 'app-advertisement-slides',
  templateUrl: './advertisement-slides.component.html',
  styleUrls: ['./advertisement-slides.component.css']
})
export class AdvertisementSlidesComponent implements OnInit {
  roomNums!: any[];
  startDate = new Date();

  constructor(private roomClassificationService: RoomClassificationService) {
  }

  ngOnInit() {
    this.getAllRoomClassification();
  }

  getAllRoomClassification() {
    this.roomClassificationService.getAll().subscribe((resp) => {
      if (resp) {
        this.roomNums = resp;
      }
    });
  }
}

import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-room-classification-detail-dialog',
  templateUrl: './room-classification-detail-dialog.component.html',
  styleUrls: ['./room-classification-detail-dialog.component.css']
})
export class RoomClassificationDetailDialogComponent implements OnInit {
  conveniences!: any[];

  constructor(public dialogRef: MatDialogRef<RoomClassificationDetailDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any) {
  }

  ngOnInit() {
    this.conveniences = this.data?.roomClassification?.convenienceDetailDtos;
  }

  close() {
    this.dialogRef.close();
  }

}

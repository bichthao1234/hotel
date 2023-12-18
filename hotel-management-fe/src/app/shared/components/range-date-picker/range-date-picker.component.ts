import {Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";
import {MatDateRangePicker} from "@angular/material/datepicker";
import {DataBindingService} from "../../../services/data-binding.service";

@Component({
  selector: 'app-range-date-picker',
  templateUrl: './range-date-picker.component.html',
  styleUrls: ['./range-date-picker.component.css']
})
export class RangeDatePickerComponent implements OnInit {
  @Input() title: any;
  @Input() startDate!: Date;
  @Input() endDate!: Date;
  @Input() isRequire!: boolean;
  @Output() respDateSelected = new EventEmitter<any>();
  @Input() showBtnToggle: any;
  respDate = {start: new Date(), end: new Date()};
  today = new Date();
  month = this.today.getMonth();
  year = this.today.getFullYear();
  campaignOne = new FormGroup({});
  @ViewChild('campaignOnePicker') campaignOnePicker!: MatDateRangePicker<Date>;

  constructor(private dataBindingService: DataBindingService) {
  }


  ngOnInit() {
    if (this.showBtnToggle === undefined) {
      this.showBtnToggle = true;
    }
    if (this.isRequire) {
      this.campaignOne = new FormGroup({
        start: new FormControl(null),
        end: new FormControl(null),
      });

    } else if (this.startDate && this.endDate) {
      this.campaignOne = new FormGroup({
        start: new FormControl(this.startDate),
        end: new FormControl(this.endDate),
      });
    } else {
      this.campaignOne = new FormGroup({
        start: new FormControl(new Date(this.year, this.month, this.today.getDate())),
        end: new FormControl(new Date(this.year, this.month, this.today.getDate() + 1)),
      });
      this.emitRespDate();
    }
  }

  emitRespDate() {
    this.respDate.start = this.campaignOne.get('start')?.value;
    this.respDate.end = this.campaignOne.get('end')?.value;
    if (new Date(this.respDate.start).getTime() === new Date(this.respDate.end).getTime()) {
      this.campaignOne = new FormGroup({
        start: new FormControl(new Date(this.year, this.month, this.today.getDate())),
        end: new FormControl(new Date(this.year, this.month, this.today.getDate() + 1)),
      });
    }
    if (this.respDate.start && this.respDate.end) {
      this.respDateSelected.emit(this.respDate);
    }
  }

  checkAndReopen() {
    if (!this.campaignOne.get('start')?.value || !this.campaignOne.get('end')?.value) {
      this.campaignOnePicker.open();
    } else {
      this.emitRespDate();
    }
  }

  openDatePicker() {
    this.campaignOnePicker.open();
  }

  cleanForm() {
    this.campaignOne.setValue({'start': null, 'end': null});
    this.respDate.start = this.campaignOne.get('start')?.value;
    this.respDate.end = this.campaignOne.get('end')?.value;
    this.respDateSelected.emit(this.respDate);
  }
}

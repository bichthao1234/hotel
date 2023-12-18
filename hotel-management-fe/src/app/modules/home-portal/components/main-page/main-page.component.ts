import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {DataBindingService} from "../../../../services/data-binding.service";

@Component({
  selector: 'app-main-page',
  templateUrl: './main-page.component.html',
  styleUrls: ['./main-page.component.css']
})
export class MainPageComponent implements OnInit {

  respDate = {start: new Date(), end: new Date()};
  roomNum = '1';

  constructor(private router: Router,
              private dataBindingService: DataBindingService) {
  }

  ngOnInit() {
    this.dataBindingService.sendData({isMainPage: true});
  }

  openSearchPage() {
    sessionStorage.setItem('roomNum', this.roomNum);
    sessionStorage.setItem('respDate', JSON.stringify(this.respDate));
    this.router.navigate(['/search']).then(r => {
    });
  }

  sendDate(event: any) {
    this.respDate.start = event.start;
    this.respDate.end = event.end;
  }
}

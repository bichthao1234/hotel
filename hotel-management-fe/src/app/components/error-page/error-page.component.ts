import {Component, OnInit} from '@angular/core';
import {DataBindingService} from "../../services/data-binding.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-error-page',
  templateUrl: './error-page.component.html',
  styleUrls: ['./error-page.component.css']
})
export class ErrorPageComponent implements OnInit {

  constructor(private dataBindingService: DataBindingService,
              private router: Router) {
  }

  ngOnInit() {
    this.dataBindingService.sendData({isMainPage: true});
  }

  goToHomePage() {
    this.router.navigate(['/home']).then(r => {
    });
  }
}

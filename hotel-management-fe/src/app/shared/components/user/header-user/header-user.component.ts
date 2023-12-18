import {Component, HostListener, OnInit} from '@angular/core';
import {DataBindingService} from "../../../../services/data-binding.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-header-user',
  templateUrl: './header-user.component.html',
  styleUrls: ['./header-user.component.css']
})
export class HeaderUserComponent implements OnInit {
  isScrolled = false;
  isMainPage: any;

  constructor(private dataBindingService: DataBindingService,
              private router: Router) {
  }

  ngOnInit() {
    this.dataBindingService.getData().subscribe((data) => {
      if (data) {
        if ('isMainPage' in data) {
          this.isMainPage = data.isMainPage;
        }
      }
    });
  }

  @HostListener("window:scroll", [])
  onWindowScroll() {
    const number = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop || 0;
    if (number > 0) {
      this.isScrolled = true;
      this.dataBindingService.sendData({isScrolled: this.isScrolled});
    } else if (this.isScrolled && number < 100) {
      this.isScrolled = false;
      this.dataBindingService.sendData({isScrolled: this.isScrolled});
    }
  }

  goToHomePage() {
    this.router.navigate(['/home']).then(() => {
    });
  }
}

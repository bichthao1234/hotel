import {Component, Injector, OnInit} from '@angular/core';
import {LoadingService} from "./services/loading.service";
import {NavigationCancel, NavigationEnd, NavigationError, NavigationStart, Router, RouterEvent} from "@angular/router";
import {PrimeNGConfig} from "primeng/api";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  static injector: Injector;
  isLoading = false;
  title = 'hotel-management-fe';

  constructor(private loadingService: LoadingService,
              private injector: Injector,
              private router: Router,
              private primengConfig: PrimeNGConfig) {
    router.events.subscribe((event) => {
      this.navigationInterceptor(event as RouterEvent);
    });
    AppComponent.injector = injector;
    this.loadingService.setApp(this);
  }

  navigationInterceptor(event: RouterEvent) {
    if (event instanceof NavigationStart) {
      this.isLoading = true;
    }
    if (event instanceof NavigationEnd) {
      const self = this;
      setTimeout(() => {
        self.loadingService.stopLoading();
      }, 200);
    }
    if (event instanceof NavigationCancel) {
      this.loadingService.stopLoading();
    }
    if (event instanceof NavigationError) {
      this.loadingService.stopLoading();
    }
  }

  ngOnInit() {
    this.primengConfig.ripple = true;
  }
}

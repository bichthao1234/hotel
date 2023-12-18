import {Injectable} from '@angular/core';
import {AppComponent} from "../app.component";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class LoadingService {
  private app!: AppComponent;
  private processNumber: any[] = [];

  constructor() {
  }

  setApp(appRef: AppComponent) {
    this.app = appRef;
  }

  loading() {
    this.app.isLoading = true;
  }

  stopLoading() {
    if (this.processNumber.length === 0) {
      setTimeout(() => {
        this.app.isLoading = false;
      }, 0);
    }
  }

  forceStop() {
    this.processNumber = [];
    this.app.isLoading = false;
  }

  pushProcessing(req: any) {
    if (req.url) {
      let url = req.url;
      if (url.indexOf(environment.rootURL) >= 0) {
        url = url.replace(environment.rootURL, '');
        this.loading();
        this.processNumber.push(url);
      }
    }
  }

  removeProcessing(req: any) {
    if (req.url && req.type !== 0) {
      let url = req.url;
      if (url.indexOf(environment.rootURL) >= 0) {
        url = url.replace(environment.rootURL, '');
        this.processNumber = this.processNumber.filter(item => {
          return url !== item;
        });
        this.stopLoading();
      }
    }
  }
}

import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomePortalComponent} from "./home-portal.component";
import {MainPageComponent} from "./components/main-page/main-page.component";
import {SearchResultComponent} from "./components/search-result/search-result.component";
import {PaymentPageComponent} from './components/payment-page/payment-page.component';
import {PaymentSuccessComponent} from "./components/payment-success/payment-success.component";
import {ErrorPageComponent} from "../../components/error-page/error-page.component";

const routes: Routes = [
  {
    path: '',
    component: HomePortalComponent,
    children: [
      {
        path: 'home',
        component: MainPageComponent
      },
      {
        path: 'search',
        component: SearchResultComponent,
      },
      {
        path: 'payment',
        component: PaymentPageComponent,
      },
      {
        path: 'payment-success',
        component: PaymentSuccessComponent,
      },
      {
        path: 'not-found',
        component: ErrorPageComponent
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class HomePortalRoutingModule {
}

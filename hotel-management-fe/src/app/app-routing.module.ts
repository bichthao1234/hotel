import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomePortalComponent} from "./modules/home-portal/home-portal.component";

const routes: Routes = [
  {
    path: '', component: HomePortalComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: false, scrollPositionRestoration: 'top'})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}

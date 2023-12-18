import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {MENU_ADMIN_ITEM} from "../../../../modules/admin-portal/menu-admin";
import {RouterFEConstant} from "../../../../constant/RouterFEConstant";
import {AuthAdminService} from "../../../../modules/admin-portal/services/auth-admin.service";
import {MENU_RECEPTIONIST_ITEM} from "../../../../modules/admin-portal/menu-receptionist";

@Component({
  selector: 'app-side-bar-admin',
  templateUrl: './side-bar-admin.component.html',
  styleUrls: ['./side-bar-admin.component.css']
})
export class SideBarAdminComponent implements OnInit, OnChanges {
  @Input() menuItems: any;

  constructor() { }

  ngOnInit() {
    console.log(this.menuItems)
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['menuItems']) {
      // Check if menuItems have changed and log the updated value
      console.log('Updated menuItems:', changes['menuItems'].currentValue);
    }
  }

  highlightItem(event: MouseEvent) {
    (event.target as HTMLElement).classList.add('list-item-highlight');
  }

  unhighlightItem(event: MouseEvent) {
    (event.target as HTMLElement).classList.remove('list-item-highlight');
  }
}

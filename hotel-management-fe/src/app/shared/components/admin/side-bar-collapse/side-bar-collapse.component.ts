import {Component, Input, OnInit} from '@angular/core';
import {MENU_ADMIN_ITEM} from "../../../../modules/admin-portal/menu-admin";

@Component({
  selector: 'app-side-bar-collapse',
  templateUrl: './side-bar-collapse.component.html',
  styleUrls: ['./side-bar-collapse.component.css']
})
export class SideBarCollapseComponent implements OnInit {
  @Input() menuItems: any;

  constructor() {
  }

  ngOnInit() {
  }

  highlightItem(event: MouseEvent) {
    (event.target as HTMLElement).classList.add('list-item-highlight');
  }

  unhighlightItem(event: MouseEvent) {
    (event.target as HTMLElement).classList.remove('list-item-highlight');
  }

}

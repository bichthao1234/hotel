import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {
  @Input() public searchBy!: string;
  @Output() public searchEvent = new EventEmitter<any>();
  @Output() public clearInput = new EventEmitter<any>();
  searchForm = new FormGroup({searchText: new FormControl('')});
  showClearButton = false;

  constructor() {
  }

  ngOnInit() {
    this.searchForm.get('searchText')?.valueChanges.subscribe(value => {
      this.showClearButton = !!value;
      if (this.searchForm.get('searchText')?.value) {
        this.clearInput.emit(false);
      } else {
        this.clearInput.emit(true);
      }
    });
  }

  clickEvent() {
    this.searchEvent.emit(this.searchForm.getRawValue().searchText);
  }

  onInputChange() {
    this.searchForm.get('searchText')?.reset();
    this.clearInput.emit(true);
  }
}

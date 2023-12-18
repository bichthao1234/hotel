import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {FileUpload} from "../../models/file-upload";
import {ImportCustomerService} from "../../services/import-customer.service";
import {Router} from "@angular/router";
import {FormControl} from "@angular/forms";
import * as XLSX from 'xlsx';
import {AdminInforLogin} from "../../models/AdminInforLogin";
import {AuthAdminService} from "../../services/auth-admin.service";
import {HttpXsrfTokenExtractor} from "@angular/common/http";

@Component({
  selector: 'app-import-customer',
  templateUrl: './import-customer.component.html',
  styleUrls: ['./import-customer.component.css']
})
export class ImportCustomerComponent implements OnInit {

  errorMessage = '';
  file = new FileUpload();
  sheetName!: any;
  isMatchedFormatFile = false;
  isClickedPreview = false;
  sheetNames: any;
  customers= [];
  private EXTENSION = 'xlsx';
  sheetNameControls = new FormControl('sheet');
  @ViewChild('formFileMultiple') private fileSelected: ElementRef | undefined;
  constructor(
    private importCustomerService: ImportCustomerService,
    private router: Router,
    private authAdminService: AuthAdminService,
    private tokenExtractor: HttpXsrfTokenExtractor
  ) { }

  ngOnInit() {
  }

  loadFile(event: any) {
    this.isClickedPreview = false;
    this.customers = [];
    this.file = this.initialFile(event);
    const fileExtension = this.file.name.substring(this.file.name.lastIndexOf('.') + 1).toLowerCase()
    if (this.EXTENSION !== '-' && this.EXTENSION.toLowerCase().indexOf(fileExtension) === -1) {
      this.errorMessage = 'The format file is wrong!\nPlease choose file with "xlsx" extension!';
      this.isMatchedFormatFile = false;
    } else {
      this.errorMessage = '';
      this.isMatchedFormatFile = true;
    }

    if(this.isMatchedFormatFile) {
      this.loadSheetNames(this.file);
    }
  }

  loadSheetNames(file: FileUpload) {
    const reader: FileReader = new FileReader();
    reader.onload = (e: any) => {
      /* read workbook */
      const bstr: string = e.target.result;
      const wb: XLSX.WorkBook = XLSX.read(bstr, { type: 'binary' });

      /* get sheet names */
      this.sheetNames = wb.SheetNames;
      this.sheetName = this.sheetNames[0];
    };
    reader.readAsBinaryString(this.file.file as Blob);

  }
  initialFile(event: any) {
    const file = new FileUpload();
    file.name= event.target.files[0].name;
    file.sizeMB = Math.round((event.target.files[0].size / (1024 * 1024)) * 100) / 100;
    file.sizeByte = event.target.files[0].size;
    file.file = event.target.files[0];
    file.sizeType = this.formatBytes(file.sizeByte);
    return file
  }
  formatBytes(bytes:any) {
    if (bytes === 0) {
      return '0 Bytes';
    }

    const k = 1024;
    const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
  }

  uploadFile() {
    this.importCustomerService.importCustomer(this.customers)
      .subscribe((resp) => {
        if (resp) {
          alert('Import success!')
        }
      }, (error) => {
        this.errorMessage = error.error.message;
    })
  }

  previewFile() {
    this.isClickedPreview = true;
    setTimeout(() => {
    }, 300)
    this.importCustomerService.previewFile(this.file, this.sheetName)
      .subscribe((resp) => {
        if (resp) {
          this.customers = resp.result;
          this.errorMessage = '';
        }
      }, (error) => {
        this.errorMessage = error.error.message;
        this.isMatchedFormatFile = false;
        this.isClickedPreview = false;
      })
  }

  chooseSheet(event: any) {
    this.sheetName = event.target.value;
    this.errorMessage = '';
    this.isMatchedFormatFile = true;
  }

  clearFile() {
    // @ts-ignore
    this.fileSelected.nativeElement.value = null;
    this.sheetNames = null;
    this.sheetName = null;
    this.isMatchedFormatFile = false;
    this.isClickedPreview = false;
    this.customers = [];
    this.errorMessage = ''
  }
}

import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";
import {SweetAlertCustomizeService} from "../../../../services/sweet-alert-customize.service";
import {RoomTypeService} from "../../services/room-type.service";
import {RoomKindService} from "../../services/room-kind.service";
import {ConvenienceService} from "../../services/convenience.service";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {AuthAdminService} from "../../services/auth-admin.service";
import {RoomClassificationService} from "../../../home-portal/services/room-classification.service";

@Component({
  selector: 'app-add-room-classification',
  templateUrl: './add-room-classification.component.html',
  styleUrls: ['./add-room-classification.component.css']
})
export class AddRoomClassificationComponent implements OnInit {
  imageUrl: string | ArrayBuffer | null | undefined;
  imageFile!: File
  roomKinds: any;
  roomTypes: any;
  conveniences: any;
  convenienceSelected: any;
  convenienceDetailSelected: any = [];
  isSubmit = false;
  roomClassificationForm = new FormGroup({
    id: new FormControl(),
    guestNum: new FormControl('', [Validators.required]),
    roomTypeDto: new FormControl('', [Validators.required]),
    roomKindDto: new FormControl('', [Validators.required]),
    description: new FormControl(''),
    priceRoomClassifications: new FormControl('', [Validators.required]),
    convenienceSelected: new FormControl()
  });
  @ViewChild('fileInput') fileInput!: ElementRef;

  constructor(
    private dialogRef: MatDialogRef<AddRoomClassificationComponent>,
    private sweetAlertCustomizeService: SweetAlertCustomizeService,
    private roomTypeService: RoomTypeService,
    private roomKindService: RoomKindService,
    private convenienceService: ConvenienceService,
    private authAdminService: AuthAdminService,
    private roomClassificationService: RoomClassificationService
  ) {
  }

  ngOnInit() {
    this.roomClassificationForm = new FormGroup({
      id: new FormControl(),
      guestNum: new FormControl('', [Validators.required]),
      roomTypeDto: new FormControl('', [Validators.required]),
      roomKindDto: new FormControl('', [Validators.required]),
      description: new FormControl(''),
      priceRoomClassifications: new FormControl('', [Validators.required]),
      convenienceSelected: new FormControl()
    });
    this.getAllRoomTypes();
    this.getAllRoomKinds();
    this.getAllConvenience();
  }

  close() {
    this.dialogRef.close();
  }

  previewImage(event: any) {
    const file = event.target.files[0];
    const fileSize = file.size / 1024 / 1024; // size in MB
    const validImageTypes = ['image/jpeg', 'image/png', 'image/gif', 'image/jpg'];
    if (fileSize > 2) {
      this.sweetAlertCustomizeService.showWarringAlertRightPopup('Dung lượng file phải nhỏ hơn 2MB');
      return;
    }
    if (!validImageTypes.includes(file.type)) {
      this.sweetAlertCustomizeService.showWarringAlertRightPopup('Chọn một hình ảnh để tải lên');
      return;
    }
    this.imageFile = file;
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => {
      this.imageUrl = reader.result;
    };
  }

  getAllRoomTypes() {
    this.roomTypeService.getAll().subscribe((resp => {
      if (resp && Array.isArray(resp) && resp.length > 0) {
        this.roomTypes = resp;
      }
    }))
  }

  getAllRoomKinds() {
    this.roomKindService.getAll().subscribe((resp => {
      if (resp && Array.isArray(resp) && resp.length > 0) {
        this.roomKinds = resp;
      }
    }))
  }

  getAllConvenience() {
    this.convenienceService.getAll().subscribe((resp => {
      if (resp && Array.isArray(resp) && resp.length > 0) {
        this.conveniences = resp;
      }
    }))
  }

  removeImage() {
    this.imageUrl = null;
    this.fileInput.nativeElement.value = null;
  }

  save() {
    this.isSubmit = true;
    let convenienceDetailSelected = this.convenienceDetailSelected.map((item: any) => {
      return {
        convenience: item.convenience,
        roomClassification: null,
        quantity: item.quantity.value,
        description: null
      };
    });
    for (let item of convenienceDetailSelected) {
      if (!item.quantity || !(item.quantity > 0)) {
        return;
      }
    }
    if (!this.imageFile) {
      return;
    }
    let employee = this.authAdminService.getLoginAdminInfor();
    if (!employee) {
      return;
    }
    if (this.roomClassificationForm.invalid) {
      return;
    }
    let obj = this.roomClassificationForm.getRawValue();
    let request = {
      id: null,
      guestNum: obj.guestNum,
      description: obj.description,
      roomTypeDto: obj.roomTypeDto,
      roomKindDto: obj.roomKindDto,
      convenienceIds: convenienceDetailSelected,
      priceRoomClassifications: obj.priceRoomClassifications,
      employeeDto: {id: employee?.id},
    };
    console.log(request)
    this.roomClassificationService.createNewRoomClassification(request).subscribe(
      (resp: any) => {
        console.log(resp);
        if (resp?.result) {
          let roomClassificationId = resp.result;
          this.roomClassificationService.updateImageRoomClassification(roomClassificationId, this.imageFile).subscribe(
            (resp: any) => {
              if (resp) {
                this.sweetAlertCustomizeService.showSuccessAlertRightPopup("Thêm hạng phòng thành công!")
              }
            }
          )
        } else {
          this.sweetAlertCustomizeService.showErrorAlertRightPopup("Thêm hạng phòng không thành công!")
        }
      },
      (error) => {
        console.log(error);
      }
    );
  }

  checkQuantityConvenience(item: any) {
    return item.quantity?.value > 0;
  }

  convertConvenienceSelected() {
    this.convenienceSelected = this.getFormControl('convenienceSelected').value;
    console.log(this.convenienceSelected)
    let newConvenienceDetailSelected = [];
    if (this.convenienceSelected && this.convenienceSelected?.length) {
      for (let convenience of this.convenienceSelected) {
        let existingConvenienceDetail = this.convenienceDetailSelected.find((detail: any) => detail.convenience.id === convenience.id);
        if (existingConvenienceDetail) {
          newConvenienceDetailSelected.push(existingConvenienceDetail);
        } else {
          let convenienceDetail = {
            convenience: convenience,
            roomClassification: null,
            quantity: new FormControl(0, [Validators.required, Validators.min(0), Validators.max(99)]),
            description: null
          }
          newConvenienceDetailSelected.push(convenienceDetail);
        }
      }
    }
    this.convenienceDetailSelected = newConvenienceDetailSelected;
    console.log(this.convenienceDetailSelected);
  }

  getFormControl(s: string) {
    return this.roomClassificationForm.controls[s] as FormControl;
  }
}

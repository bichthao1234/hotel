import {Component, ElementRef, Inject, OnInit, ViewChild} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {SweetAlertCustomizeService} from "../../../../services/sweet-alert-customize.service";
import {RoomTypeService} from "../../services/room-type.service";
import {RoomKindService} from "../../services/room-kind.service";
import {ConvenienceService} from "../../services/convenience.service";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {AuthAdminService} from "../../services/auth-admin.service";
import {RoomClassificationService} from "../../../home-portal/services/room-classification.service";

@Component({
  selector: 'app-add-room-classification',
  templateUrl: './edit-room-classification.component.html',
  styleUrls: ['./edit-room-classification.css']
})
export class EditRoomClassificationComponent implements OnInit {
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
    convenienceSelected: new FormControl()
  });
  @ViewChild('fileInput') fileInput!: ElementRef;

  constructor(
    private dialogRef: MatDialogRef<EditRoomClassificationComponent>,
    private sweetAlertCustomizeService: SweetAlertCustomizeService,
    private roomTypeService: RoomTypeService,
    private roomKindService: RoomKindService,
    private convenienceService: ConvenienceService,
    private authAdminService: AuthAdminService,
    private roomClassificationService: RoomClassificationService,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
  }

  ngOnInit() {
    let conveniences = this.getConvenience();
    this.convenienceSelected = conveniences;
    this.getAllRoomTypes();
    this.getAllRoomKinds();
    this.getAllConvenience();
    this.roomClassificationForm = new FormGroup({
      id: new FormControl(this.data.id),
      guestNum: new FormControl(this.data.guestNum, [Validators.required]),
      roomTypeDto: new FormControl(this.data.roomType, [Validators.required]),
      roomKindDto: new FormControl(this.data.roomKind , [Validators.required]),
      description: new FormControl(this.data.description),
      convenienceSelected: new FormControl(this.convenienceSelected)
    });
    this.convertConvenienceSelected(true);
    this.roomClassificationService.getImage(this.data.id).subscribe((resp) => {
      if ('result' in resp) {
        this.imageUrl = `http://localhost:9095/hotel-ws/api/v1/image/${resp.result}`;
      }
    })
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
        roomClassification: {id: this.data.id},
        quantity: item.quantity.value,
        description: null
      };
    });
    if (!convenienceDetailSelected || convenienceDetailSelected?.length === 0) {
      return;
    }
    for (let item of convenienceDetailSelected) {
      if (!item.quantity || !(item.quantity > 0)) {
        return;
      }
    }
    if (!this.imageFile && !this.imageUrl) {
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
      id: obj.id,
      guestNum: obj.guestNum,
      description: obj.description,
      roomTypeDto: obj.roomTypeDto,
      roomKindDto: obj.roomKindDto,
      convenienceIds: convenienceDetailSelected,
      priceRoomClassifications: obj.priceRoomClassifications,
      employeeDto: {id: employee?.id},
    };
    this.roomClassificationService.editRoomClassification(request).subscribe((resp: any) => {
        if (resp?.result) {
          if (!!this.imageFile) {
            let roomClassificationId = resp.result;
            this.roomClassificationService.updateImageRoomClassification(roomClassificationId, this.imageFile).subscribe(
              (resp: any) => {
                if (resp) {
                  this.sweetAlertCustomizeService.showSuccessAlertRightPopup("Chỉnh sửa hạng phòng thành công!");
                  this.dialogRef.close({result: true});
                }
              }
            )
          } else {
            this.sweetAlertCustomizeService.showSuccessAlertRightPopup("Chỉnh sửa hạng phòng thành công!");
            this.dialogRef.close({result: true});
          }
        } else {
          this.sweetAlertCustomizeService.showErrorAlertRightPopup("Chỉnh sửa hạng phòng không thành công!");
        }
      },
      (error) => {
        this.sweetAlertCustomizeService.showErrorAlertRightPopup(error.error.error);
      }
    );
  }

  checkQuantityConvenience(item: any) {
    return item.quantity?.value > 0;
  }
  getQuantityInData(convenience: any) {
    return this.data.convenienceDetailDtos.find((detail: any) => detail.convenience.id === convenience.id)?.quantity;
  }
  convertConvenienceSelected(isInitial: boolean) {
    this.convenienceSelected = this.getFormControl('convenienceSelected').value;
    let newConvenienceDetailSelected = [];
    if (this.convenienceSelected && this.convenienceSelected?.length) {
      for (let convenience of this.convenienceSelected) {
        let existingConvenienceDetail = this.convenienceDetailSelected.find((detail: any) => detail.convenience.id === convenience.id);
        if (existingConvenienceDetail && !isInitial) {
          newConvenienceDetailSelected.push(existingConvenienceDetail);
        } else {
          let convenienceDetail = {
            convenience: convenience,
            roomClassification: null,
            quantity: new FormControl(isInitial ? this.getQuantityInData(convenience): 0, [Validators.required, Validators.min(0), Validators.max(99)]),
            description: null
          }
          newConvenienceDetailSelected.push(convenienceDetail);
        }
      }
    }
    this.convenienceDetailSelected = newConvenienceDetailSelected;
  }

  getFormControl(s: string) {
    return this.roomClassificationForm.controls[s] as FormControl;
  }

  public getConvenience() {
    return this.data.convenienceDetailDtos.map((item: any) => item.convenience);
  }
}

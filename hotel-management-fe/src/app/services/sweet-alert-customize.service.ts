import {Injectable} from '@angular/core';
import Swal, {SweetAlertIcon} from "sweetalert2";

@Injectable({
  providedIn: 'root'
})
export class SweetAlertCustomizeService {

  constructor() {
  }

  showAlert(title: string, text: string, icon: SweetAlertIcon) {
    Swal.fire({
      title,
      text,
      icon,
    }).then(r => {
    });
  }
  showAlertCustomize(title: any,) {
    return  Swal.fire({
      title: title,
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#afafaf',
      confirmButtonText: 'Next',
      cancelButtonText: 'Cancel'
    })
  }
  showErrorAlertRightPopup(content: any) {
    const Toast = Swal.mixin({
      toast: true,
      position: 'top-end',
      showConfirmButton: false,
      timerProgressBar: false,
      showCloseButton: true,
      timer: 4000,
      didOpen: (toast) => {
        toast.addEventListener('mouseenter', Swal.stopTimer)
        toast.addEventListener('mouseleave', Swal.resumeTimer)
      }
    })

    Toast.fire({
      icon: 'error',
      title: content
    })
  }
  showSuccessAlertRightPopup(content: any) {
    const Toast = Swal.mixin({
      toast: true,
      position: 'top-end',
      showConfirmButton: false,
      timerProgressBar: false,
      showCloseButton: true,
      timer: 4000,
      didOpen: (toast) => {
        toast.addEventListener('mouseenter', Swal.stopTimer)
        toast.addEventListener('mouseleave', Swal.resumeTimer)
      }
    })

    Toast.fire({
      icon: 'success',
      title: content
    })
  }
  showWarringAlertRightPopup(content: any) {
    const Toast = Swal.mixin({
      toast: true,
      position: 'top-end',
      showConfirmButton: false,
      timerProgressBar: false,
      showCloseButton: true,
      timer: 4000,
      didOpen: (toast) => {
        toast.addEventListener('mouseenter', Swal.stopTimer)
        toast.addEventListener('mouseleave', Swal.resumeTimer)
      }
    })

    Toast.fire({
      icon: 'warning',
      title: content
    })
  }
}
